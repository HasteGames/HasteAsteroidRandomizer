package com.hastegames.asteroidrandomizer;

import com.hastegames.asteroidrandomizer.model.AsteroidRegion;
import com.hastegames.commons.util.EasyLog;
import com.hastegames.commons.util.FormatUtil;
import com.hastegames.commons.util.Randomize;
import com.hastegames.commons.util.datetime.TimeUtil;
import com.hastegames.commons.util.string.CC;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AsteroidTask extends BukkitRunnable {

    private final AsteroidPlugin plugin;
    private final List<AsteroidRegion> regions;
    private final World world;

    private final long startedAt;
    private final int beginAmount;
    private long nextAttempt = 0;

    public AsteroidTask(AsteroidPlugin plugin, List<AsteroidRegion> regions, World world) {
        this.plugin = plugin;
        this.regions = regions;
        this.world = world;

        this.startedAt = System.currentTimeMillis();
        this.beginAmount = regions.size();
    }

    @Override
    public void run() {
        if (regions.isEmpty()) {
            cancel();
            return;
        }

        if (nextAttempt > System.currentTimeMillis()) {
            return;
        }

        AsteroidRegion region = regions.get(0);
        int randomX = Randomize.randomInteger(region.getMinX(), region.getMaxX());
        int randomZ = Randomize.randomInteger(region.getMinZ(), region.getMaxZ());
        int randomY = Randomize.randomInteger(plugin.getSettings().settings__region__min_y, plugin.getSettings().settings__region__max_y);
        Location location = new Location(world, randomX, randomY, randomZ);
        Block block = world.getBlockAt(location);

        if (block.getType().isAir()) {
            File file = Randomize.getRandom(this.plugin.settings.getSchematics());
            // do the actual paste!

            this.regions.remove(region);
            nextAttempt = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(plugin.getSettings().settings__paste__delay_seconds);
            logProgress(file.getName(), randomX, randomY, randomZ);
        } else {
            EasyLog.toConsole(getClass(), CC.RED + "[Failed] Cannot paste at " + randomX + ", " + randomY + ", " + randomZ + " as it was not air.");
        }
    }

    private void logProgress(String file, int x, int y, int z) {
        int percentageDone = (int) (((double) regions.size() / beginAmount) * 100);
        int percentageRemaining = 100 - percentageDone;

        long elapsedTimeMillis = System.currentTimeMillis() - startedAt;
        String duration = TimeUtil.timeAsString(elapsedTimeMillis);

        long timeRemaining = TimeUnit.SECONDS.toMillis((long) this.regions.size() * plugin.settings.settings__paste__delay_seconds);
        String estimatedRemaining = TimeUtil.timeAsString(timeRemaining);

        EasyLog.toConsole(getClass(),
                Arrays.asList(
                        "Pasted " + file + " at " + x + " " + y + " " + z,
                        "Status: " + percentageRemaining + "% (" + percentageDone + " complete)",
                        "Current Time Elapsed: " + duration,
                        "Estimated Time Remaining: " + estimatedRemaining
                )
        );
    }

}
