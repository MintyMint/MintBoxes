package com.mint.mintboxes.config;

import com.mint.mintboxes.MintBoxes;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;
import java.util.Map;

public final class DefaultConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static ModConfigSpec.IntValue preOpenDelayTicks;
    public static ModConfigSpec.IntValue openTicks;
    public static ModConfigSpec.IntValue rewardDelayTicks;
    public static ModConfigSpec.IntValue closeTicks;
    public static ModConfigSpec.IntValue cooldownTicks;

    public static ModConfigSpec.DoubleValue mobKeyChance;
    public static ModConfigSpec.DoubleValue oreKeyChance;
    public static ModConfigSpec.DoubleValue fishingKeyChance;
    public static ModConfigSpec.DoubleValue cropKeyChance;

    public static ModConfigSpec.BooleanValue enchantUpgradesEnabled;
    public static ModConfigSpec.DoubleValue enchantTriggerChance;
    public static ModConfigSpec.DoubleValue ironUpgradeChance;
    public static ModConfigSpec.DoubleValue goldUpgradeChance;
    public static ModConfigSpec.DoubleValue diamondUpgradeChance;

    public static final Map<String, ModConfigSpec.ConfigValue<String>> LOOTBOX_TINTS = new HashMap<>();

    static {
        BUILDER.push("LootBox Timings");
        preOpenDelayTicks = BUILDER.comment("Ticks before lid starts opening after key is accepted").defineInRange("preOpenDelayTicks", 30, 0, 200);
        openTicks = BUILDER.comment("Ticks box is visibly open before rewards").defineInRange("openTicks", 20, 0, 200);
        rewardDelayTicks = BUILDER.comment("Delay between opening and rewards").defineInRange("rewardDelayTicks", 10, 0, 200);
        closeTicks = BUILDER.comment("Ticks box remains open after rewards").defineInRange("closeTicks", 40, 0, 200);
        cooldownTicks = BUILDER.comment("Cooldown before box can be reopened").defineInRange("cooldownTicks", 60, 0, 200);
        BUILDER.pop();

        BUILDER.push("Key Drop Chances (%)");
        mobKeyChance = BUILDER.defineInRange("mobKeyChance", 5.0, 0.0, 100.0);
        oreKeyChance = BUILDER.defineInRange("oreKeyChance", 1.0, 0.0, 100.0);
        fishingKeyChance = BUILDER.defineInRange("fishingKeyChance", 0.5, 0.0, 100.0);
        cropKeyChance = BUILDER.defineInRange("cropKeyChance", 0.25, 0.0, 100.0);
        BUILDER.pop();

        BUILDER.push("Enchant Upgrades");
        enchantUpgradesEnabled = BUILDER.define("enchantUpgradesEnabled", true);
        enchantTriggerChance = BUILDER.defineInRange("enchantTriggerChance", 25.0, 0.0, 100.0);
        ironUpgradeChance = BUILDER.defineInRange("ironUpgradeChance", 50.0, 0.0, 100.0);
        goldUpgradeChance = BUILDER.defineInRange("goldUpgradeChance", 25.0, 0.0, 100.0);
        diamondUpgradeChance = BUILDER.defineInRange("diamondUpgradeChance", 10.0, 0.0, 100.0);
        BUILDER.pop();

        BUILDER.push("LootBoxTints");
        for (var entry : DefaultConfig.LOOTBOX_TINTS.entrySet()) {
            LOOTBOX_TINTS.put(entry.getKey(),
                    BUILDER.comment("ARGB hex tint for " + entry.getKey() + " lootbox")
                            .define(entry.getKey() + "Tint",
                                    String.format("#%08X", entry.getValue())));
        }
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static void register() {
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, SPEC, MintBoxes.MODID + "-misc.toml");
    }
}
