package com.astrelion.LaunchMe;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class PermissionController
{
    public static final String CAN_TOGGLE = "launchme.toggle";
    public static final String CAN_LAUNCH = "launchme.launch";
    public static final String HAS_NO_COOLDOWN = "launchme.nocooldown";

    private List<Player> toggledPlayers;
    private List<Player> onCooldownPlayers;

    private final LaunchMe plugin;

    public PermissionController(LaunchMe plugin)
    {
        this.plugin = plugin;

        toggledPlayers = new ArrayList<>();
        onCooldownPlayers = new ArrayList<>();
    }

    /**
     * Determine if given player can launch themselves
     * @param player the Player to check
     * @return True if the player can launch
     */
    public boolean canLaunch(Player player)
    {
        return
            player.hasPermission(CAN_LAUNCH) &&
            isPlayerToggled(player) &&
            !isPlayerOnCooldown(player);
    }

    public boolean isPlayerToggled(Player player)
    {
        return toggledPlayers.contains(player);
    }

    public boolean isPlayerOnCooldown(Player player)
    {
        return onCooldownPlayers.contains(player);
    }

    public void putPlayerOnCooldown(Player player)
    {
        if (!isPlayerOnCooldown(player))
        {
            this.onCooldownPlayers.add(player);

            BukkitScheduler scheduler = getPlugin().getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(getPlugin(), () ->
            {
                this.onCooldownPlayers.remove(player);

                if (ConfigurationController.ENABLE_COOLDOWN_DONE_PROMPT)
                {
                    player.sendMessage("%sLaunching is available again!".formatted(ChatColor.GREEN));
                }

            }, ConfigurationController.COOLDOWN_TIME);
        }
        else if (ConfigurationController.ENABLE_COOLDOWN_PROGRESS_PROMPT)
        {
            player.sendMessage("%sLaunching is on cooldown.".formatted(ChatColor.RED));
        }
    }

    public void togglePlayer(Player player)
    {
        if (toggledPlayers.contains(player))
        {
            toggledPlayers.remove(player);
        }
        else
        {
            toggledPlayers.add(player);
        }
    }

    public boolean canToggle(Player player)
    {
        return player.hasPermission(CAN_TOGGLE);
    }

    public LaunchMe getPlugin()
    {
        return this.plugin;
    }
}
