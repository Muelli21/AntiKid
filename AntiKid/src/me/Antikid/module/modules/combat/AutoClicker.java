package me.Antikid.module.modules.combat;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.ServerUtils;

public class AutoClicker extends Module implements Listener {

    public AutoClicker() {
	super("Autoclicker", new ItemBuilder(Material.IRON_SWORD).build(), 5, 10, 15, false, BanReason.AUTOCLICKER);

    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
	if (!isEnabled())
	    return;

	if (ServerUtils.getTps() < 19.5) { return; }

	if (e.getAction().toString().contains("LEFT")) {
	    Player p = e.getPlayer();
	    PlayerData pd = PlayerData.getPlayerData(p);
	    pd.addClicks();
	}
    }
}
