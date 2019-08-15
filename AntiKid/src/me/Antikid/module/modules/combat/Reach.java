package me.Antikid.module.modules.combat;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.Antikid.listener.MoveListener;
import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.MoveTrail;
import me.Antikid.types.MoveTrail.Move;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Playerchecks;

public class Reach extends Module implements Listener {

    public Reach() {
	super("Reach", new ItemBuilder(Material.STONE_SWORD).build());
    }

    @EventHandler
    public void onReach(EntityDamageByEntityEvent e) {

	if (!(e.getDamager() instanceof Player)) { return; }

	Player p = (Player) e.getDamager();
	Entity entityTarget = e.getEntity();
	Entity entityPlayer = e.getDamager();

	PlayerData pd = Main.getPlayerData(p);
	MoveTrail trail = MoveTrail.trails.get(p);

	if (!isEnabled() || !MoveListener.checkAble(p) || entityTarget instanceof Player && !MoveListener.checkAble((Player) entityTarget)) { return; }
	if (Playerchecks.isCreative(p)) { return; }

	Location locTarget = entityTarget.getLocation();

	int delay = 5 + ((int) Playerchecks.ping(p) / 50);
	Iterator<Move> itr = trail.getLastEntries(delay);
	double reach = Double.MAX_VALUE;

	while (itr.hasNext()) {

	    Location loc = itr.next().getLoc();
	    loc.setY(0);
	    locTarget.setY(0);
	    double minDistance = loc.distance(locTarget);

	    if (minDistance < reach) {
		reach = minDistance;
	    }
	}

	if (reach != Double.MAX_VALUE) {
	    if (entityTarget.isOnGround() && entityPlayer.isOnGround()) {
		String color = "§a";

		if (reach > 3.8) {
		    pd.addReach();
		    pd.setFakeplayervisible(true);
		    color = "§6";
		}

		if (reach > 4) {
		    pd.addReach();
		    color = "§c";

		}

		if (reach > 4.5) {
		    pd.addReach();
		    color = "§4";
		}

		DecimalFormat df = new DecimalFormat("#.##");
		String stringReach = df.format(reach);
		debug(p, Arrays.asList(color + stringReach + " §fblocks"));
	    }
	}
    }

    public static void handleReach(PlayerData pd) {

	long time = 60 * 1000;

	if (System.currentTimeMillis() - pd.getLastReach() > time) {
	    if (pd.getReach() > 0)
		pd.setReach(pd.getReach() - 1);
	}
    }
}
