package com.l3tplay.crashclaimmap.hook;

import com.l3tplay.crashclaimmap.configuration.Config;
import com.l3tplay.crashclaimmap.task.Pl3xMapTask;
import net.pl3x.map.api.Key;
import net.pl3x.map.api.Pl3xMapProvider;
import net.pl3x.map.api.SimpleLayerProvider;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Pl3xMapHook {
    private final Map<UUID, Pl3xMapTask> provider = new HashMap<>();

    public Pl3xMapHook(Plugin plugin) {
        Pl3xMapProvider.get().mapWorlds().forEach(world -> {
            if (CrashClaimHook.isWorldEnabled(world.uuid())) {
                SimpleLayerProvider provider = SimpleLayerProvider
                        .builder(Config.CONTROL_LABEL)
                        .showControls(Config.CONTROL_SHOW)
                        .defaultHidden(Config.CONTROL_HIDE)
                        .build();
                world.layerRegistry().register(Key.of("crashclaim_" + world.uuid()), provider);
                Pl3xMapTask task = new Pl3xMapTask(world, provider);
                task.runTaskTimerAsynchronously(plugin, 0, 20L * Config.UPDATE_INTERVAL);
                this.provider.put(world.uuid(), task);
            }
        });
    }

    public void disable() {
        provider.values().forEach(Pl3xMapTask::disable);
        provider.clear();
    }
}
