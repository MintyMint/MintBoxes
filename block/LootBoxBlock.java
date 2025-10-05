package com.mint.mintboxes.block;

import net.minecraft.world.level.block.Block;
import com.mint.mintboxes.block.entity.LootBoxBlockEntity;
import com.mint.mintboxes.registry.ModRegistry;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class LootBoxBlock extends BaseEntityBlock {
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    private final String tier;

    // Custom codec because ctor takes tier
    public static MapCodec<LootBoxBlock> makeCodec(String tier) {
        return simpleCodec(props -> new LootBoxBlock(props, tier));
    }

    private final MapCodec<? extends LootBoxBlock> codec;

    @Override
    public MapCodec<? extends LootBoxBlock> codec() {
        return codec;
    }

    public LootBoxBlock(Properties props, String tier) {
        super(props);
        this.tier = tier;
        this.codec = simpleCodec(p -> new LootBoxBlock(p, tier));
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, false));
    }

    public String getTier() {
        return tier;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LootBoxBlockEntity(pos, state, tier);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null :
                createTickerHelper(type, ModRegistry.LOOT_BOX_BE_TYPE.get(),
                        (sl, pos, st, be) -> {
                            if (sl instanceof ServerLevel server && be instanceof LootBoxBlockEntity lbe) {
                                lbe.serverTick(server, pos, st, lbe);
                            }
                        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof LootBoxBlockEntity lbe)) return InteractionResult.PASS;

        if (lbe.isBusy()) {
            player.displayClientMessage(Component.literal("The box is busy..."), true);
            return InteractionResult.FAIL;
        }

        // Fallback sound/message if used without a key
        level.playSound(null, pos, SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 1.0f, 1.0f);
        player.displayClientMessage(Component.literal("You need a key to open this."), true);
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(OPEN, false);
    }
}
