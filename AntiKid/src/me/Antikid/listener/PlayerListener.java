package me.Antikid.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.util.Vector;

import me.Antikid.main.Main;
import me.Antikid.types.MoveTrail;
import me.Antikid.types.PlayerData;
import me.Antikid.types.SittingReachCheck;

public class PlayerListener implements Listener {

    @EventHandler
    public void onEntityLeave(VehicleExitEvent e) {

	if (!(e.getExited() instanceof Player))
	    return;
	Player p = (Player) e.getExited();
	PlayerData pd = Main.getPlayerData(p);
	SittingReachCheck check = pd.getSittingCheck();

	if (check != null) {
	    e.setCancelled(true);
	    return;
	}
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent e) {
	Player p = (Player) e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	pd.setVelocitycooldown(8000);
    }

    @EventHandler
    private void onVel(PlayerVelocityEvent e) {

	Player p = e.getPlayer();

	if (!p.isOnline()) { return; }

	PlayerData pd = Main.getPlayerData(p);
	Vector vel = e.getVelocity();

	pd.setVelocitycooldown((long) (vel.length() * 2000));
	pd.setFlycooldown((long) (vel.length() * 1000));
    }

    @EventHandler
    private void onHit(EntityDamageEvent e) {
	if (!(e.getEntity() instanceof Player)) { return; }

	Player p = (Player) e.getEntity();
	PlayerData pd = Main.getPlayerData(p);

	if (e.getCause().equals(DamageCause.ENTITY_ATTACK)) {
	    pd.setHitcooldown(500);
	}
	if (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
	    pd.setVelocitycooldown(5000);
	}
    }

    @EventHandler
    private void onTeleport(PlayerTeleportEvent e) {

	Player p = (Player) e.getPlayer();
	resetPlayer(p, e.getTo());
    }

    @EventHandler
    private void onRespwan(PlayerRespawnEvent e) {

	Player p = (Player) e.getPlayer();
	resetPlayer(p, e.getRespawnLocation());
    }

    private void resetPlayer(Player p, Location loc) {

	PlayerData pd = Main.getPlayerData(p);

	if (pd == null) { return; }

	pd.setFlycooldown(2000);
	pd.setLastOnGround(loc);
	pd.setOnGroundcooldown(2000);
	pd.setVelocitycooldown(2000);

	MoveTrail trail = MoveTrail.trails.get(p);
	if (trail != null) {
	    trail.clear();
	}
    }
}
