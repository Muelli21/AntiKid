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

public class NoSlowdown extends Module implements Listener {

    public NoSlowdown() {
	super("NoSlowdown", new ItemBuilder(Material.SOUL_SAND).build());
    }

    @EventHandler
    public void slowdown(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	if (p.isSneaking() && p.isSprinting()) {
	    pd.setNoslowdown(pd.getNoslowdown() + 1);
	    debug(p, Arrays.asList("noslowdown sneak"));
	}

	if (p.isConversing() && p.isSprinting()) {
	    pd.setNoslowdown(pd.getNoslowdown() + 1);
	    debug(p, Arrays.asList("noslowdown conversing"));

	}
    }
}
