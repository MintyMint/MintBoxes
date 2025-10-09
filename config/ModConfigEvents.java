package com.mint.mintboxes.config;

import com.mint.mintboxes.MintBoxes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent; // ✅ correct package

@EventBusSubscriber(modid = MintBoxes.MODID, value = Dist.CLIENT)
public final class ModConfigEvents {
    private ModConfigEvents() {}

    @SubscribeEvent
    public static void onConfigLoad(final ModConfigEvent.Loading e) {
        if (MintBoxes.MODID.equals(e.getConfig().getModId())) {
            ConfigBaker.bake();
            MintBoxes.LOG("Configs loaded → baked values.");
        }
    }

    @SubscribeEvent
    public static void onConfigReload(final ModConfigEvent.Reloading e) {
        if (MintBoxes.MODID.equals(e.getConfig().getModId())) {
            ConfigBaker.bake();
            MintBoxes.LOG("Configs reloaded → re-baked values.");
        }
    }
}
