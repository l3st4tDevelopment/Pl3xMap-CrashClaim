package com.l3tplay.crashclaimmap;

import com.l3tplay.crashclaimmap.configuration.Config;
import com.l3tplay.crashclaimmap.hook.Pl3xMapHook;
import org.bukkit.plugin.java.JavaPlugin;

public class Pl3xMapCrashClaim extends JavaPlugin {
    private Pl3xMapHook pl3xmapHook;

    @Override
    public void onEnable() {
        Config.reload(this);
        pl3xmapHook = new Pl3xMapHook(this);
    }

    @Override
    public void onDisable() {
        if (pl3xmapHook != null) {
            pl3xmapHook.disable();
        }
    }
}
