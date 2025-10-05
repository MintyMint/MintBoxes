package com.mint.mintboxes.config;

import com.mint.mintboxes.MintBoxes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public final class LootBoxConfig {
    private LootBoxConfig() {}

    public static final String FILE_NAME = "mintboxes-lootbox.toml";

    public static void load(Path configDir) {
        try {
            Path dir = configDir.resolve("mintboxes");
            Files.createDirectories(dir);
            Path file = dir.resolve(FILE_NAME);

            if (!Files.exists(file)) {
                writeDefaults(file);
            }

            parse(file);

            MintBoxes.LOG("Loaded lootbox timings + roll ranges.");
        } catch (Exception e) {
            MintBoxes.LOG("Failed to load lootbox config; using defaults. " + e);
            // fallback to defaults
            ConfigValues.OPEN_TICKS = DefaultValues.DEFAULT_OPEN_TICKS;
            ConfigValues.REWARD_DELAY_TICKS = DefaultValues.DEFAULT_REWARD_DELAY_TICKS;
            ConfigValues.CLOSE_TICKS = DefaultValues.DEFAULT_CLOSE_TICKS;
            ConfigValues.LOCKOUT_TICKS = DefaultValues.DEFAULT_LOCKOUT_TICKS;

            ConfigValues.ROLL_MIN.clear();
            ConfigValues.ROLL_MIN.putAll(DefaultValues.DEFAULT_ROLL_MIN);

            ConfigValues.ROLL_MAX.clear();
            ConfigValues.ROLL_MAX.putAll(DefaultValues.DEFAULT_ROLL_MAX);
        }
    }

    private static void writeDefaults(Path file) throws IOException {
        List<String> out = new ArrayList<>();
        out.add("# MintBoxes lootbox timings and per-tier roll counts (TOML-ish)");
        out.add("open_ticks=" + DefaultValues.DEFAULT_OPEN_TICKS);
        out.add("reward_delay_ticks=" + DefaultValues.DEFAULT_REWARD_DELAY_TICKS);
        out.add("close_ticks=" + DefaultValues.DEFAULT_CLOSE_TICKS);
        out.add("lockout_ticks=" + DefaultValues.DEFAULT_LOCKOUT_TICKS);
        out.add("");

        out.add("[rolls]");
        for (String tier : ConfigValues.KNOWN_TIERS) {
            int min = DefaultValues.DEFAULT_ROLL_MIN.getOrDefault(tier, 1);
            int max = DefaultValues.DEFAULT_ROLL_MAX.getOrDefault(tier, 1);
            out.add(tier + "_min=" + min);
            out.add(tier + "_max=" + max);
        }

        Files.write(file, out, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        MintBoxes.LOG("Wrote default lootbox config: " + file);
    }

    private static void parse(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

        boolean inRolls = false;
        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            if (line.startsWith("[") && line.endsWith("]")) {
                inRolls = "rolls".equalsIgnoreCase(line.substring(1, line.length() - 1).trim());
                continue;
            }

            int eq = line.indexOf('=');
            if (eq <= 0) continue;

            String key = line.substring(0, eq).trim();
            String val = line.substring(eq + 1).trim();

            if (!inRolls) {
                switch (key) {
                    case "open_ticks" -> ConfigValues.OPEN_TICKS = parseInt(val, DefaultValues.DEFAULT_OPEN_TICKS);
                    case "reward_delay_ticks" -> ConfigValues.REWARD_DELAY_TICKS = parseInt(val, DefaultValues.DEFAULT_REWARD_DELAY_TICKS);
                    case "close_ticks" -> ConfigValues.CLOSE_TICKS = parseInt(val, DefaultValues.DEFAULT_CLOSE_TICKS);
                    case "lockout_ticks" -> ConfigValues.LOCKOUT_TICKS = parseInt(val, DefaultValues.DEFAULT_LOCKOUT_TICKS);
                }
            } else {
                // rolls section
                for (String tier : ConfigValues.KNOWN_TIERS) {
                    if (key.equalsIgnoreCase(tier + "_min")) {
                        ConfigValues.ROLL_MIN.put(tier, parseInt(val, DefaultValues.DEFAULT_ROLL_MIN.getOrDefault(tier, 1)));
                    } else if (key.equalsIgnoreCase(tier + "_max")) {
                        ConfigValues.ROLL_MAX.put(tier, parseInt(val, DefaultValues.DEFAULT_ROLL_MAX.getOrDefault(tier, 1)));
                    }
                }
            }
        }
    }

    private static int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}
