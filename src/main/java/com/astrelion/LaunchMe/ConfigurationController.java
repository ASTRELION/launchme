package com.astrelion.LaunchMe;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationController
{
    public static boolean ENABLED;
    public static boolean ENABLE_MOB_LAUNCHES;
    public static boolean SHOULD_RESET_VEHICLE;
    public static int COOLDOWN_TIME;

    public static boolean ENABLE_TOGGLE_PROMPT;
    public static boolean ENABLE_COOLDOWN_PROGRESS_PROMPT;
    public static boolean ENABLE_COOLDOWN_DONE_PROMPT;
    public static boolean ENABLE_LAUNCH_MESSAGE;

    public static final EntityType[] PROJECTILES = {
        EntityType.ARROW,
        EntityType.EGG,
        EntityType.ENDER_PEARL,
        EntityType.THROWN_EXP_BOTTLE,
        EntityType.FIREWORK,
        EntityType.FISHING_HOOK,
        EntityType.SNOWBALL,
        EntityType.SPLASH_POTION,
        EntityType.TRIDENT
    };
    public static List<EntityType> ENABLED_PROJECTILES;

    private final LaunchMe plugin;

    private FileConfiguration config;

    public ConfigurationController(LaunchMe plugin)
    {
        this.plugin = plugin;
        ENABLED_PROJECTILES = new ArrayList<>();

        this.saveDefaultConfig();
        this.loadConfig();
    }

    public LaunchMe getPlugin()
    {
        return this.plugin;
    }

    public void saveDefaultConfig()
    {
        this.plugin.saveDefaultConfig();
    }

    public void saveConfig()
    {
        this.plugin.saveConfig();
    }

    public void loadConfig()
    {
        this.config = this.plugin.getConfig();

        ENABLED = this.config.getBoolean("enable");
        ENABLE_MOB_LAUNCHES = this.config.getBoolean("enableMobLaunches");
        SHOULD_RESET_VEHICLE = this.config.getBoolean("shouldResetVehicle");
        COOLDOWN_TIME = this.config.getInt("cooldownTime");

        ENABLE_TOGGLE_PROMPT = this.config.getBoolean("enableTogglePrompt");
        ENABLE_COOLDOWN_PROGRESS_PROMPT = this.config.getBoolean("enableCooldownProgressPrompt");
        ENABLE_COOLDOWN_DONE_PROMPT = this.config.getBoolean("enableCooldownDonePrompt");
        ENABLE_LAUNCH_MESSAGE = this.config.getBoolean("enableLaunchMessage");

        List<?> projs = this.config.getList("launchableProjectiles");
        ENABLED_PROJECTILES.clear();

        for (var p : projs)
        {
            ENABLED_PROJECTILES.add(EntityType.valueOf(p.toString()));
        }
    }

    public boolean isProjectileEnabled(Projectile projectile)
    {
        return ENABLED_PROJECTILES.contains(projectile.getType());
    }
}
