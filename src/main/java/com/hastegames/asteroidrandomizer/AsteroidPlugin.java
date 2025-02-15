package com.hastegames.asteroidrandomizer;

import com.hastegames.commons.LibraryPlugin;
import com.hastegames.commons.util.string.CC;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class AsteroidPlugin extends JavaPlugin {

    @Getter public static AsteroidPlugin instance;
    public LibraryPlugin commons;
    public AsteroidSettings settings;
    public AsteroidDataConfig dataConfig;

    @Override
    public void onEnable() {
        instance = this;

        this.commons = new LibraryPlugin().onEnable(this,
                new LibraryPlugin.LibrarySettings(CC.AQUA, CC.WHITE, CC.GRAY,
                        "AsteroidRandomizer", "asteroidrandomizer.admin"));

        this.settings = new AsteroidSettings("settings", null);
        this.dataConfig = new AsteroidDataConfig("progress", this, true);
    }

    @Override
    public void onDisable() {
    }

}