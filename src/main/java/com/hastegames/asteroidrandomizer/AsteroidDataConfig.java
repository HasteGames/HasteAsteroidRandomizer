package com.hastegames.asteroidrandomizer;

import com.hastegames.asteroidrandomizer.model.AsteroidRegion;
import com.hastegames.commons.config.Config;
import com.hastegames.commons.util.EasyLog;
import lombok.Getter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class AsteroidDataConfig extends Config {

    private final AsteroidPlugin plugin;
    public final Set<AsteroidRegion> alreadyProcessed = new HashSet<>();

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
                EasyLog.toConsole(getClass(), "Added already processed region: " + minX + " " + maxX + " " + minZ + " " + maxZ);
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
        EasyLog.toConsole(getClass(), "All regions is a size of: " + regions.size());
        if (!this.alreadyProcessed.isEmpty()) {
            this.alreadyProcessed.forEach(alreadyProcessed ->
                    regions.removeIf(region -> region.asFormat().equalsIgnoreCase(alreadyProcessed.asFormat())));
            EasyLog.toConsole(getClass(), "Remaining regions is a size of: " + regions.size());
        }

        return regions;
    }

    private List<AsteroidRegion> getAllRegions(int minX, int maxX, int minZ, int maxZ, int gap) {
        EasyLog.toConsole(getClass(), "minX: " + minX + ", maxX: " + maxX);
        EasyLog.toConsole(getClass(), "minZ: " + minZ + ", maxZ: " + maxZ);
        EasyLog.toConsole(getClass(), "gap: " + gap);

        List<AsteroidRegion> regions = new LinkedList<>();

        // Calculate the number of sub-regions horizontally (X direction) and vertically (Z direction)
        int numRegionsX = (maxX - minX) / gap;
        int numRegionsZ = (maxZ - minZ) / gap;
        EasyLog.toConsole(getClass(), "numRegionsX: " + numRegionsX);
        EasyLog.toConsole(getClass(), "numRegionsZ: " + numRegionsZ);

        // Loop through the sub-regions and calculate the min and max coordinates for each
        for (int i = 0; i < numRegionsX; i++) {
            for (int j = 0; j < numRegionsZ; j++) {
                // Calculate the min and max X and Z for the current sub-region
                int subMinX = minX + i * gap;
                int subMaxX = Math.min(minX + (i + 1) * gap, maxX);
                int subMinZ = minZ + j * gap;
                int subMaxZ = Math.min(minZ + (j + 1) * gap, maxZ);

                // Do something with the sub-region min/max coordinates (subMinX, subMaxX, subMinZ, subMaxZ)
                AsteroidRegion newRegion = new AsteroidRegion(subMinX, subMaxX, subMinZ, subMaxZ);
                regions.add(newRegion);
            }
        }

        EasyLog.toConsole(getClass(), "Creating a total of: " + regions.size() + " regions to process.");
        return regions;
    }

    public void saveProgress() {
        long start = System.currentTimeMillis();

        List<String> alreadyProcessed = this.alreadyProcessed.stream().map(AsteroidRegion::asFormat).collect(Collectors.toList());
        this.set("progress", alreadyProcessed);

        long end = System.currentTimeMillis();
        EasyLog.toConsole(getClass(), "Saved progress in " + (end - start) + "ms.");
    }

}
