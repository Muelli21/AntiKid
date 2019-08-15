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

import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.module.modules.movement.Direction;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.MoveTrail;
import me.Antikid.types.MoveTrail.Move;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Playerchecks;

public class Aim extends Module implements Listener {

    public Aim() {
	super("Aim", new ItemBuilder(Material.WOOD_SWORD).build());
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {

	if (!isEnabled()) { return; }
	if (!(e.getDamager() instanceof Player)) { return; }

	Player p = (Player) e.getDamager();
	PlayerData pd = Main.getPlayerData(p);

	if (!(e.getEntity() instanceof Player)) {

	    if (Playerchecks.entitiesCollide(e.getDamager(), e.getEntity())) {
		debug(p, Arrays.asList("Entities collide"));
		return;
	    }

	    Location loc = e.getEntity().getLocation();
	    Vector direction = p.getEyeLocation().getDirection().normalize().setY(0);
	    Vector realDirection = loc.toVector().subtract(p.getLocation().toVector()).normalize().setY(0);
	    Double angle = Direction.angleBetweenVectors(direction, realDirection);

	    debug(p, Arrays.asList("Angle " + angle));

	    if (angle > 50)
		pd.addAim();

	    return;
	}

	Player target = (Player) e.getEntity();
	int delay = 5 + ((int) Playerchecks.ping(p) / 50);

	MoveTrail trail = MoveTrail.trails.get(target);
	Iterator<Move> itr = trail.getLastEntries(delay);

	double smallestAngle = Double.MAX_VALUE;

	if (Playerchecks.playersCollide(p, target)) {
	    debug(p, Arrays.asList("Players collide"));
	    return;
	}

	while (itr.hasNext()) {

	    Location loc = itr.next().getLoc();
	    Vector direction = p.getEyeLocation().getDirection().normalize().setY(0);
	    Vector realDirection = loc.toVector().subtract(p.getLocation().toVector()).normalize().setY(0);
	    Double angle = Direction.angleBetweenVectors(direction, realDirection);

	    if (angle < smallestAngle)
		smallestAngle = angle;
	}

	if (smallestAngle != Double.MAX_VALUE) {
	    debug(p, Arrays.asList("Angle " + smallestAngle));

	    if (smallestAngle > 50)
		pd.addAim();
	}
    }
}
