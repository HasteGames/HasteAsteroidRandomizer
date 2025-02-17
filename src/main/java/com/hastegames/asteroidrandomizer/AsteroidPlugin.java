package com.hastegames.asteroidrandomizer;

import com.hastegames.asteroidrandomizer.command.AsteroidCommand;
import com.hastegames.asteroidrandomizer.fawe.AsteroidFawe;
import com.hastegames.commons.LibraryPlugin;
import com.hastegames.commons.util.EasyLog;
import com.hastegames.commons.util.string.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.A;

@Getter
public class AsteroidPlugin extends JavaPlugin {

    @Getter
    public static AsteroidPlugin instance;
    public LibraryPlugin commons;
    public AsteroidSettings settings;
    public AsteroidFawe fawe;
    public AsteroidDataConfig dataConfig;
    public AsteroidTask task;

    @Override
    public void onEnable() {
        instance = this;

        this.commons = new LibraryPlugin().onEnable(this,
                new LibraryPlugin.LibrarySettings(CC.AQUA, CC.WHITE, CC.GRAY,
                        "AsteroidRandomizer", "asteroidrandomizer.admin"));

        this.settings = new AsteroidSettings("settings", null);
        this.dataConfig = new AsteroidDataConfig("progress", this, true);

        World world = Bukkit.getWorld(this.settings.settings__world);
        if (world == null) {
            EasyLog.toConsole(getClass(), "World cannot be null: " + this.settings.settings__world,
                    new NullPointerException("Bukkit world cannot be null."));
            return;
        }

        this.fawe = new AsteroidFawe();

        this.task = new AsteroidTask(this, this.dataConfig.getRemainingRegions(), world);
        this.task.runTaskTimer(this, 0L, 20L);

        this.commons.getPaperCommandManager().registerCommand(new AsteroidCommand(this));
    }

    @Override
    public void onDisable() {
        if (this.dataConfig != null) {
            this.dataConfig.saveProgress();
        }
    }

}