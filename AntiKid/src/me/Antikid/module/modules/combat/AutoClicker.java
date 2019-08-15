package me.Antikid.module.modules.combat;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.Lag;
import me.Antikid.types.PlayerData;

public class AutoClicker extends Module implements Listener {

    public AutoClicker() {
	super("Autoclicker", new ItemBuilder(Material.IRON_SWORD).build());

    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
	if (!isEnabled())
	    return;

	if (Lag.getTPS() < 19.5) { return; }

	if (e.getAction().toString().contains("LEFT")) {
	    Player p = e.getPlayer();
	    PlayerData pd = Main.getPlayerData(p);
	    pd.addClicks();
	}
    }
}
