package me.Antikid.gui;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;

public class ModuleGui extends AbstractGui {

    public ModuleGui(Player p, Module module) {
	super(p, 18, "§7" + module.getName(), true);

	clearGui();
	setItems(module);
	openInventory();
    }

    private void setItems(Module module) {
	setItemLeftAction(0, new ItemBuilder.GlassPaneBuilder(DyeColor.RED).build().setName("§cGo back").build(), p -> {
	    openLastInventory();
	});

	setItemLeftAction(2, new ItemBuilder(Material.COMMAND).setName("§7Debug").build(), null);
	setItemLeftAction(3, new ItemBuilder(Material.IRON_FENCE).setName("§7Ban").build(), null);
	setDebugItem(module);
	setBanItem(module);
    }

    private void setDebugItem(Module module) {

	ItemStack item = null;

	if (module.isDebug()) {
	    item = new ItemBuilder.GlassPaneBuilder(DyeColor.GREEN).build().setName("§ais debugging").build();
	} else {
	    item = new ItemBuilder.GlassPaneBuilder(DyeColor.RED).build().setName("§cis not debugging").build();
	}
	setItemLeftAction(11, item, p -> {
	    module.setDebug(!module.isDebug());
	    setDebugItem(module);
	});
    }

    private void setBanItem(Module module) {

	ItemStack item = null;

	if (module.isBanning()) {
	    item = new ItemBuilder.GlassPaneBuilder(DyeColor.GREEN).build().setName("§ais banning").build();
	} else {
	    item = new ItemBuilder.GlassPaneBuilder(DyeColor.RED).build().setName("§cis not banning").build();
	}
	setItemLeftAction(12, item, p -> {
	    module.setBanning(!module.isBanning());
	    setBanItem(module);
	});
    }
}
