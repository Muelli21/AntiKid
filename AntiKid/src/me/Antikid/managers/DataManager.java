package me.Antikid.managers;

import org.bukkit.configuration.file.FileConfiguration;

import me.Antikid.Antikid;
import me.Antikid.AntikidData;
import me.Antikid.module.Module;

public class DataManager {

    public static void loadAntikid() {

	FileConfiguration config = Antikid.getPlugin().getConfig();
	boolean ban = config.getBoolean("Antikid.ban");
	AntikidData.getData().setBanning(ban);

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

	FileConfiguration config = Antikid.getPlugin().getConfig();
	config.set("Antikid.ban", AntikidData.getData().isBanning());

	Module.modules.forEach(module -> {
	    config.set("Antikid.module." + module.getName() + ".enabled", module.isEnabled());
	    config.set("Antikid.module." + module.getName() + ".banning", module.isBanning());
	    config.set("Antikid.module." + module.getName() + ".debug", module.isDebug());
	});

	Antikid.getPlugin().saveConfig();
    }
}
