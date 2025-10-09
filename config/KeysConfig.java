package com.mint.mintboxes.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mint.mintboxes.MintBoxes;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;

public final class KeysConfig {
    private KeysConfig() {}

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static ModConfigSpec.BooleanValue enableCraftingUpgrades;
    public static ModConfigSpec.BooleanValue enableNetheriteKeyCrafting;
    public static ModConfigSpec.ConfigValue<String> stoneToIronCatalyst;
    public static ModConfigSpec.ConfigValue<String> ironToGoldCatalyst;
    public static ModConfigSpec.ConfigValue<String> goldToDiamondCatalyst;
    public static ModConfigSpec.ConfigValue<String> diamondToNetheriteCatalyst;

    public static ModConfigSpec.BooleanValue enchantUpgradesEnabled;
    public static ModConfigSpec.DoubleValue enchantBaseChance;
    public static ModConfigSpec.DoubleValue ironUpgradeChance;
    public static ModConfigSpec.DoubleValue goldUpgradeChance;
    public static ModConfigSpec.DoubleValue diamondUpgradeChance;

    public static ModConfigSpec.BooleanValue shimmeringEnabled;
    public static ModConfigSpec.IntValue shimmeringMaxLevel;

    static {
        BUILDER.push("Crafting Upgrades");

        enableCraftingUpgrades = BUILDER
                .comment("Enable or disable crafting upgrades for keys")
                .define("enableCraftingUpgrades", ConfigValues.ENABLE_CRAFTING_UPGRADES);

        enableNetheriteKeyCrafting = BUILDER
                .comment("Enable Diamond → Netherite key upgrade")
                .define("enableNetheriteKeyCrafting", ConfigValues.ENABLE_NETHERITE_KEY_CRAFTING);

        stoneToIronCatalyst = BUILDER
                .comment("Catalyst item for Stone → Iron key upgrade")
                .define("stoneToIronCatalyst", ConfigValues.CRAFTING_UPGRADE_CATALYSTS.get("stone_to_iron"));

        ironToGoldCatalyst = BUILDER
                .comment("Catalyst item for Iron → Gold key upgrade")
                .define("ironToGoldCatalyst", ConfigValues.CRAFTING_UPGRADE_CATALYSTS.get("iron_to_gold"));

        goldToDiamondCatalyst = BUILDER
                .comment("Catalyst item for Gold → Diamond key upgrade")
                .define("goldToDiamondCatalyst", ConfigValues.CRAFTING_UPGRADE_CATALYSTS.get("gold_to_diamond"));

        diamondToNetheriteCatalyst = BUILDER
                .comment("Catalyst item for Diamond → Netherite key upgrade")
                .define("diamondToNetheriteCatalyst", ConfigValues.CRAFTING_UPGRADE_CATALYSTS.get("diamond_to_netherite"));


        BUILDER.push("Enchant Upgrades");

        enchantUpgradesEnabled = BUILDER
                .comment("Allow enchant-based tier upgrades")
                .define("enchantUpgradesEnabled", DefaultValues.DEFAULT_ALLOW_ENCHANT_UPGRADES);

        enchantBaseChance = BUILDER
                .comment("Base chance for enchantment tier upgrades (%)")
                .defineInRange("enchantBaseChance", DefaultValues.DEFAULT_UPGRADE_CHANCE * 100.0, 0.0, 100.0);

        ironUpgradeChance = BUILDER
                .comment("Chance to upgrade to Iron (%)")
                .defineInRange("ironUpgradeChance", DefaultValues.DEFAULT_IRON_UPGRADE_CHANCE * 100.0, 0.0, 100.0);

        goldUpgradeChance = BUILDER
                .comment("Chance to upgrade to Gold (%)")
                .defineInRange("goldUpgradeChance", DefaultValues.DEFAULT_GOLD_UPGRADE_CHANCE * 100.0, 0.0, 100.0);

        diamondUpgradeChance = BUILDER
                .comment("Chance to upgrade to Diamond (%)")
                .defineInRange("diamondUpgradeChance", DefaultValues.DEFAULT_DIAMOND_UPGRADE_CHANCE * 100.0, 0.0, 100.0);

        BUILDER.pop();

        BUILDER.push("Shimmering");

        shimmeringEnabled = BUILDER
                .comment("Enable the Shimmering enchantment")
                .define("shimmeringEnabled", DefaultValues.DEFAULT_ALLOW_SHIMMERING);

        shimmeringMaxLevel = BUILDER
                .comment("Maximum level of Shimmering enchantment")
                .defineInRange("shimmeringMaxLevel", DefaultValues.DEFAULT_SHIMMERING_MAX_LEVEL, 1, 10);

        BUILDER.pop();

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static void load() {
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, SPEC, MintBoxes.MODID + "-keys.toml");
    }
}
