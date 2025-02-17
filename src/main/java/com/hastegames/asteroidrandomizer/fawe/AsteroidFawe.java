package com.hastegames.asteroidrandomizer.fawe;

import com.fastasyncworldedit.core.FaweAPI;
import com.hastegames.commons.util.EasyLog;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.BlockVector3Imp;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

public class AsteroidFawe {

    public CompletableFuture<Void> pasteSchematic(Location location, File schematicFile) {
        return CompletableFuture.runAsync(() -> {
            try {
                String fileName = schematicFile.getName();

                if (location.getWorld() == null) {
                    return;
                }
                String worldName = location.getWorld().getName();

                ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(schematicFile);
                if (clipboardFormat == null) {
                    EasyLog.toConsole(getClass(), "No clipboard format found for schematic " + fileName);
                    return;
                }

                try (InputStream in = Files.newInputStream(schematicFile.toPath())) {
                    Clipboard clipboard = clipboardFormat.load(in);
                    if (clipboard == null) {
                        EasyLog.toConsole(getClass(), "Schematic " + fileName + " has no clipboard");
                        return;
                    }

                    World weWorld = FaweAPI.getWorld(worldName);
                    BlockVector3 vector3 = BlockVector3Imp.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                    clipboard.paste(weWorld, vector3, false, false, null);
                }
            } catch (Throwable t) {
                EasyLog.toConsole(getClass(), "An error occurred while pasting schematic " + schematicFile.getName(), t);
            }
        });
    }

}
