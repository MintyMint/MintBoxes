package com.mint.mintboxes.item;

import com.mint.mintboxes.block.LootBoxBlock;
import com.mint.mintboxes.block.entity.LootBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;

public class KeyItem extends Item {
    private final String tier;

    public KeyItem(String tier, Properties props) {
        super(props);
        this.tier = tier;
    }

    public String getTier() {
        return tier;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        var level = ctx.getLevel();
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockPos pos = ctx.getClickedPos();
        var state = level.getBlockState(pos);
        var block = state.getBlock();
        var player = ctx.getPlayer();

        if (!(player instanceof ServerPlayer sp)) return InteractionResult.FAIL;

        if (block instanceof LootBoxBlock lb) {
            if (!lb.getTier().equals(this.tier)) {
                sp.displayClientMessage(Component.literal("That key doesn’t fit this box!"), true);
                level.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 1f, 1f);
                return InteractionResult.FAIL;
            }

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof LootBoxBlockEntity box) {
                if (box.tryBeginOpening((ServerLevel) level, pos, sp)) {
                    ctx.getItemInHand().shrink(1); // consume key
                    sp.displayClientMessage(Component.literal("The box accepts your key!"), true);
                    return InteractionResult.SUCCESS;
                } else {
                    sp.displayClientMessage(Component.literal("The box is busy!"), true);
                    return InteractionResult.FAIL;
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.tier.equals("special") || super.isFoil(stack);
    }
}
