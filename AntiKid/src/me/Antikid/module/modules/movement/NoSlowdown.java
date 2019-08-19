package me.Antikid.module.modules.movement;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.PlayerUtils;

public class NoSlowdown extends Module implements Listener {

    public NoSlowdown() {
	super("NoSlowdown", new ItemBuilder(Material.SOUL_SAND).build(), 1, 3, 5, false, BanReason.SPEED);
    }

    @EventHandler
    public void slowdown(PlayerMoveEvent e) {

	Player player = e.getPlayer();

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }

	if (player.isSneaking() && player.isSprinting()) {
	    addViolation(player);
	    debug(player, Arrays.asList("noslowdown sneak"));
	}

	if (player.isConversing() && player.isSprinting()) {
	    addViolation(player);
	    debug(player, Arrays.asList("noslowdown conversing"));

	}
    }
}
