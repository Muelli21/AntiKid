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

public class FlyVelocity extends Module implements Listener {

    public FlyVelocity() {
	super("FlyVelocity", new ItemBuilder(Material.PAPER).build());
    }

    @EventHandler
    public void flyVelocity(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	if (Playerchecks.isLiquid(p)) {
	    pd.setLastOnGround(p.getLocation());
	}

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

	if (p.getVelocity().getY() <= -1 && !(e.getTo().getY() < e.getFrom().getY())) {
	    pd.addFly();
	    debug(p, Arrays.asList("negative velocity but not falling"));
	}
    }
}
