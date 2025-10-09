package com.mint.mintboxes.config;

import java.awt.*;

public final class ConfigBaker {
    private ConfigBaker() {}

    public static void bake() {
        // Enchant upgrades
        ConfigValues.ALLOW_ENCHANT_UPGRADES = KeysConfig.enchantUpgradesEnabled.get();
        ConfigValues.ENCHANT_UPGRADE_BASE_CHANCE = KeysConfig.enchantBaseChance.get();
        ConfigValues.IRON_UPGRADE_CHANCE = KeysConfig.ironUpgradeChance.get();
        ConfigValues.GOLD_UPGRADE_CHANCE = KeysConfig.goldUpgradeChance.get();
        ConfigValues.DIAMOND_UPGRADE_CHANCE = KeysConfig.diamondUpgradeChance.get();

        ConfigValues.ALLOW_SHIMMERING = KeysConfig.shimmeringEnabled.get();
        ConfigValues.SHIMMERING_MAX_LEVEL = KeysConfig.shimmeringMaxLevel.get();

        ConfigValues.ENABLE_CRAFTING_UPGRADES = KeysConfig.enableCraftingUpgrades.get();
        ConfigValues.ENABLE_NETHERITE_KEY_CRAFTING = KeysConfig.enableNetheriteKeyCrafting.get();

        ConfigValues.CRAFTING_UPGRADE_CATALYSTS.put("stone_to_iron", KeysConfig.stoneToIronCatalyst.get());
        ConfigValues.CRAFTING_UPGRADE_CATALYSTS.put("iron_to_gold", KeysConfig.ironToGoldCatalyst.get());
        ConfigValues.CRAFTING_UPGRADE_CATALYSTS.put("gold_to_diamond", KeysConfig.goldToDiamondCatalyst.get());
        ConfigValues.CRAFTING_UPGRADE_CATALYSTS.put("diamond_to_netherite", KeysConfig.diamondToNetheriteCatalyst.get());

        // Tints
        bakeTints();
    }

    public static void bakeTints() {
        ConfigValues.LOOTBOX_TINTS.clear();

        for (var entry : ConfigValues.LOOTBOX_TINTS.entrySet()) {
            String tier = entry.getKey();
            int fallback = entry.getValue();

            try {
                String hex = DefaultConfig.LOOTBOX_TINTS.get(tier).get();
                if (hex != null && hex.startsWith("#")) {
                    ConfigValues.LOOTBOX_TINTS.put(tier, (int) Long.parseLong(hex.substring(1), 16));
                } else {
                    ConfigValues.LOOTBOX_TINTS.put(tier, fallback);
                }
            } catch (Exception e) {
                ConfigValues.LOOTBOX_TINTS.put(tier, fallback);
            }
        }
    }
}

