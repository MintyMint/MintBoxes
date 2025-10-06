package com.mint.mintboxes.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Central place for ALL hardcoded defaults.
 * Used as fallback if configs fail or are missing.
 */
public final class DefaultValues {
    private DefaultValues() {}

    // ---------------------------
    // LootBox Timings
    // ---------------------------
    public static final int DEFAULT_OPEN_TICKS = 30;            // small delay before opening sound
    public static final int DEFAULT_REWARD_DELAY_TICKS = 30;   // ticks until loot spawns
    public static final int DEFAULT_CLOSE_TICKS = 30;           // ticks until box closes
    public static final int DEFAULT_LOCKOUT_TICKS = 200;        // ticks before it can be opened again

    // ---------------------------
    // Key Drop Chances
    // ---------------------------
    public static final double DEFAULT_MOB_KILL_CHANCE = 0.01;       // 1% by default
    public static final double DEFAULT_ORE_BREAK_CHANCE = 0.005;     // 0.5%
    public static final double DEFAULT_FISHING_CHANCE = 0.005;       // 0.5%
    public static final double DEFAULT_CROP_HARVEST_CHANCE = 0.002;  // 0.2%

    // ---------------------------
    // Enchant-based Upgrades
    // ---------------------------
    public static final boolean DEFAULT_ALLOW_ENCHANT_UPGRADES = true;
    public static final double DEFAULT_UPGRADE_CHANCE = 0.25;        // 25% per roll
    public static final double DEFAULT_IRON_UPGRADE_CHANCE = 0.5;    // 50% chance → Iron
    public static final double DEFAULT_GOLD_UPGRADE_CHANCE = 0.25;   // 25% chance → Gold
    public static final double DEFAULT_DIAMOND_UPGRADE_CHANCE = 0.10;// 10% chance → Diamond

    // ---------------------------
    // Crafting Catalysts
    // ---------------------------
    public static final Map<String, String> CRAFTING_UPGRADE_CATALYSTS = new HashMap<>();
    static {
        CRAFTING_UPGRADE_CATALYSTS.put("stone_to_iron", "minecraft:iron_block");
        CRAFTING_UPGRADE_CATALYSTS.put("iron_to_gold", "minecraft:gold_block");
        CRAFTING_UPGRADE_CATALYSTS.put("gold_to_diamond", "minecraft:diamond_block");
        // Netherite crafting is disabled by default
    }

    // ---------------------------
    // Rarity Colors
    // ---------------------------
    public static final Map<String, String> RARITY_COLORS = new HashMap<>();
    static {
        RARITY_COLORS.put("common",    "#FFFFFF"); // white
        RARITY_COLORS.put("uncommon",  "#55FF55"); // green
        RARITY_COLORS.put("rare",      "#5555FF"); // blue
        RARITY_COLORS.put("epic",      "#AA00AA"); // purple
        RARITY_COLORS.put("legendary", "#FFAA00"); // orange/gold
    }

    // ---------------------------
    // Default Loot Tables
    // Format: "item, chance, min, max, rarity[, enchant:level...]"
    // ---------------------------

    public static final List<String> STONE_REWARDS = Arrays.asList(
            "minecraft:bread, 0.5, 1, 3, common",
            "minecraft:apple, 0.5, 1, 2, common"
    );

    public static final List<String> IRON_REWARDS = Arrays.asList(
            "minecraft:iron_sword, 0.2, 1, 1, uncommon, unbreaking:1",
            "minecraft:iron_ingot, 0.8, 2, 5, common"
    );

    public static final List<String> GOLD_REWARDS = Arrays.asList(
            "minecraft:golden_shovel, 0.2, 1, 1, rare, unbreaking:1, efficiency:1",
            "minecraft:gold_ingot, 0.8, 2, 4, common"
    );

    public static final List<String> DIAMOND_REWARDS = Arrays.asList(
            "minecraft:diamond_pickaxe, 0.1, 1, 1, epic",
            "mintboxes:key_netherite, 0.01, 1, 1, legendary"
    );

    public static final List<String> NETHERITE_REWARDS = Arrays.asList(
            "minecraft:netherite_ingot, 0.5, 1, 1, legendary",
            "minecraft:nether_star, 0.5, 1, 1, legendary"
    );

    public static final List<String> SPECIAL_REWARDS = Arrays.asList(
            "mintboxes:key_stone, 1.0, 9, 9, legendary",
            "mintboxes:key_iron, 1.0, 9, 9, legendary",
            "mintboxes:key_gold, 1.0, 9, 9, legendary",
            "mintboxes:key_diamond, 1.0, 9, 9, legendary",
            "mintboxes:key_netherite, 1.0, 9, 9, legendary"
    );

    // ---------------------------
    // Default Roll Counts Per Tier
    // ---------------------------
    public static final Map<String, Integer> DEFAULT_ROLL_MIN = new HashMap<>();
    public static final Map<String, Integer> DEFAULT_ROLL_MAX = new HashMap<>();
    static {
        DEFAULT_ROLL_MIN.put("stone", 2);
        DEFAULT_ROLL_MAX.put("stone", 5);

        DEFAULT_ROLL_MIN.put("iron", 4);
        DEFAULT_ROLL_MAX.put("iron", 6);

        DEFAULT_ROLL_MIN.put("gold", 5);
        DEFAULT_ROLL_MAX.put("gold", 7);

        DEFAULT_ROLL_MIN.put("diamond", 6);
        DEFAULT_ROLL_MAX.put("diamond", 8);

        DEFAULT_ROLL_MIN.put("netherite", 7);
        DEFAULT_ROLL_MAX.put("netherite", 9);

        DEFAULT_ROLL_MIN.put("special", 1);
        DEFAULT_ROLL_MAX.put("special", 1);
    }

    public static List<String> getDefaultRewards(String tier) {
        return switch (tier) {
            case "stone" -> STONE_REWARDS;
            case "iron" -> IRON_REWARDS;
            case "gold" -> GOLD_REWARDS;
            case "diamond" -> DIAMOND_REWARDS;
            case "netherite" -> NETHERITE_REWARDS;
            case "special" -> SPECIAL_REWARDS;
            default -> List.of();
        };
    }
}
