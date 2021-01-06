package com.astrelion.LaunchMe;

import org.bukkit.plugin.java.JavaPlugin;

public class LaunchMe extends JavaPlugin
{
    private ConfigurationController configurationController;
    private PermissionController permissionController;
    private CommandController commandController;
    private EventController eventController;

    @Override
    public void onEnable()
    {
        configurationController = new ConfigurationController(this);
        permissionController = new PermissionController(this);
        commandController = new CommandController(this);
        eventController = new EventController(this);

        getLogger().warning("LaunchMe enabled.");
    }

    @Override
    public void onDisable()
    {
        configurationController = null;
        permissionController = null;
        commandController = null;
        eventController = null;

        getLogger().warning("LaunchMe disabled.");
    }

    public ConfigurationController getConfigurationController()
    {
        return this.configurationController;
    }

    public PermissionController getPermissionController()
    {
        return this.permissionController;
    }

    public CommandController getCommandController()
    {
        return this.commandController;
    }

    public EventController getEventController()
    {
        return this.eventController;
    }
}
