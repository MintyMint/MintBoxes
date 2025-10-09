package com.mint.mintboxes.config;

import com.mint.mintboxes.MintBoxes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public final class LootTableConfig {

    private LootTableConfig() {}

    public static final String FILE_NAME = "mintboxes-loottables.toml";

    public static void load(Path configDir) {
        try {
            Path dir = configDir.resolve("mintboxes");
            Files.createDirectories(dir);
            Path file = dir.resolve(FILE_NAME);

            if (!Files.exists(file)) {
                writeDefaults(file);
            }

            Map<String, String> rarityColors = new LinkedHashMap<>();
            Map<String, List<String>> tables = new LinkedHashMap<>();

            parseFile(file, rarityColors, tables);

            // Push into live config values
            ConfigValues.RARITY_COLORS.clear();
            ConfigValues.RARITY_COLORS.putAll(rarityColors);

            ConfigValues.REWARD_TABLES.clear();
            ConfigValues.REWARD_TABLES.putAll(tables);

            MintBoxes.LOG("Loaded loot table config with " + tables.size() + " tiers.");
        } catch (Exception e) {
            MintBoxes.LOG("Failed to load loottables; using defaults. " + e);
            // fallback to defaults
            ConfigValues.RARITY_COLORS.clear();
            ConfigValues.RARITY_COLORS.putAll(DefaultValues.DEFAULT_RARITY_COLORS);

            ConfigValues.REWARD_TABLES.clear();
            ConfigValues.REWARD_TABLES.put("stone", new ArrayList<>(DefaultValues.STONE_REWARDS));
            ConfigValues.REWARD_TABLES.put("iron", new ArrayList<>(DefaultValues.IRON_REWARDS));
            ConfigValues.REWARD_TABLES.put("gold", new ArrayList<>(DefaultValues.GOLD_REWARDS));
            ConfigValues.REWARD_TABLES.put("diamond", new ArrayList<>(DefaultValues.DIAMOND_REWARDS));
            ConfigValues.REWARD_TABLES.put("netherite", new ArrayList<>(DefaultValues.NETHERITE_REWARDS));
            ConfigValues.REWARD_TABLES.put("special", new ArrayList<>(DefaultValues.SPECIAL_REWARDS));
        }
    }

    private static void writeDefaults(Path file) throws IOException {
        List<String> out = new ArrayList<>();
        out.add("# MintBoxes loot tables (TOML-ish).");
        out.add("# Each entry: \"namespace:item, chance, min, max, rarity[, enchant:level ...]\"");
        out.add("");

        // rarities
        out.add("[rarities]");
        DefaultValues.DEFAULT_RARITY_COLORS.forEach((k, v) -> out.add(k + " = \"" + v + "\""));
        out.add("");

        // helper to write a tier block
        java.util.function.BiConsumer<String, List<String>> writeTier = (tier, entries) -> {
            out.add("[tier." + tier + "]");
            out.add("rewards = [");
            for (int i = 0; i < entries.size(); i++) {
                String e = entries.get(i).trim();
                out.add("  \"" + e + "\"" + (i + 1 < entries.size() ? "," : ""));
            }
            out.add("]");
            out.add("");
        };

        writeTier.accept("stone", DefaultValues.STONE_REWARDS);
        writeTier.accept("iron", DefaultValues.IRON_REWARDS);
        writeTier.accept("gold", DefaultValues.GOLD_REWARDS);
        writeTier.accept("diamond", DefaultValues.DIAMOND_REWARDS);
        writeTier.accept("netherite", DefaultValues.NETHERITE_REWARDS);
        writeTier.accept("special", DefaultValues.SPECIAL_REWARDS);

        Files.write(file, out, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        MintBoxes.LOG("Wrote default loottables: " + file);
    }

    private static void parseFile(Path file,
                                  Map<String, String> rarityColorsOut,
                                  Map<String, List<String>> tablesOut) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

        String section = "";
        String currentTier = null;
        boolean readingArray = false;
        List<String> currentArray = new ArrayList<>();

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            if (line.startsWith("[") && line.endsWith("]")) {
                // flush any pending array to the tier map
                if (readingArray && currentTier != null) {
                    tablesOut.put(currentTier, new ArrayList<>(currentArray));
                    currentArray.clear();
                    readingArray = false;
                }

                section = line.substring(1, line.length() - 1).trim(); // e.g. "rarities" or "tier.stone"
                if (section.startsWith("tier.")) {
                    currentTier = section.substring("tier.".length()).trim();
                } else {
                    currentTier = null;
                }
                continue;
            }

            if ("rarities".equals(section)) {
                // key = "value"
                int eq = line.indexOf('=');
                if (eq > 0) {
                    String key = line.substring(0, eq).trim();
                    String val = line.substring(eq + 1).trim();
                    val = unquote(val);
                    rarityColorsOut.put(key, val);
                }
                continue;
            }

            if (section.startsWith("tier.")) {
                if (line.startsWith("rewards")) {
                    // rewards = [
                    readingArray = line.contains("[");
                    if (readingArray && line.endsWith("]")) {
                        // single line, empty array
                        readingArray = false;
                        tablesOut.put(currentTier, new ArrayList<>());
                    }
                    continue;
                }

                if (readingArray) {
                    if (line.startsWith("]")) {
                        // end of array
                        tablesOut.put(currentTier, new ArrayList<>(currentArray));
                        currentArray.clear();
                        readingArray = false;
                    } else {
                        // "value",
                        String v = line;
                        // strip trailing comma
                        if (v.endsWith(",")) v = v.substring(0, v.length() - 1).trim();
                        v = unquote(v);
                        if (!v.isEmpty()) currentArray.add(v);
                    }
                }
            }
        }

        // flush if file ended while reading
        if (readingArray && currentTier != null) {
            tablesOut.put(currentTier, new ArrayList<>(currentArray));
        }
    }

    private static String unquote(String s) {
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }
}
