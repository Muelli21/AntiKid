package me.Antikid.module.modules.movement;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
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
import me.Antikid.types.Utils;
import net.minecraft.server.v1_7_R4.EntityPlayer;

public class Fly extends Module implements Listener {

    public Fly() {
	super("Fly", new ItemBuilder(Material.FEATHER).build());
    }

    @EventHandler
    public void fly(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	CraftPlayer cp = (CraftPlayer) p;
	EntityPlayer ep = (EntityPlayer) cp.getHandle();

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

	debug(cp, Arrays.asList("" + Utils.difference(p.getLocation().getY(), pd.getLastOnGround().getY())));

	if (p.getLocation().getY() - 1.26 > pd.getLastOnGround().getY()) {

	    if (ep.h_()) {
		pd.setLastOnGround(p.getLocation());

	    } else {
		pd.addFly();
		return;
	    }
	}
    }
}
