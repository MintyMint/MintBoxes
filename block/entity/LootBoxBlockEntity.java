package com.mint.mintboxes.block.entity;

import com.mint.mintboxes.config.ConfigValues;
import com.mint.mintboxes.loot.RewardTables;
import com.mint.mintboxes.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

/**
 * Controls the opening/closing cycle of the loot box and triggers rewards/particles.
 */
public class LootBoxBlockEntity extends BlockEntity {

    private final String tier;
    private boolean busy = false;
    private int timer = 0;
    private Phase phase = Phase.IDLE;
    private UUID openerId = null;

    public LootBoxBlockEntity(BlockPos pos, BlockState state, String tier) {
        super(ModRegistry.LOOT_BOX_BE_TYPE.get(), pos, state);
        this.tier = tier;
    }

    public String getTier() {
        return tier;
    }

    public boolean isBusy() {
        return busy;
    }

    public boolean tryBeginOpening(ServerLevel level, BlockPos pos, ServerPlayer player) {
        if (busy) return false;

        busy = true;
        openerId = player.getUUID();
        phase = Phase.PRE_OPEN;
        timer = ConfigValues.OPEN_TICKS;

        // First feedback: key accepted
        level.playSound(null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1.0f, 1.0f);
        return true;
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, LootBoxBlockEntity be) {
        if (!be.busy) return;

        if (be.timer > 0) {
            be.timer--;
            return;
        }

        switch (be.phase) {
            case PRE_OPEN -> {
                // After short delay, box opens
                level.playSound(null, pos, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 1.0f, 1.0f);
                be.phase = Phase.WAIT_REWARDS;
                be.timer = ConfigValues.REWARD_DELAY_TICKS;
            }
            case WAIT_REWARDS -> {
                // Spawn rewards
                if (be.openerId != null) {
                    var p = level.getPlayerByUUID(be.openerId); // may be Player in some mappings
                    if (p instanceof ServerPlayer opener) {
                        RewardTables.spawnRolledRewards(level, pos, opener, be.tier);
                        be.spawnRainbowParticles(level, pos);
                    }
                }
                level.playSound(null, pos, SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 1.0f, 1.0f);
                be.phase = Phase.WAIT_CLOSE;
                be.timer = ConfigValues.CLOSE_TICKS;
            }
            case WAIT_CLOSE -> {
                // Close box
                level.playSound(null, pos, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 1.0f, 1.0f);
                be.phase = Phase.LOCKOUT;
                be.timer = ConfigValues.LOCKOUT_TICKS;
            }
            case LOCKOUT -> {
                // Ready again
                be.busy = false;
                be.phase = Phase.IDLE;
                be.openerId = null;
                level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            default -> {}
        }
    }

    private void spawnRainbowParticles(ServerLevel level, BlockPos pos) {
        Vec3 center = new Vec3(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
        for (int i = 0; i < 24; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = 0.5 + level.random.nextDouble() * 0.5;
            double x = center.x + Math.cos(angle) * radius;
            double y = center.y + level.random.nextDouble() * 0.6;
            double z = center.z + Math.sin(angle) * radius;

            level.sendParticles(
                    net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                    x, y, z, 1,
                    0.0, 0.05, 0.0, 0.1
            );
        }
    }

    // NBT (1.21+: saveAdditional(tag, lookup) / loadAdditional(tag, lookup) exist, but these no-lookup forms still compile)
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.saveAdditional(tag, lookup);
        tag.putBoolean("Busy", busy);
        tag.putInt("Timer", timer);
        tag.putString("Phase", phase.name());
        if (openerId != null) tag.putUUID("Opener", openerId);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.loadAdditional(tag, lookup);
        busy = tag.getBoolean("Busy");
        timer = tag.getInt("Timer");
        try {
            phase = Phase.valueOf(tag.getString("Phase"));
        } catch (Exception e) {
            phase = Phase.IDLE;
        }
        openerId = tag.hasUUID("Opener") ? tag.getUUID("Opener") : null;
    }

    private enum Phase {
        IDLE, PRE_OPEN, WAIT_REWARDS, WAIT_CLOSE, LOCKOUT
    }
}
