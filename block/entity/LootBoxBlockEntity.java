package com.mint.mintboxes.block.entity;

import com.mint.mintboxes.config.ConfigValues;
import com.mint.mintboxes.loot.RewardTables;
import com.mint.mintboxes.registry.ModParticles;
import com.mint.mintboxes.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

/**
 * LootBox BlockEntity – handles open/close cycle, rewards, and lid animation.
 */
public class LootBoxBlockEntity extends BlockEntity {

    private final String tier;
    private boolean busy = false;
    private int timer = 0;
    private Phase phase = Phase.IDLE;
    private UUID openerId = null;

    // Lid animation
    private float lidProgress = 0.0f;      // current progress
    private float prevLidProgress = 0.0f;  // for smooth interpolation

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

    // -------------------------
    // Server logic
    // -------------------------
    public boolean tryBeginOpening(ServerLevel level, BlockPos pos, ServerPlayer player) {
        if (busy) return false;

        busy = true;
        openerId = player.getUUID();
        phase = Phase.PRE_OPEN;
        timer = ConfigValues.OPEN_TICKS;

        // Feedback: key accepted
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
                // Lid is opening (client will animate)
                level.playSound(null, pos, SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 1.0f, 1.0f);
                be.phase = Phase.WAIT_REWARDS;
                be.timer = ConfigValues.REWARD_DELAY_TICKS;
            }
            case WAIT_REWARDS -> {
                if (be.openerId != null) {
                    var p = level.getPlayerByUUID(be.openerId);
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
                // Lid will close (client animates)
                level.playSound(null, pos, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 1.0f, 1.0f);
                be.phase = Phase.LOCKOUT;
                be.timer = ConfigValues.LOCKOUT_TICKS;
            }
            case LOCKOUT -> {
                be.busy = false;
                be.phase = Phase.IDLE;
                be.openerId = null;
                level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            default -> {}
        }
    }

    // -------------------------
    // Client logic
    // -------------------------
    public static void clientTick(Level level, BlockPos pos, BlockState state, LootBoxBlockEntity be) {
        be.prevLidProgress = be.lidProgress;

        if (be.phase == Phase.PRE_OPEN || be.phase == Phase.WAIT_REWARDS) {
            if (be.lidProgress < 1.0f) be.lidProgress += 0.1f;
        } else if (be.phase == Phase.WAIT_CLOSE || be.phase == Phase.LOCKOUT) {
            if (be.lidProgress > 0.0f) be.lidProgress -= 0.1f;
        }
    }

    public float getLidAngle(float partialTicks) {
        return prevLidProgress + (lidProgress - prevLidProgress) * partialTicks;
    }

    // -------------------------
    // Particles
    // -------------------------
    private void spawnRainbowParticles(ServerLevel level, BlockPos pos) {
        Vec3 center = new Vec3(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);

        for (int i = 0; i < 100; i++) { // increase count for more "explosive" look
            // Spawn directly at the box center
            double x = center.x;
            double y = center.y;
            double z = center.z;

            // Velocity is ignored here because RainbowParticle handles it in its constructor.
            level.sendParticles(
                    ModParticles.RAINBOW.get(),
                    x, y, z,
                    1, // count: 1 particle per call
                    0.0, 0.0, 0.0, // no spread, custom particle handles its own motion
                    0.0            // speed (not used by SimpleParticleType)
            );
        }
    }

    // -------------------------
    // NBT
    // -------------------------
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.saveAdditional(tag, lookup);
        tag.putBoolean("Busy", busy);
        tag.putInt("Timer", timer);
        tag.putString("Phase", phase.name());
        tag.putFloat("LidProgress", lidProgress);
        if (openerId != null) tag.putUUID("Opener", openerId);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.loadAdditional(tag, lookup);
        busy = tag.getBoolean("Busy");
        timer = tag.getInt("Timer");
        lidProgress = tag.getFloat("LidProgress");
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
