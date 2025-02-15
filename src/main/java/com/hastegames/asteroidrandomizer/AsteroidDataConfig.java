package com.hastegames.asteroidrandomizer;

import com.hastegames.asteroidrandomizer.model.AsteroidRegion;
import com.hastegames.commons.config.Config;
import com.hastegames.commons.util.EasyLog;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AsteroidDataConfig extends Config {

    private final AsteroidPlugin plugin;
    private final Set<AsteroidRegion> alreadyProcessed = new HashSet<>();

    public AsteroidDataConfig(String path, AsteroidPlugin plugin, boolean create) {
        super(path, plugin, create);
        this.plugin = plugin;

        List<String> savedProgress = this.getConfig().getStringList("progress");
        savedProgress.forEach(line -> {
            try {
                String[] split = line.split(",");
                int minX = Integer.parseInt(split[0]);
                int maxX = Integer.parseInt(split[1]);
                int minZ = Integer.parseInt(split[2]);
                int maxZ = Integer.parseInt(split[3]);
                AsteroidRegion processedRegion = new AsteroidRegion(minX, maxX, minZ, maxZ);
                this.alreadyProcessed.add(processedRegion);
            } catch (Exception e) {
                EasyLog.toConsole(getClass(), "Illegal line format in progress.yml of line: " + line);
            }
        });
    }

    public List<AsteroidRegion> getRemainingRegions() {
        AsteroidSettings settings = plugin.getSettings();
        int minX = settings.settings__region__min_x;
        int maxX = settings.settings__region__max_x;
        int minZ = settings.settings__region__min_z;
        int maxZ = settings.settings__region__max_z;
        int pasteGap = settings.settings__paste__gap;

        List<AsteroidRegion> regions = getAllRegions(minX, maxX, minZ, maxZ, pasteGap);
        regions.removeAll(this.alreadyProcessed);

        return regions;
    }

    private List<AsteroidRegion> getAllRegions(int minX, int maxX, int minZ, int maxZ, int gap) {
        List<AsteroidRegion> regions = new LinkedList<>();

        int minimumX = Math.min(minX, maxX);
        int maximumX = Math.max(minX, maxX);
        int minimumZ = Math.min(minZ, maxZ);
        int maximumZ = Math.max(minZ, maxZ);

        for (int z = minimumZ; z <= maximumZ; z += gap) { // Process row-by-row (top to bottom)
            for (int x = minimumX; x <= maximumX; x += gap) { // Process left to right
                int regionMaxX = Math.min(x + gap - 1, maximumX); // Ensure it doesn't exceed bounds
                int regionMaxZ = Math.min(z + gap - 1, maximumZ);

                regions.add(new AsteroidRegion(x, regionMaxX, z, regionMaxZ));
            }
        }

        return regions;
    }

    public void saveProgress() {
        long start = System.currentTimeMillis();

        List<String> alreadyProcessed = this.alreadyProcessed.stream().map(AsteroidRegion::asFormat).toList();
        this.set("progress", alreadyProcessed);

        long end = System.currentTimeMillis();
        EasyLog.toConsole(getClass(), "Saved progress in " + (end - start) + "ms.");
    }

}
