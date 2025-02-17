package com.hastegames.asteroidrandomizer;

import com.google.common.base.Joiner;
import com.hastegames.commons.config.AnnotatedConfig;
import com.hastegames.commons.util.EasyLog;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AsteroidSettings extends AnnotatedConfig {

    public boolean enabled;

    public String settings__world;
    public String settings__teleport_command;
    public int settings__paste__delay_seconds;
    public int settings__paste__gap;
    public int settings__region__min_x;
    public int settings__region__max_x;
    public int settings__region__min_y;
    public int settings__region__max_y;
    public int settings__region__min_z;
    public int settings__region__max_z;

    @Getter
    private final List<File> schematics = new ArrayList<>();

    public AsteroidSettings(String file, List<String> ignoredSections) {
        super(file, ignoredSections);
        reload();
        loadSchematics();
    }

    public boolean toggleRunning() {
        setAndSave(getField("enabled"), !enabled);
        return enabled;
    }

    public void loadSchematics() {
        this.schematics.clear();
        AsteroidPlugin plugin = AsteroidPlugin.getInstance();

        File dir = new File(plugin.getDataFolder(), "schematics/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        List<File> files = Arrays.asList(Objects.requireNonNull(dir.listFiles()));
        schematics.addAll(files);

        String fileNames = Joiner.on(", ").join(files.stream().map(File::getName).collect(Collectors.toSet()));
        EasyLog.toConsole(getClass(), "Loaded " + this.schematics.size() + " schematic files. (" + fileNames + ")");
    }

}
