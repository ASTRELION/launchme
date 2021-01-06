package com.astrelion.LaunchMe;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandController implements CommandExecutor
{
    private final LaunchMe plugin;
    private final PermissionController permissionController;

    public CommandController(LaunchMe plugin)
    {
        this.plugin = plugin;
        this.permissionController = plugin.getPermissionController();
        plugin.getCommand("launchme").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            if (permissionController.canToggle(player))
            {
                permissionController.togglePlayer(player);

                if (ConfigurationController.ENABLE_TOGGLE_PROMPT)
                {
                    boolean toggled = permissionController.isPlayerToggled(player);
                    ChatColor color = toggled ? ChatColor.GREEN : ChatColor.RED;
                    String onOff = toggled ? "on" : "off";
                    player.sendMessage("%sProjectile riding has been turned %s%s.".formatted(ChatColor.YELLOW, color, onOff));
                }
            }
        }

        return true;
    }

    public LaunchMe getPlugin()
    {
        return this.plugin;
    }
}
