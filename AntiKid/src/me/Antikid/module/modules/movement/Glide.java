package me.Antikid.module.modules.movement;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.listener.MoveListener;
import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Playerchecks;

public class Glide extends Module implements Listener {

    public Glide() {
	super("Glide", new ItemBuilder(Material.CARPET).build());
    }

    @EventHandler
    public void glide(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	if (p.isFlying()) {
	    debug(p, Arrays.asList("is flying"));
	    return;
	}
	if (Playerchecks.isCreative(p)) {
	    debug(p, Arrays.asList("is creative"));
	    return;
	}
	if (p.getAllowFlight()) {
	    debug(p, Arrays.asList("is allowed to fly"));
	    return;
	}

	if (System.currentTimeMillis() < pd.getVelocitycooldown()) {
	    debug(p, Arrays.asList("is on velocity"));
	    return;
	}

	if (Playerchecks.isFalling(p, e)) {
	    double distance = pd.getFalldistance() - p.getFallDistance();
	    if (distance < -3.93) {
		pd.getRoast().addRoast();
		debug(p, Arrays.asList(distance + ""));
	    }

	    pd.setFalldistance(p.getFallDistance());
	    return;
	}

	pd.setFalldistance(p.getFallDistance());

	if (System.currentTimeMillis() < pd.getFlycooldown()) {
	    debug(p, Arrays.asList("is on flycooldown"));
	    return;
	}

	if (Playerchecks.inAir(p, e)) {
	    debug(p, Arrays.asList("in Air without alibi"));
	    pd.addFly();
	}
    }
}
