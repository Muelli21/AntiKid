package me.Antikid.module.modules.player;

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

public class Timer extends Module implements Listener {

    public Timer() {
	super("Timer", new ItemBuilder(Material.WATCH).build());
    }

    @EventHandler
    public void timer(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	if (System.currentTimeMillis() > Main.lastban)
	    pd.setMovepackets(pd.getMovepackets() + 1);
    }
}