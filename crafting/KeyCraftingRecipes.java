package com.mint.mintboxes.crafting;

import com.mint.mintboxes.MintBoxes;
import com.mint.mintboxes.config.ConfigValues;
import com.mint.mintboxes.registry.ModRegistry;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public final class KeyCraftingRecipes {

    private KeyCraftingRecipes() {}

    public static void registerAll(PackOutput output, RecipeOutput out) {
        // Stone -> Iron
        addUpgradeRecipe("stone_to_iron",
                ModRegistry.KEY_STONE.get(),
                ModRegistry.KEY_IRON.get(),
                ConfigValues.CRAFTING_UPGRADE_CATALYSTS.get("stone_to_iron"),
                out);

        // Iron -> Gold
        addUpgradeRecipe("iron_to_gold",
                ModRegistry.KEY_IRON.get(),
                ModRegistry.KEY_GOLD.get(),
                ConfigValues.CRAFTING_UPGRADE_CATALYSTS.get("iron_to_gold"),
                out);

        // Gold -> Diamond
        addUpgradeRecipe("gold_to_diamond",
                ModRegistry.KEY_GOLD.get(),
                ModRegistry.KEY_DIAMOND.get(),
                ConfigValues.CRAFTING_UPGRADE_CATALYSTS.get("gold_to_diamond"),
                out);

        // Note: Netherite key crafting intentionally disabled (config controlled).
    }

    private static void addUpgradeRecipe(String name, Item inputKey, Item outputKey, String catalystId, RecipeOutput out) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, outputKey)
                .define('K', inputKey)
                .define('C', BuiltInRegistries.ITEM.get(ResourceLocation.parse(catalystId)))
                .pattern("KKK")
                .pattern("KCK")
                .pattern("KKK")
                .unlockedBy("has_key", InventoryChangeTrigger.TriggerInstance.hasItems(inputKey))
                .save(out, ResourceLocation.parse(MintBoxes.MODID + ":upgrade_" + name));
    }
}
