package com.mint.mintboxes.config;

import com.mint.mintboxes.MintBoxes;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

/**
 * Single place to load all config files (our simple TOML-ish files).
 */
public final class ModConfigs {
    private ModConfigs() {}

    public static void loadAll() {
        Path cfg = FMLPaths.CONFIGDIR.get();

        KeyDropConfig.load(cfg);
        LootBoxConfig.load(cfg);
        LootTableConfig.load(cfg);

        MintBoxes.LOG("All MintBoxes configs loaded.");
    }
}
