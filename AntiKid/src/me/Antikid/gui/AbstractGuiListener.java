package me.Antikid.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import me.Antikid.main.Main;
import me.Antikid.types.PlayerData;

public class AbstractGuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

	Inventory inv = event.getClickedInventory();
	int slot = event.getSlot();
	Player player = (Player) event.getWhoClicked();
	PlayerData pd = Main.getPlayerData(player);
	AbstractGui gui = AbstractGui.Guis.get(inv);

	if (gui == null) { return; }
	if (player != gui.getPlayer()) { return; }

	event.setCancelled(true);
	pd.setCurrentGui(gui);

	if (event.isRightClick() && (gui.getRightaction().get(slot) != null)) {
	    gui.getRightaction().get(slot).click(player);
	}

	if (event.isLeftClick() && (gui.getLeftaction().get(slot) != null)) {

	    gui.getLeftaction().get(slot).click(player);
	}
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

	Inventory inv = event.getInventory();
	Player player = (Player) event.getPlayer();
	AbstractGui gui = AbstractGui.Guis.get(inv);
	if (gui == null) { return; }
	if (!gui.isCloseable()) {
	    new BukkitRunnable() {
		@Override
		public void run() {
		    if (gui.getLastGui() != null) {
			gui.getLastGui().openInventory();
		    } else {
			player.openInventory(inv);
		    }
		    return;
		}
	    }.runTaskLater(Main.getPlugin(), 1);
	    return;
	}

	if (player != gui.getPlayer()) { return; }
	if (gui.getInventory() == player.getInventory()) {
	    gui.delete();
	}
    }
}
