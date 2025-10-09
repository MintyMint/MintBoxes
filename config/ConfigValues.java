package com.mint.mintboxes.config;

import org.lwjgl.system.Pointer;

import java.util.*;

/**
 * Live, baked values other game code reads from.
 */
public final class ConfigValues {
    private ConfigValues() {}

    // Tier order (used in config generation too)
    public static final List<String> KNOWN_TIERS = List.of("stone","iron","gold","diamond","netherite","special");

    public static boolean ENABLE_CRAFTING_UPGRADES = DefaultValues.DEFAULT_ENABLE_CRAFTING_UPGRADES;

    public static boolean ENABLE_NETHERITE_KEY_CRAFTING = DefaultValues.DEFAULT_ENABLE_NETHERITE_KEY_CRAFTING;

    public static Map<String, String> CRAFTING_UPGRADE_CATALYSTS = new HashMap<>(DefaultValues.DEFAULT_CRAFTING_UPGRADE_CATALYSTS);

    // ---- Timings ----
    public static int OPEN_TICKS = DefaultValues.DEFAULT_OPEN_TICKS;
    public static int REWARD_DELAY_TICKS = DefaultValues.DEFAULT_REWARD_DELAY_TICKS;
    public static int CLOSE_TICKS = DefaultValues.DEFAULT_CLOSE_TICKS;
    public static int LOCKOUT_TICKS = DefaultValues.DEFAULT_LOCKOUT_TICKS;

    // ---- Per-tier roll ranges ----
    public static final Map<String, Integer> ROLL_MIN = new HashMap<>(DefaultValues.DEFAULT_ROLL_MIN);
    public static final Map<String, Integer> ROLL_MAX = new HashMap<>(DefaultValues.DEFAULT_ROLL_MAX);

    // ---- Key drop chances ----
    public static double MOB_KILL_CHANCE = DefaultValues.DEFAULT_MOB_KILL_CHANCE;
    public static double ORE_BREAK_CHANCE = DefaultValues.DEFAULT_ORE_BREAK_CHANCE;
    public static double FISHING_CHANCE = DefaultValues.DEFAULT_FISHING_CHANCE;
    public static double CROP_HARVEST_CHANCE = DefaultValues.DEFAULT_CROP_HARVEST_CHANCE;

    // ---- Enchant-based upgrades ----
    public static boolean ALLOW_ENCHANT_UPGRADES = DefaultValues.DEFAULT_ALLOW_ENCHANT_UPGRADES;
    public static double ENCHANT_UPGRADE_BASE_CHANCE = DefaultValues.DEFAULT_UPGRADE_CHANCE;
    public static double IRON_UPGRADE_CHANCE = DefaultValues.DEFAULT_IRON_UPGRADE_CHANCE;
    public static double GOLD_UPGRADE_CHANCE = DefaultValues.DEFAULT_GOLD_UPGRADE_CHANCE;
    public static double DIAMOND_UPGRADE_CHANCE = DefaultValues.DEFAULT_DIAMOND_UPGRADE_CHANCE;

    // ---- Rarity colors (#RRGGBB) ----
    public static final Map<String, String> RARITY_COLORS = new HashMap<>(DefaultValues.DEFAULT_RARITY_COLORS);

    // ---- Raw reward table strings per tier ----
    public static final Map<String, List<String>> REWARD_TABLES = new HashMap<>();
    static {
        REWARD_TABLES.put("stone", new ArrayList<>(DefaultValues.STONE_REWARDS));
        REWARD_TABLES.put("iron", new ArrayList<>(DefaultValues.IRON_REWARDS));
        REWARD_TABLES.put("gold", new ArrayList<>(DefaultValues.GOLD_REWARDS));
        REWARD_TABLES.put("diamond", new ArrayList<>(DefaultValues.DIAMOND_REWARDS));
        REWARD_TABLES.put("netherite", new ArrayList<>(DefaultValues.NETHERITE_REWARDS));
        REWARD_TABLES.put("special", new ArrayList<>(DefaultValues.SPECIAL_REWARDS));
    }

    // --- LootBox Reward Rolls ---
    public static Map<String, Integer> REWARD_MIN_ROLLS = new HashMap<>(DefaultValues.DEFAULT_ROLL_MIN);
    public static Map<String, Integer> REWARD_MAX_ROLLS = new HashMap<>(DefaultValues.DEFAULT_ROLL_MAX);

    // Shimmering settings
    public static boolean ALLOW_SHIMMERING = DefaultValues.DEFAULT_ALLOW_SHIMMERING;
    public static int SHIMMERING_MAX_LEVEL = DefaultValues.DEFAULT_SHIMMERING_MAX_LEVEL;
    public static List<Integer> SHIMMERING_LEVEL_COSTS = DefaultValues.DEFAULT_SHIMMERING_COSTS;

    //Tinting
    public static final Map<String, Integer> LOOTBOX_TINTS = new HashMap<>(DefaultValues.DEFAULT_LOOTBOX_TINTS);

}
