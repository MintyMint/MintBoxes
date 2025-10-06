package com.mint.mintboxes.loot;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single reward entry parsed from config.
 */
public class RewardDefinition {
    public final String itemId;
    public final double chance;
    public final int min;
    public final int max;
    public final Rarity rarity;
    public final Map<String, Integer> enchantments;

    public RewardDefinition(String itemId, double chance, int min, int max, Rarity rarity, Map<String, Integer> enchants) {
        this.itemId = itemId;
        this.chance = chance;
        this.min = min;
        this.max = max;
        this.rarity = rarity;
        this.enchantments = enchants;
    }

    /**
     * Parse a line like: "minecraft:apple, 0.5, 1, 3, common"
     */
    public static RewardDefinition parse(String line) {
        String[] parts = line.split(",");
        String item = parts[0].trim();
        double chance = Double.parseDouble(parts[1].trim());
        int min = Integer.parseInt(parts[2].trim());
        int max = Integer.parseInt(parts[3].trim());
        Rarity rarity = Rarity.valueOf(parts[4].trim().toUpperCase());

        Map<String, Integer> enchants = new HashMap<>();
        for (int i = 5; i < parts.length; i++) {
            String[] ench = parts[i].trim().split(":");
            if (ench.length == 2) enchants.put(ench[0], Integer.parseInt(ench[1]));
        }

        return new RewardDefinition(item, chance, min, max, rarity, enchants);
    }

    public void applyEnchantments(ItemStack stack, RegistryAccess registries) {
        var enchRegistry = registries.lookupOrThrow(Registries.ENCHANTMENT);
        for (var entry : enchantments.entrySet()) {
            String enchId = entry.getKey();
            int level = entry.getValue();

            ResourceLocation enchLoc = ResourceLocation.parse(enchId);
            var enchHolder = enchRegistry.get(ResourceKey.create(Registries.ENCHANTMENT, enchLoc));
            enchHolder.ifPresent(holder -> stack.enchant(holder, level));
        }
    }

    public Component getDisplayName(ItemStack stack) {
        return rarity.colorize(stack.getHoverName());
    }
}
