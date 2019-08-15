package me.Antikid.gui;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Antikid.main.Main;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.PlayerData;

public abstract class AbstractGui {

    public static HashMap<Inventory, AbstractGui> Guis = new HashMap<>();

    private Inventory inventory;
    private boolean closeable;
    private HashMap<Integer, AbstractAction> leftaction = new HashMap<>();
    private HashMap<Integer, AbstractAction> rightaction = new HashMap<>();
    private Player player;
    private AbstractGui lastGui;

    public AbstractGui(Player player, int slots, String title, boolean closeable) {

	Inventory inventory = Bukkit.createInventory(null, slots, title);
	Guis.put(inventory, this);
	this.player = player;
	this.closeable = closeable;
	this.inventory = inventory;

	PlayerData pd = Main.getPlayerData(player);

	if (pd.getCurrentGui() != null) {
	    setLastGui(pd.getCurrentGui());
	}
    }

    public AbstractGui(Player player, boolean closeable) {

	Inventory inventory = player.getInventory();
	Guis.put(inventory, this);
	this.player = player;
	this.closeable = closeable;
	this.inventory = inventory;
    }

    public AbstractGui(Player player, Player target, boolean closeable) {

	Inventory inventory = target.getInventory();
	Guis.put(inventory, this);
	this.player = player;
	this.closeable = closeable;
	this.inventory = inventory;
    }

    public void setItem(int slot, ItemStack item, AbstractAction leftaction, AbstractAction rightaction) {

	inventory.setItem(slot, item);
	getLeftaction().remove(slot);
	getRightaction().remove(slot);
	getLeftaction().put(slot, leftaction);
	getRightaction().put(slot, rightaction);
    }

    public void setItemRightAction(int slot, ItemStack item, AbstractAction rightaction) {

	inventory.setItem(slot, item);
	getLeftaction().remove(slot);
	getRightaction().remove(slot);
	getRightaction().put(slot, rightaction);
    }

    public void setItemLeftAction(int slot, ItemStack item, AbstractAction leftaction) {

	inventory.setItem(slot, item);
	getLeftaction().remove(slot);
	getRightaction().remove(slot);
	getLeftaction().put(slot, leftaction);
    }

    public void clearGui() {

	inventory.clear();
	leftaction.clear();
	rightaction.clear();

	for (int i = 0; i < getInventory().getSize(); i++)
	    setItem(i, new ItemBuilder.GlassPaneBuilder(DyeColor.GRAY).build().setName("~").build(), null, null);

	player.updateInventory();
    }

    public void delete() {
	if (player == null || !player.isOnline()) { return; }

	close();
	Guis.remove(inventory);
	leftaction.clear();
	rightaction.clear();
	this.inventory = null;
	this.player = null;
    }

    public static void deleteAllInventories(Player player) {
	for (AbstractGui gui : Guis.values()) {
	    if (gui.getPlayer() == player) {
		gui.delete();
	    }
	}
    }

    public void openLastInventory() {
	if (getLastGui() != null) {
	    getLastGui().openInventory();
	} else {
	    delete();
	}
    }

    public static void closeCurrentGui(Player player) {
	PlayerData pd = Main.getPlayerData(player);
	AbstractGui gui = pd.getCurrentGui();

	if (gui != null) {
	    gui.delete();
	}
    }

    public void close() {

	if (player == null || !player.isOnline()) { return; }
	PlayerData pd = Main.getPlayerData(player);

	if (isCloseable()) {
	    if (pd.getCurrentGui() == this) {
		pd.setCurrentGui(null);
		player.closeInventory();
	    }
	} else {
	    setCloseable(true);
	    if (pd.getCurrentGui() == this) {
		pd.setCurrentGui(null);
		player.closeInventory();
	    }
	    setCloseable(false);
	}
    }

    public void openInventory() {

	PlayerData pd = Main.getPlayerData(player);
	AbstractGui currentGui = pd.getCurrentGui();

	if (currentGui != null) {
	    currentGui.close();
	}

	pd.setCurrentGui(this);
	player.openInventory(getInventory());
	player.updateInventory();
    }

    public interface AbstractAction {
	void click(Player player);
    }

    public Inventory getInventory() {
	return inventory;
    }

    public void setInv(Inventory inventory) {
	this.inventory = inventory;
    }

    public HashMap<Inventory, AbstractGui> getGuis() {
	return Guis;
    }

    public void setGuis(HashMap<Inventory, AbstractGui> guis) {
	Guis = guis;
    }

    public boolean isCloseable() {
	return closeable;
    }

    public void setCloseable(boolean closeable) {
	this.closeable = closeable;
    }

    public Player getPlayer() {
	return player;
    }

    public void setPlayer(Player player) {
	this.player = player;
    }

    public HashMap<Integer, AbstractAction> getRightaction() {
	return rightaction;
    }

    public void setRightaction(HashMap<Integer, AbstractAction> rightaction) {
	this.rightaction = rightaction;
    }

    public HashMap<Integer, AbstractAction> getLeftaction() {
	return leftaction;
    }

    public void setLeftaction(HashMap<Integer, AbstractAction> leftaction) {
	this.leftaction = leftaction;
    }

    public AbstractGui getLastGui() {
	return lastGui;
    }

    public void setLastGui(AbstractGui lastGui) {
	this.lastGui = lastGui;
    }
}
