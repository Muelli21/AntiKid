package me.Antikid.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.ListUtils;
import me.Antikid.types.PlayerData;

public class ModulesGui extends AbstractGui {

    private int page = 0;
    private List<List<Module>> pages = new ArrayList<List<Module>>();

    public ModulesGui(Player p) {
	super(p, 54, "§7AntiKid Settings", true);

	ArrayList<Module> modules = new ArrayList<>();
	modules.addAll(Module.modules);

	modules.sort(Comparator.comparing(Module::getName));
	this.pages = ListUtils.chopped(modules, 35);

	showPage();
	openInventory();
    }

    private void nextPage() {
	page++;
	showPage();
    }

    private void previousPage() {
	page--;
	if (page < 0) {
	    openLastInventory();
	    return;
	}
	showPage();
    }

    public void setBanToggleItem() {

	ItemStack item = null;

	if (Main.isBanning()) {
	    item = new ItemBuilder.DyeBuilder(DyeColor.GREEN).build().setName("AntiKid is: §abanning").build();
	} else {
	    item = new ItemBuilder.DyeBuilder(DyeColor.RED).build().setName("AntiKid is: §cnot banning").build();
	}
	setItemLeftAction(4, item, p -> {
	    Main.toggleBan();
	    setBanToggleItem();
	});
    }

    public void setPersonalDebugToggleItem() {

	ItemStack item = null;
	PlayerData pd = Main.getPlayerData(getPlayer());

	if (pd.isDebug()) {
	    item = new ItemBuilder.SkullHeadBuilder(getPlayer()).build().setName("Debugmode: §adebugging").build();
	} else {
	    item = new ItemBuilder.SkullHeadBuilder(getPlayer()).build().setName("Debugmode: §cnot debugging").build();
	}

	setItemLeftAction(2, item, p -> {
	    pd.setDebug(!pd.isDebug());
	    setPersonalDebugToggleItem();
	});
    }

    public void setToggleAllModulesItem() {

	setItem(6,
		new ItemBuilder(Material.REDSTONE_TORCH_ON).setName("Toggle all modules").setLore(Arrays.asList("§fclick left to §aenable §fall modules", "§fclick right to §cdisable §fall modules"))
			.build(),

		p -> {
		    Module.toggleAllModules(true);
		    showPage();
		}, p -> {
		    Module.toggleAllModules(false);
		    showPage();
		});
    }

    public void setModuleItem(Module module, Integer slot) {

	ItemStack item = module.getInventoryItem();

	if (module.isEnabled()) {
	    item = new ItemBuilder(item).setName("§a" + module.getName()).build();
	} else {
	    item = new ItemBuilder.GlassPaneBuilder(DyeColor.WHITE).build().setName("§c" + module.getName()).build();
	}

	setItem(slot, item, p -> {

	    module.setEnabled(!module.isEnabled());
	    setModuleItem(module, slot);
	}, p -> {
	    new ModuleGui(p, module);
	    close();
	});
    }

    private void showPage() {
	clearGui();
	setPersonalDebugToggleItem();
	setBanToggleItem();
	setToggleAllModulesItem();

	setItemLeftAction(27, new ItemBuilder.GlassPaneBuilder(DyeColor.RED).build().setName("§cPrevious Page").setLore(Arrays.asList("§7Click to see the ", "§7previous page of modules")).build(),
		p -> previousPage());

	if (page + 1 < pages.size())
	    setItemLeftAction(35, new ItemBuilder.GlassPaneBuilder(DyeColor.GREEN).build().setName("§aNext Page").setLore(Arrays.asList("§7Click to see the ", "§7next page of modules")).build(),
		    p -> nextPage());

	if (pages.size() > 0) {

	    List<Module> thisPage = pages.get(page);
	    int slot = 18;
	    for (Module module : thisPage) {

		if (slot % 9 == 0) {
		    slot++;

		} else if ((slot + 1) % 9 == 0) {
		    slot = slot + 2;
		}

		setModuleItem(module, slot);
		slot++;
	    }
	}
	getPlayer().updateInventory();
    }
}
