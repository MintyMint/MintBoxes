package com.mint.mintboxes;

import com.mint.mintboxes.client.MintBoxesClient;
import com.mint.mintboxes.config.ModConfigs;
import com.mint.mintboxes.client.LootBoxRenderer;
import com.mint.mintboxes.registry.ModParticles;
import com.mint.mintboxes.registry.ModRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MintBoxes.MODID)
public class MintBoxes {
    public static final String MODID = "mintboxes";
    public static final Logger LOGGER = LoggerFactory.getLogger("MintBoxes");

    public MintBoxes() {
        ModConfigs.loadAll();

        IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();

        ModRegistry.BLOCKS.register(modBus);
        ModRegistry.ITEMS.register(modBus);
        ModRegistry.BLOCK_ENTITIES.register(modBus);
        ModRegistry.TABS.register(modBus);
        ModParticles.register(modBus);
        System.out.println("✅Registered resources successfully!");
    }

    public static void LOG(String msg) {
        LOGGER.info("[MintBoxes] {}", msg);
    }
}
