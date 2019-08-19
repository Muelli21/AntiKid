package me.Antikid.module.modules.movement;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.MathUtils;
import me.Antikid.utils.PlayerUtils;
import net.minecraft.server.v1_7_R4.EntityPlayer;

public class Fly extends Module implements Listener {

    public Fly() {
	super("Fly", new ItemBuilder(Material.FEATHER).build(), 1, 3, 5, false, BanReason.FLY);
    }

    @EventHandler
    public void fly(PlayerMoveEvent e) {

	Player player = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(player);

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }

	CraftPlayer cp = (CraftPlayer) player;
	EntityPlayer ep = (EntityPlayer) cp.getHandle();

	if (player.isFlying()) {
	    debug(player, Arrays.asList("is flying"));
	    return;
	}
	if (PlayerUtils.isCreative(player)) {
	    debug(player, Arrays.asList("is creative"));
	    return;
	}
	if (player.getAllowFlight()) {
	    debug(player, Arrays.asList("is allowed to fly"));
	    return;
	}

	if (System.currentTimeMillis() < pd.getVelocitycooldown()) {
	    debug(player, Arrays.asList("is on velocity"));
	    return;
	}

	debug(cp, Arrays.asList("" + MathUtils.difference(player.getLocation().getY(), pd.getLastOnGround().getY())));

	if (player.getLocation().getY() - 1.26 > pd.getLastOnGround().getY()) {

	    if (ep.h_()) {
		pd.setLastOnGround(player.getLocation());

	    } else {
		addViolation(player);
		return;
	    }
	}
    }
}
