package com.mint.mintboxes;

import com.mint.mintboxes.config.ModConfigs;
import com.mint.mintboxes.registry.ModRegistry;
import com.mint.mintboxes.loot.RewardTables;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MintBoxes.MODID)
public class MintBoxes {
    public static final String MODID = "mintboxes";
    public static final Logger LOGGER = LoggerFactory.getLogger("MintBoxes");

    public MintBoxes() {
        // Load our simple configs right away
        ModConfigs.loadAll();

        // Registry
        IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        ModRegistry.BLOCKS.register(modBus);
        ModRegistry.ITEMS.register(modBus);
        ModRegistry.BLOCK_ENTITIES.register(modBus);
        ModRegistry.TABS.register(modBus);

        // Build parsed tables from loaded config
        RewardTables.rebuildFromConfig();
    }

    public static void LOG(String msg) {
        LOGGER.info("[MintBoxes] {}", msg);
    }
}
