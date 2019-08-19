package me.Antikid.module.modules.combat;

import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import me.Antikid.module.Module;
import me.Antikid.module.modules.movement.Direction;
import me.Antikid.types.BanReason;
import me.Antikid.types.MoveTrail;
import me.Antikid.types.MoveTrail.Move;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.PlayerUtils;

public class Aim extends Module implements Listener {

    public Aim() {
	super("Aim", new ItemBuilder(Material.WOOD_SWORD).build(), 3, 4, 5, false, BanReason.AIM);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {

	if (!isEnabled()) { return; }
	if (!(e.getDamager() instanceof Player)) { return; }

	Player player = (Player) e.getDamager();

	if (!(e.getEntity() instanceof Player)) {

	    if (PlayerUtils.entitiesCollide(e.getDamager(), e.getEntity())) {
		debug(player, Arrays.asList("Entities collide"));
		return;
	    }

	    Location loc = e.getEntity().getLocation();
	    Vector direction = player.getEyeLocation().getDirection().normalize().setY(0);
	    Vector realDirection = loc.toVector().subtract(player.getLocation().toVector()).normalize().setY(0);
	    Double angle = Direction.angleBetweenVectors(direction, realDirection);

	    debug(player, Arrays.asList("Angle " + angle));

	    if (angle > 50)
		addViolation(player);
	    return;
	}

	Player target = (Player) e.getEntity();
	int delay = 5 + ((int) PlayerUtils.getPing(player) / 50);

	MoveTrail trail = MoveTrail.trails.get(target);
	Iterator<Move> itr = trail.getLastEntries(delay);

	double smallestAngle = Double.MAX_VALUE;

	if (PlayerUtils.playersCollide(player, target)) {
	    debug(player, Arrays.asList("Players collide"));
	    return;
	}

	while (itr.hasNext()) {

	    Location loc = itr.next().getLoc();
	    Vector direction = player.getEyeLocation().getDirection().normalize().setY(0);
	    Vector realDirection = loc.toVector().subtract(player.getLocation().toVector()).normalize().setY(0);
	    Double angle = Direction.angleBetweenVectors(direction, realDirection);

	    if (angle < smallestAngle)
		smallestAngle = angle;
	}

	if (smallestAngle != Double.MAX_VALUE) {
	    debug(player, Arrays.asList("Angle " + smallestAngle));

	    if (smallestAngle > 50)
		addViolation(player);
	}
    }
}
