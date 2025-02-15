package com.hastegames.asteroidrandomizer;

import com.hastegames.asteroidrandomizer.model.AsteroidRegion;
import com.hastegames.commons.util.EasyLog;
import com.hastegames.commons.util.Randomize;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class AsteroidTask extends BukkitRunnable {

    private final AsteroidPlugin plugin;
    private final List<AsteroidRegion> regions;
    private final World world;
    private long nextAttempt = 0;

    @Override
    public void run() {
        if (regions.isEmpty()) {
            return;
        }

        if (nextAttempt > System.currentTimeMillis()) {
            return;
        }

        AsteroidRegion region = regions.getFirst();
        int randomX = Randomize.randomInteger(region.getMinX(), region.getMaxX());
        int randomZ = Randomize.randomInteger(region.getMinZ(), region.getMaxZ());
        int randomY = Randomize.randomInteger(plugin.getSettings().settings__region__min_y, plugin.getSettings().settings__region__max_y);
        Location location = new Location(world, randomX, randomY, randomZ);
        Block block = world.getBlockAt(location);

        if (block.getType().isAir()) {
            // paste

            this.regions.remove(region);
            nextAttempt = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(plugin.getSettings().settings__paste__delay_seconds);
        } else {
            EasyLog.toConsole(getClass(), "Cannot paste at " + randomX + ", " + randomY + ", " + randomZ + " as it was not air.");
        }
    }

}
