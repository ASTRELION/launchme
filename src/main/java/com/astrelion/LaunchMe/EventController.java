package com.astrelion.LaunchMe;

import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

public class EventController implements Listener
{
    private final LaunchMe plugin;
    private final PermissionController permissionController;
    private final ConfigurationController configurationController;

    public EventController(LaunchMe plugin)
    {
        this.plugin = plugin;
        this.permissionController = plugin.getPermissionController();
        this.configurationController = plugin.getConfigurationController();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event)
    {
        Projectile projectile = event.getEntity();

        if (projectile.getShooter() instanceof LivingEntity &&
            configurationController.isProjectileEnabled(projectile))
        {
            if (!ConfigurationController.ENABLE_MOB_LAUNCHES && projectile.getShooter() instanceof Monster)
            {
                return;
            }

            LivingEntity shooter = (LivingEntity) projectile.getShooter();
            boolean success = rideProjectile(shooter, projectile);

            if (shooter instanceof Player && permissionController.isPlayerOnCooldown((Player) shooter))
            {
                shooter.sendMessage("%sLaunching is on cooldown.".formatted(ChatColor.YELLOW));
            }
            else if (!success && ConfigurationController.ENABLE_TOGGLE_PROMPT)
            {
                shooter.sendMessage("%sYou need permission or to toggle /launchme!".formatted(ChatColor.RED));
            }

            if (success && shooter instanceof Player)
            {
                permissionController.putPlayerOnCooldown((Player) shooter);
            }
        }
    }

    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event)
    {
        Projectile projectile = event.getEntity();
        ProjectileSource source = projectile.getShooter();

        if (source instanceof LivingEntity &&
            projectile.getPassengers().contains(source) &&
            configurationController.isProjectileEnabled(projectile))
        {
            projectile.removePassenger((LivingEntity) source);
        }
    }

    /**
     *
     * @param entity the entity
     * @param projectile the Projectile
     * @return True if the "ride" was successful
     */
    private boolean rideProjectile(LivingEntity entity, Projectile projectile)
    {
        if (entity instanceof Monster ||
            (entity instanceof Player && permissionController.canLaunch((Player) entity)))
        {
            if (ConfigurationController.SHOULD_RESET_VEHICLE &&
                entity.isInsideVehicle())
            {
                entity.getVehicle().removePassenger(entity);
            }

            if (ConfigurationController.ENABLE_LAUNCH_MESSAGE && entity instanceof Player)
            {
                entity.sendMessage("%sYou're riding a %s!".formatted(ChatColor.GREEN, projectile.getName()));
            }

            projectile.addPassenger(entity);

            return true;
        }

        return false;
    }
}
