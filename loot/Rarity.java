package com.mint.mintboxes.loot;

import com.mint.mintboxes.config.ConfigValues;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

/**
 * Defines rarity tiers and colors for rewards.
 */
public enum Rarity {
    COMMON(() -> ConfigValues.RARITY_COLORS.getOrDefault("common", "#FFFFFF")),
    UNCOMMON(() -> ConfigValues.RARITY_COLORS.getOrDefault("uncommon", "#55FF55")),
    RARE(() -> ConfigValues.RARITY_COLORS.getOrDefault("rare", "#5555FF")),
    EPIC(() -> ConfigValues.RARITY_COLORS.getOrDefault("epic", "#AA00AA")),
    LEGENDARY(() -> ConfigValues.RARITY_COLORS.getOrDefault("legendary", "#FFAA00"));

    private final ColorSupplier colorSupplier;

    Rarity(ColorSupplier colorSupplier) {
        this.colorSupplier = colorSupplier;
    }

    public static Rarity fromString(String s) {
        try {
            return Rarity.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return COMMON;
        }
    }

    public MutableComponent colorize(Component base) {
        String hex = colorSupplier.get();
        TextColor color = TextColor.parseColor(hex).result().orElse(TextColor.fromRgb(0xFFFFFF));
        return base.copy().setStyle(Style.EMPTY.withColor(color));
    }

    @FunctionalInterface
    private interface ColorSupplier {
        String get();
    }
}
