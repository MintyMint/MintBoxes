package com.mint.mintboxes.registry;

import com.mint.mintboxes.MintBoxes;
import com.mint.mintboxes.block.LootBoxBlock;
import com.mint.mintboxes.block.entity.LootBoxBlockEntity;
import com.mint.mintboxes.item.KeyItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRegistry {

    // Deferred registers
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, MintBoxes.MODID);

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, MintBoxes.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MintBoxes.MODID);

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MintBoxes.MODID);

    // Tiers
    public static final String TIER_STONE = "stone";
    public static final String TIER_IRON = "iron";
    public static final String TIER_GOLD = "gold";
    public static final String TIER_DIAMOND = "diamond";
    public static final String TIER_NETHERITE = "netherite";
    public static final String TIER_SPECIAL = "special";

    // Loot box blocks
    public static final DeferredHolder<Block, LootBoxBlock> LOOT_BOX_STONE =
            BLOCKS.register("loot_box_stone", () -> new LootBoxBlock(Block.Properties.of(), TIER_STONE));
    public static final DeferredHolder<Block, LootBoxBlock> LOOT_BOX_IRON =
            BLOCKS.register("loot_box_iron", () -> new LootBoxBlock(Block.Properties.of(), TIER_IRON));
    public static final DeferredHolder<Block, LootBoxBlock> LOOT_BOX_GOLD =
            BLOCKS.register("loot_box_gold", () -> new LootBoxBlock(Block.Properties.of(), TIER_GOLD));
    public static final DeferredHolder<Block, LootBoxBlock> LOOT_BOX_DIAMOND =
            BLOCKS.register("loot_box_diamond", () -> new LootBoxBlock(Block.Properties.of(), TIER_DIAMOND));
    public static final DeferredHolder<Block, LootBoxBlock> LOOT_BOX_NETHERITE =
            BLOCKS.register("loot_box_netherite", () -> new LootBoxBlock(Block.Properties.of(), TIER_NETHERITE));
    public static final DeferredHolder<Block, LootBoxBlock> LOOT_BOX_SPECIAL =
            BLOCKS.register("loot_box_special", () -> new LootBoxBlock(Block.Properties.of(), TIER_SPECIAL));

    // Loot box block entity
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LootBoxBlockEntity>> LOOT_BOX_BE_TYPE =
            BLOCK_ENTITIES.register("loot_box",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new LootBoxBlockEntity(pos, state, TIER_STONE),
                            LOOT_BOX_STONE.get(),
                            LOOT_BOX_IRON.get(),
                            LOOT_BOX_GOLD.get(),
                            LOOT_BOX_DIAMOND.get(),
                            LOOT_BOX_NETHERITE.get(),
                            LOOT_BOX_SPECIAL.get()
                    ).build(null)
            );

    // Keys
    public static final DeferredHolder<Item, KeyItem> KEY_STONE =
            ITEMS.register("key_stone", () -> new KeyItem(TIER_STONE, new Item.Properties()));
    public static final DeferredHolder<Item, KeyItem> KEY_IRON =
            ITEMS.register("key_iron", () -> new KeyItem(TIER_IRON, new Item.Properties()));
    public static final DeferredHolder<Item, KeyItem> KEY_GOLD =
            ITEMS.register("key_gold", () -> new KeyItem(TIER_GOLD, new Item.Properties()));
    public static final DeferredHolder<Item, KeyItem> KEY_DIAMOND =
            ITEMS.register("key_diamond", () -> new KeyItem(TIER_DIAMOND, new Item.Properties()));
    public static final DeferredHolder<Item, KeyItem> KEY_NETHERITE =
            ITEMS.register("key_netherite", () -> new KeyItem(TIER_NETHERITE, new Item.Properties()));
    public static final DeferredHolder<Item, KeyItem> KEY_SPECIAL =
            ITEMS.register("key_special", () -> new KeyItem(TIER_SPECIAL, new Item.Properties()));

    // BlockItems
    public static final DeferredHolder<Item, BlockItem> ITEM_LOOT_BOX_STONE =
            ITEMS.register("loot_box_stone", () -> new BlockItem(LOOT_BOX_STONE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> ITEM_LOOT_BOX_IRON =
            ITEMS.register("loot_box_iron", () -> new BlockItem(LOOT_BOX_IRON.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> ITEM_LOOT_BOX_GOLD =
            ITEMS.register("loot_box_gold", () -> new BlockItem(LOOT_BOX_GOLD.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> ITEM_LOOT_BOX_DIAMOND =
            ITEMS.register("loot_box_diamond", () -> new BlockItem(LOOT_BOX_DIAMOND.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> ITEM_LOOT_BOX_NETHERITE =
            ITEMS.register("loot_box_netherite", () -> new BlockItem(LOOT_BOX_NETHERITE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> ITEM_LOOT_BOX_SPECIAL =
            ITEMS.register("loot_box_special", () -> new BlockItem(LOOT_BOX_SPECIAL.get(), new Item.Properties()));

    // Creative Tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB =
            TABS.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mintboxes"))
                    .icon(() -> new ItemStack(KEY_SPECIAL.get()))
                    .displayItems((params, output) -> {
                        // Add all keys
                        output.accept(KEY_STONE.get());
                        output.accept(KEY_IRON.get());
                        output.accept(KEY_GOLD.get());
                        output.accept(KEY_DIAMOND.get());
                        output.accept(KEY_NETHERITE.get());
                        output.accept(KEY_SPECIAL.get());

                        // Add all loot boxes
                        output.accept(ITEM_LOOT_BOX_STONE.get());
                        output.accept(ITEM_LOOT_BOX_IRON.get());
                        output.accept(ITEM_LOOT_BOX_GOLD.get());
                        output.accept(ITEM_LOOT_BOX_DIAMOND.get());
                        output.accept(ITEM_LOOT_BOX_NETHERITE.get());
                        output.accept(ITEM_LOOT_BOX_SPECIAL.get());
                    })
                    .build());

    // Register everything
    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        TABS.register(bus);
    }
}
