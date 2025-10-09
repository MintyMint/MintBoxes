package com.mint.mintboxes.config;

import com.mint.mintboxes.MintBoxes;
import com.mint.mintboxes.loot.RewardTables;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

public final class ModConfigs {
    private ModConfigs() {}

    public static void loadAll() {
        Path cfg = FMLPaths.CONFIGDIR.get();

        KeyDropConfig.load(cfg);
        LootBoxConfig.load(cfg);
        LootTableConfig.load(cfg);
        KeysConfig.load();
        DefaultConfig.register();

        RewardTables.rebuildFromConfig();

        MintBoxes.LOG("All MintBoxes configs loaded.");
    }
}
