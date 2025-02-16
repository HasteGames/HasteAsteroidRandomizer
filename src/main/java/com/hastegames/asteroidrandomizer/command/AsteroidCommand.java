package com.hastegames.asteroidrandomizer.command;

import com.hastegames.asteroidrandomizer.AsteroidPlugin;
import com.hastegames.asteroidrandomizer.AsteroidTask;
import com.hastegames.commons.acf.BaseCommand;
import com.hastegames.commons.acf.annotation.CommandAlias;
import com.hastegames.commons.acf.annotation.Subcommand;
import com.hastegames.commons.util.datetime.TimeUtil;
import com.hastegames.commons.util.string.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

@CommandAlias("asteroidrandomizer")
@RequiredArgsConstructor
public class AsteroidCommand extends BaseCommand {

    private final AsteroidPlugin plugin;

    @Subcommand("toggle")
    public void onToggle(CommandSender sender) {
        boolean newStatus = plugin.getSettings().toggleRunning();
        sender.sendMessage(newStatus ? CC.GREEN + "Schematic placement is now running." :
                CC.RED + "Schematic placement is no longer running.");
    }

    @Subcommand("status")
    public void onStatus(CommandSender sender) {
        AsteroidTask task = plugin.getTask();
        int percentageRemaining = (int) (((double) task.regions.size() / task.beginAmount) * 100);
        int percentageDone = 100 - percentageRemaining;

        long elapsedTimeMillis = System.currentTimeMillis() - task.startedAt;
        String duration = TimeUtil.timeAsString(elapsedTimeMillis);

        long timeRemaining = TimeUnit.SECONDS.toMillis((long) task.regions.size() * plugin.settings.settings__paste__delay_seconds);
        String estimatedRemaining = TimeUtil.timeAsString(timeRemaining);

        sender.sendMessage(CC.PRIMARY + "Progress: " + CC.SECONDARY + percentageDone + "%/" + percentageRemaining + "%");
        sender.sendMessage(CC.PRIMARY + "Current Elapsed Time: " + CC.SECONDARY + duration);
        sender.sendMessage(CC.PRIMARY + "Estimated Time Remaining: " + CC.SECONDARY + estimatedRemaining);
    }

}
