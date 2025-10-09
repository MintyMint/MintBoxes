package com.mint.mintboxes.crafting;

import com.mint.mintboxes.MintBoxes;
import com.mint.mintboxes.config.ConfigValues;
import com.mint.mintboxes.registry.ModRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map;
import java.util.Optional;

@EventBusSubscriber(modid = MintBoxes.MODID)
public class KeyCraftingRecipes {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        RecipeManager manager = server.getRecipeManager();

        if (!ConfigValues.ENABLE_CRAFTING_UPGRADES) {
            MintBoxes.LOG("Key crafting disabled by config");
            return;
        }

        for (Map.Entry<String, String> e : ConfigValues.CRAFTING_UPGRADE_CATALYSTS.entrySet()) {
            String upgrade = e.getKey();
            String catalystId = e.getValue();

            Item input = getInput(upgrade);
            Item output = getOutput(upgrade);
            Item catalyst = BuiltInRegistries.ITEM.get(ResourceLocation.parse(catalystId));

            if (input == null || output == null || catalyst == null) continue;

            NonNullList<Ingredient> inputs = NonNullList.withSize(9, Ingredient.EMPTY);
            for (int i = 0; i < 9; i++) inputs.set(i, Ingredient.of(input));
            inputs.set(4, Ingredient.of(catalyst)); // middle slot

            ShapedRecipePattern pattern = new ShapedRecipePattern(
                    3, 3, inputs, Optional.empty()
            );

            ShapedRecipe recipe = new ShapedRecipe(
                    "mintboxes", CraftingBookCategory.MISC,
                    pattern, output.getDefaultInstance()
            );

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MintBoxes.MODID, "upgrade_" + upgrade);
            RecipeHolder<ShapedRecipe> holder = new RecipeHolder<>(id, recipe);

            // register into the manager
            //manager.recipes.add(holder);
        }

        MintBoxes.LOG("Key upgrade recipes injected.");
    }

    private static Item getInput(String upgrade) {
        return switch (upgrade) {
            case "stone_to_iron" -> ModRegistry.KEY_STONE.get();
            case "iron_to_gold" -> ModRegistry.KEY_IRON.get();
            case "gold_to_diamond" -> ModRegistry.KEY_GOLD.get();
            case "diamond_to_netherite" -> ConfigValues.ENABLE_NETHERITE_KEY_CRAFTING ? ModRegistry.KEY_DIAMOND.get() : null;
            default -> null;
        };
    }

    private static Item getOutput(String upgrade) {
        return switch (upgrade) {
            case "stone_to_iron" -> ModRegistry.KEY_IRON.get();
            case "iron_to_gold" -> ModRegistry.KEY_GOLD.get();
            case "gold_to_diamond" -> ModRegistry.KEY_DIAMOND.get();
            case "diamond_to_netherite" -> ConfigValues.ENABLE_NETHERITE_KEY_CRAFTING ? ModRegistry.KEY_NETHERITE.get() : null;
            default -> null;
        };
    }
}
