package com.l3tplay.crashclaimmap.task;

import com.l3tplay.crashclaimmap.configuration.Config;
import com.l3tplay.crashclaimmap.hook.CrashClaimHook;
import net.crashcraft.crashclaim.claimobjects.Claim;
import net.pl3x.map.api.Key;
import net.pl3x.map.api.MapWorld;
import net.pl3x.map.api.Point;
import net.pl3x.map.api.SimpleLayerProvider;
import net.pl3x.map.api.marker.Marker;
import net.pl3x.map.api.marker.MarkerOptions;
import net.pl3x.map.api.marker.Rectangle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.UUID;

public class Pl3xMapTask extends BukkitRunnable {
    private final MapWorld world;
    private final SimpleLayerProvider provider;

    private boolean stop;

    public Pl3xMapTask(MapWorld world, SimpleLayerProvider provider) {
        this.world = world;
        this.provider = provider;
    }

    @Override
    public void run() {
        if (stop) {
            cancel();
        }
        updateClaims();
    }

    void updateClaims() {
        provider.clearMarkers(); // TODO track markers instead of clearing them
        Collection<Claim> topLevelClaims = CrashClaimHook.getClaims();
        if (topLevelClaims != null) {
            topLevelClaims.stream()
                    .filter(claim -> claim.getWorld().equals(this.world.uuid()))
                    .forEach(this::handleClaim);
        }
    }

    private void handleClaim(Claim claim) {
        World world = Bukkit.getWorld(claim.getWorld());

        Location min = new Location(world, claim.getMinX(), world.getMinHeight(), claim.getMinZ());
        Location max = new Location(world, claim.getMaxX(), world.getMaxHeight(), claim.getMaxZ());

        Rectangle rect = Marker.rectangle(Point.of(min.getBlockX(), min.getBlockZ()), Point.of(max.getBlockX() + 1, max.getBlockZ() + 1));

        MarkerOptions.Builder options = MarkerOptions.builder()
                .strokeColor(Config.STROKE_COLOR)
                .strokeWeight(Config.STROKE_WEIGHT)
                .strokeOpacity(Config.STROKE_OPACITY)
                .fillColor(Config.FILL_COLOR)
                .fillOpacity(Config.FILL_OPACITY)
                .clickTooltip(Config.CLAIM_TOOLTIP
                        .replace("{world}", world.getName())
                        .replace("{id}", Long.toString(claim.getId()))
                        .replace("{owner}", getPlayerName(claim.getOwner()))
                        .replace("{name}", claim.getName())
                        .replace("{subclaims_amount}", Integer.toString(CrashClaimHook.getSubclaimsSize(claim)))
                        .replace("{subclaims}", CrashClaimHook.getSubclaims(claim))
                        .replace("{entrymessage}", CrashClaimHook.getEntryMessage(claim))
                        .replace("{exitmessage}", CrashClaimHook.getExitMessage(claim)));

        rect.markerOptions(options);

        String markerId = "crashclaim_" + world.getName() + "_region_" + Long.toHexString(claim.getId());
        this.provider.addMarker(Key.of(markerId), rect);
    }

    private static String getPlayerName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }

    public void disable() {
        cancel();
        this.stop = true;
        this.provider.clearMarkers();
    }
}
