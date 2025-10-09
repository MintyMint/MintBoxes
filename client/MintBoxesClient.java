package com.mint.mintboxes.client;


import com.mint.mintboxes.MintBoxes;
import com.mint.mintboxes.registry.ModParticles;
import com.mint.mintboxes.registry.ModRegistry;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import com.mint.mintboxes.client.particle.RainbowParticle;

@EventBusSubscriber(modid = MintBoxes.MODID, value = Dist.CLIENT)
public class MintBoxesClient {

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.RAINBOW.get(), RainbowParticle.Provider::new);
        System.out.println("✅Registered particles successfully!");
    }


    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModRegistry.LOOT_BOX_BE_TYPE.get(), LootBoxRenderer::new);
        System.out.println("✅Registered renderer successfully!");
    }

    @SuppressWarnings("unused")
    public static void onClientSetup(net.neoforged.fml.event.lifecycle.FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BlockEntityRenderers.register(ModRegistry.LOOT_BOX_BE_TYPE.get(), LootBoxRenderer::new);
        });
    }
}