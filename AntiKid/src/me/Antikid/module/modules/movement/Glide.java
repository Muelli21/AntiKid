package me.Antikid.module.modules.movement;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.PlayerUtils;

public class Glide extends Module implements Listener {

    public Glide() {
	super("Glide", new ItemBuilder(Material.CARPET).build(), 1, 3, 5, false, BanReason.FLY);
    }

    @EventHandler
    public void glide(PlayerMoveEvent e) {

	Player player = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(player);

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }

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

	if (PlayerUtils.isFalling(player, e)) {
	    double distance = pd.getFalldistance() - player.getFallDistance();
	    if (distance < -3.93) {
		addViolation(player);
		debug(player, Arrays.asList(distance + ""));
	    }

	    pd.setFalldistance(player.getFallDistance());
	    return;
	}

	pd.setFalldistance(player.getFallDistance());

	if (System.currentTimeMillis() < pd.getFlycooldown()) {
	    debug(player, Arrays.asList("is on flycooldown"));
	    return;
	}

	if (PlayerUtils.inAir(player, e)) {
	    debug(player, Arrays.asList("in Air without alibi"));
	    addViolation(player);
	}
    }
}
