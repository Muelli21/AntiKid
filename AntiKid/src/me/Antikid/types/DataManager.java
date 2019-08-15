package me.Antikid.types;

import org.bukkit.configuration.file.FileConfiguration;

import me.Antikid.main.Main;
import me.Antikid.module.Module;

public class DataManager {

    public static void loadAntikid() {

	FileConfiguration config = Main.getPlugin().getConfig();
	boolean ban = config.getBoolean("Antikid.ban");
	Main.ban = ban;

	Module.modules.forEach(module -> {

	    boolean banning = config.getBoolean("Antikid.module." + module.getName() + ".banning");
	    boolean enabled = config.getBoolean("Antikid.module." + module.getName() + ".enabled");
	    boolean debug = config.getBoolean("Antikid.module." + module.getName() + ".debug");

	    module.setBanning(banning);
	    module.setEnabled(enabled);
	    module.setDebug(debug);
	});
    }

    public static void saveAntikid() {

	FileConfiguration config = Main.getPlugin().getConfig();
	config.set("Antikid.ban", Main.ban);

	Module.modules.forEach(module -> {
	    config.set("Antikid.module." + module.getName() + ".enabled", module.isEnabled());
	    config.set("Antikid.module." + module.getName() + ".banning", module.isBanning());
	    config.set("Antikid.module." + module.getName() + ".debug", module.isDebug());
	});

	Main.getPlugin().saveConfig();
    }
}
