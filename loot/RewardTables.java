package com.mint.mintboxes.loot;

import com.mint.mintboxes.MintBoxes;
import com.mint.mintboxes.config.ConfigValues;
import com.mint.mintboxes.config.DefaultValues;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * Handles reward spawning for loot boxes.
 */
public class RewardTables {

    /**
     * Spawn rolled rewards for a given tier.
     */
    public static void spawnRolledRewards(ServerLevel level, BlockPos pos, ServerPlayer player, String tier) {
        // Get configured list or fallback to defaults
        List<String> rawList = ConfigValues.REWARD_TABLES.getOrDefault(tier, DefaultValues.getDefaultRewards(tier));
        if (rawList == null || rawList.isEmpty()) {
            player.displayClientMessage(Component.literal("[MintBoxes] No rewards configured for tier=" + tier), false);
            return;
        }

        Random rand = new Random();

        // Decide how many rolls to do
        int rolls = getRandomBetween(level,
                ConfigValues.REWARD_MIN_ROLLS.getOrDefault(tier, 1),
                ConfigValues.REWARD_MAX_ROLLS.getOrDefault(tier, 1));

        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal("You receive:"));

        for (int i = 0; i < rolls; i++) {
            for (String entry : rawList) {
                RewardDefinition def = RewardDefinition.parse(entry);
                if (rand.nextDouble() <= def.chance) {
                    int amount = getRandomBetween(level, def.min, def.max);

                    // Resolve item
                    ResourceLocation id = ResourceLocation.parse(def.itemId);
                    var holder = level.registryAccess()
                            .lookupOrThrow(Registries.ITEM)
                            .get(net.minecraft.resources.ResourceKey.create(Registries.ITEM, id));

                    if (holder.isPresent()) {
                        ItemStack stack = new ItemStack(holder.get(), amount);
                        def.applyEnchantments(stack, level.registryAccess());

                        // Drop item in world
                        ItemEntity entity = new ItemEntity(level,
                                pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                                stack);
                        level.addFreshEntity(entity);

                        // Build colored message
                        MutableComponent msg = def.getDisplayName(stack).copy();
                        lines.add(msg.append(Component.literal(" x" + amount).withStyle(style -> style.withColor(0xFFFF55))));
                    }
                }
            }
        }

        // Send messages to player
        for (Component line : lines) {
            player.displayClientMessage(line, false);
        }

        System.out.println("[MintBoxes] Spawning rewards for tier=" + tier + ", entries=" + rawList.size());
    }

    private static int getRandomBetween(ServerLevel level, int min, int max) {
        if (max <= min) return min;
        return min + level.getRandom().nextInt(max - min + 1);
    }
    private static final Map<String, List<String>> rewardCache = new HashMap<>();

    public static void rebuildFromConfig() {
        rewardCache.clear();

        for (String tier : ConfigValues.REWARD_TABLES.keySet()) {
            List<String> rawList = ConfigValues.REWARD_TABLES.get(tier);
            if (rawList == null || rawList.isEmpty()) {
                rawList = DefaultValues.getDefaultRewards(tier); // fallback
            }
            rewardCache.put(tier, rawList);
        }

        MintBoxes.LOGGER.info("[MintBoxes] Rebuilt reward tables from config.");
    }

}
