package com.mint.mintboxes.stats;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.level.ServerPlayer;

public class StatsManager {
    private static final Map<UUID, StatsManager> BY_PLAYER = new ConcurrentHashMap<>();

    public static StatsManager get(ServerPlayer player) {
        return BY_PLAYER.computeIfAbsent(player.getUUID(), id -> new StatsManager());
    }

    // Dummy counters for now
    private long boxesOpened;
    private long keysUsed;

    public void onBoxOpened() { boxesOpened++; }
    public void onKeyUsed()   { keysUsed++; }

    public String formatStatsColored() {
        return "§bBoxes opened§r: " + boxesOpened + "   §bKeys used§r: " + keysUsed;
    }

    public String formatVerboseStatsColored() {
        return """
               §b== MintBoxes Stats ==§r
               §7Boxes opened§r: %d
               §7Keys used§r:   %d
               """.formatted(boxesOpened, keysUsed);
    }
}
