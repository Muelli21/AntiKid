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

public class FlyVelocity extends Module implements Listener {

    public FlyVelocity() {
	super("FlyVelocity", new ItemBuilder(Material.PAPER).build(), 1, 3, 5, false, BanReason.FLY);
    }

    @EventHandler
    public void flyVelocity(PlayerMoveEvent e) {

	Player player = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(player);

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }

	if (PlayerUtils.isLiquid(player)) {
	    pd.setLastOnGround(player.getLocation());
	}

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

	if (player.getVelocity().getY() <= -1 && !(e.getTo().getY() < e.getFrom().getY())) {
	    addViolation(player);
	    debug(player, Arrays.asList("negative velocity but not falling"));
	}
    }
}
