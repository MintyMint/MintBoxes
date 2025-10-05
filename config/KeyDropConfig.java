package com.mint.mintboxes.config;

import com.mint.mintboxes.MintBoxes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads/writes player-action key drop chances + upgrade chances (TOML-ish).
 *
 * File: config/mintboxes/mintboxes-keydrops.toml
 */
public final class KeyDropConfig {
    private KeyDropConfig() {}

    public static final String FILE_NAME = "mintboxes-keydrops.toml";

    public static void load(Path configDir) {
        try {
            Path dir = configDir.resolve("mintboxes");
            Files.createDirectories(dir);
            Path file = dir.resolve(FILE_NAME);

            if (!Files.exists(file)) {
                writeDefaults(file);
            }
            parse(file);

            MintBoxes.LOG("Loaded key drop config.");
        } catch (Exception e) {
            MintBoxes.LOG("Failed to load key drop config; using defaults. " + e);
            // fallback
            ConfigValues.MOB_KILL_CHANCE = DefaultValues.DEFAULT_MOB_KILL_CHANCE;
            ConfigValues.ORE_BREAK_CHANCE = DefaultValues.DEFAULT_ORE_BREAK_CHANCE;
            ConfigValues.FISHING_CHANCE = DefaultValues.DEFAULT_FISHING_CHANCE;
            ConfigValues.CROP_HARVEST_CHANCE = DefaultValues.DEFAULT_CROP_HARVEST_CHANCE;

            ConfigValues.ALLOW_ENCHANT_UPGRADES = DefaultValues.DEFAULT_ALLOW_ENCHANT_UPGRADES;
            ConfigValues.ENCHANT_UPGRADE_BASE_CHANCE = DefaultValues.DEFAULT_UPGRADE_CHANCE;
            ConfigValues.IRON_UPGRADE_CHANCE = DefaultValues.DEFAULT_IRON_UPGRADE_CHANCE;
            ConfigValues.GOLD_UPGRADE_CHANCE = DefaultValues.DEFAULT_GOLD_UPGRADE_CHANCE;
            ConfigValues.DIAMOND_UPGRADE_CHANCE = DefaultValues.DEFAULT_DIAMOND_UPGRADE_CHANCE;
        }
    }

    private static void writeDefaults(Path file) throws IOException {
        List<String> out = new ArrayList<>();
        out.add("# MintBoxes key drop chances & upgrade rules (TOML-ish)");
        out.add("mob_kill_chance=" + DefaultValues.DEFAULT_MOB_KILL_CHANCE);
        out.add("ore_break_chance=" + DefaultValues.DEFAULT_ORE_BREAK_CHANCE);
        out.add("fishing_chance=" + DefaultValues.DEFAULT_FISHING_CHANCE);
        out.add("crop_harvest_chance=" + DefaultValues.DEFAULT_CROP_HARVEST_CHANCE);
        out.add("");
        out.add("allow_enchant_upgrades=" + DefaultValues.DEFAULT_ALLOW_ENCHANT_UPGRADES);
        out.add("enchant_upgrade_base_chance=" + DefaultValues.DEFAULT_UPGRADE_CHANCE);
        out.add("iron_upgrade_chance=" + DefaultValues.DEFAULT_IRON_UPGRADE_CHANCE);
        out.add("gold_upgrade_chance=" + DefaultValues.DEFAULT_GOLD_UPGRADE_CHANCE);
        out.add("diamond_upgrade_chance=" + DefaultValues.DEFAULT_DIAMOND_UPGRADE_CHANCE);

        Files.write(file, out, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        MintBoxes.LOG("Wrote default key drop config: " + file);
    }

    private static void parse(Path file) throws Exception {
        for (String raw : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            int eq = line.indexOf('=');
            if (eq <= 0) continue;

            String key = line.substring(0, eq).trim();
            String val = line.substring(eq + 1).trim();

            switch (key) {
                case "mob_kill_chance" -> ConfigValues.MOB_KILL_CHANCE = parseDouble(val, DefaultValues.DEFAULT_MOB_KILL_CHANCE);
                case "ore_break_chance" -> ConfigValues.ORE_BREAK_CHANCE = parseDouble(val, DefaultValues.DEFAULT_ORE_BREAK_CHANCE);
                case "fishing_chance" -> ConfigValues.FISHING_CHANCE = parseDouble(val, DefaultValues.DEFAULT_FISHING_CHANCE);
                case "crop_harvest_chance" -> ConfigValues.CROP_HARVEST_CHANCE = parseDouble(val, DefaultValues.DEFAULT_CROP_HARVEST_CHANCE);

                case "allow_enchant_upgrades" -> ConfigValues.ALLOW_ENCHANT_UPGRADES = parseBoolean(val, DefaultValues.DEFAULT_ALLOW_ENCHANT_UPGRADES);
                case "enchant_upgrade_base_chance" -> ConfigValues.ENCHANT_UPGRADE_BASE_CHANCE = parseDouble(val, DefaultValues.DEFAULT_UPGRADE_CHANCE);
                case "iron_upgrade_chance" -> ConfigValues.IRON_UPGRADE_CHANCE = parseDouble(val, DefaultValues.DEFAULT_IRON_UPGRADE_CHANCE);
                case "gold_upgrade_chance" -> ConfigValues.GOLD_UPGRADE_CHANCE = parseDouble(val, DefaultValues.DEFAULT_GOLD_UPGRADE_CHANCE);
                case "diamond_upgrade_chance" -> ConfigValues.DIAMOND_UPGRADE_CHANCE = parseDouble(val, DefaultValues.DEFAULT_DIAMOND_UPGRADE_CHANCE);
            }
        }
    }

    private static double parseDouble(String s, double def) {
        try { return Double.parseDouble(s); } catch (Exception e) { return def; }
    }
    private static boolean parseBoolean(String s, boolean def) {
        try { return Boolean.parseBoolean(s); } catch (Exception e) { return def; }
    }
}
