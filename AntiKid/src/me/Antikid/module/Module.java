package me.Antikid.module;

import java.util.HashSet;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Antikid.main.Main;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.PlayerData;

public class Module {

    public static HashSet<Module> modules = new HashSet<>();
    private String name;
    private boolean enabled = true;
    private boolean debug = false;
    private boolean banning = true;
    private ItemStack inventoryItem;

    public Module(String name, ItemStack inventoryItem) {
	this.name = name;
	this.inventoryItem = new ItemBuilder(inventoryItem).setName(name).build();
	modules.add(this);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
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

    public void debug(Player p, List<String> debugMessages) {
	PlayerData pd = Main.getPlayerData(p);

	if (isDebug() && pd.isDebug()) {
	    debugMessages.forEach(message -> {
		if (debugMessages.get(0).equals(message)) {
		    message = "§f{§6De§cbug§f} > " + getName() + ": " + message;
		}
		p.sendMessage(message);
	    });
	}
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
}
