package com.hastegames.asteroidrandomizer;

import com.hastegames.asteroidrandomizer.model.AsteroidRegion;
import com.hastegames.commons.config.Config;
import org.bukkit.configuration.ConfigurationSection;

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

        ConfigurationSection section = this.getConfig().getConfigurationSection("processed");
    }

    private List<AsteroidRegion> getRemainingRegions() {
        AsteroidSettings settings = plugin.getSettings();
        int minX = settings.settings__region__min_x;
        int maxX = settings.settings__region__max_x;
        int minZ = settings.settings__region__min_z;
        int maxZ = settings.settings__region__max_z;

        List<AsteroidRegion> regions = getAllRegions(minX, maxX, minZ, maxZ);

        // remove all
    }

    private List<AsteroidRegion> getAllRegions(int minX, int maxX, int minZ, int maxZ) {
        List<AsteroidRegion> regions = new LinkedList<>();

        int minimimX = Math.min(minX, maxX);
        int maximumX = Math.max(minX, maxX);
        int minimumZ = Math.min(minZ, maxZ);
        int maximumZ = Math.max(minZ, maxZ);

        for (int z = minimumZ; z <= maximumZ; z += 100) { // Process row-by-row (top to bottom)
            for (int x = minimimX; x <= maximumX; x += 100) { // Process left to right
                regions.add(new AsteroidRegion(x, z));
            }
        }

        return regions;
    }

}
