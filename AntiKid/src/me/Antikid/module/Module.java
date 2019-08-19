package me.Antikid.module;

import java.util.HashSet;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;

public class Module {

    public static HashSet<Module> modules = new HashSet<>();
    private String name;
    private boolean enabled = true;
    private boolean debug = false;
    private boolean banning = true;
    private ItemStack inventoryItem;
    private int violationsAlert;
    private int violationsRoast;
    private int violationsBan;
    private boolean experimental;
    private BanReason banReason;

    public Module(String name, ItemStack inventoryItem, int violationsAlert, int violationsRoast, int violationsBan, boolean experimental, BanReason banReason) {
	this.name = name;
	this.inventoryItem = new ItemBuilder(inventoryItem).setName(name).build();
	this.violationsAlert = violationsAlert;
	this.violationsRoast = violationsRoast;
	this.violationsBan = violationsBan;
	this.experimental = experimental;
	this.banReason = banReason;
	modules.add(this);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public BanReason getBanReason() {
	return banReason;
    }

    public void setBanReason(BanReason banReason) {
	this.banReason = banReason;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    public boolean isDebug() {
	return debug;
    }

    public void setDebug(boolean debug) {
	this.debug = debug;
    }

    public boolean isBanning() {
	return banning;
    }

    public void setBanning(boolean banning) {
	this.banning = banning;
    }

    public ItemStack getInventoryItem() {
	return inventoryItem;
    }

    public static HashSet<Module> getModules() {
	return modules;
    }

    public boolean isExperimental() {
	return experimental;
    }

    public void addViolation(Player player) {
	PlayerData pd = PlayerData.getPlayerData(player);
	pd.getViolationManager().addViolation(this, 1);
    }

    public int getViolationsAlert() {
	return violationsAlert;
    }

    public int getViolationsRoast() {
	return violationsRoast;
    }

    public int getViolationsBan() {
	return violationsBan;
    }

    public static Module getModuleByName(String name) {
	for (Module module : modules) {
	    if (module.getName().equalsIgnoreCase(name)) { return module; }
	}
	return null;
    }

    public static void toggleAllModules(boolean enabled) {
	modules.forEach(module -> {
	    module.setEnabled(enabled);
	});
    }

    public void debug(Player player, List<String> debugMessages) {
	PlayerData pd = PlayerData.getPlayerData(player);

	if (isDebug() && pd.isDebug()) {
	    debugMessages.forEach(message -> {
		if (debugMessages.get(0).equals(message)) {
		    message = "§f{§6De§cbug§f} > " + getName() + ": " + message;
		}
		player.sendMessage(message);
	    });
	}
    }
}
