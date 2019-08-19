package me.Antikid.module.modules.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.AntikidData;
import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.PlayerUtils;

public class Timer extends Module implements Listener {

    public Timer() {
	super("Timer", new ItemBuilder(Material.WATCH).build(), 1, 2, 3, false, BanReason.OTHER);
    }

    @EventHandler
    public void timer(PlayerMoveEvent e) {

	Player player = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(player);

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }

	if (System.currentTimeMillis() > AntikidData.getData().getLastban())
	    pd.addMovepacket();
    }
}