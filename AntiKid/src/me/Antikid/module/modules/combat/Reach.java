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

import me.Antikid.managers.ViolationManager;
import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.MoveTrail;
import me.Antikid.types.MoveTrail.Move;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.PlayerUtils;

public class Reach extends Module implements Listener {

    public Reach() {
	super("Reach", new ItemBuilder(Material.STONE_SWORD).build(), 1, 3, 5, false, BanReason.REACH);
    }

    @EventHandler
    public void onReach(EntityDamageByEntityEvent e) {

	if (!(e.getDamager() instanceof Player)) { return; }

	Player player = (Player) e.getDamager();
	Entity entityTarget = e.getEntity();
	Entity entityPlayer = e.getDamager();

	PlayerData pd = PlayerData.getPlayerData(player);
	MoveTrail trail = MoveTrail.trails.get(player);

	if (!isEnabled() || !PlayerUtils.checkAble(player) || entityTarget instanceof Player && !PlayerUtils.checkAble((Player) entityTarget)) { return; }
	if (PlayerUtils.isCreative(player)) { return; }

	Location locTarget = entityTarget.getLocation();

	int delay = 5 + ((int) PlayerUtils.getPing(player) / 50);
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
		    addViolation(player);
		    pd.setFakeplayervisible(true);
		    color = "§6";
		}

		if (reach > 4) {
		    addViolation(player);
		    color = "§c";

		}

		if (reach > 4.5) {
		    addViolation(player);
		    color = "§4";
		}

		DecimalFormat df = new DecimalFormat("#.##");
		String stringReach = df.format(reach);
		debug(player, Arrays.asList(color + stringReach + " §fblocks"));
	    }
	}
    }

    public static void handleReach(PlayerData pd) {

	long time = 60 * 1000;
	Module module = Module.getModuleByName("reach");
	ViolationManager violationManager = pd.getViolationManager();

	if (System.currentTimeMillis() - pd.getLastReach() > time) {
	    if (violationManager.hasViolation(module) && violationManager.getViolations().get(module).getViolations() > 0) {
		pd.getViolationManager().addViolation(module, -1);
	    }
	}
    }
}
