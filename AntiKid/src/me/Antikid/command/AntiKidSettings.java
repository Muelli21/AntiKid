package me.Antikid.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Antikid.AntikidData;
import me.Antikid.gui.ModulesGui;
import me.Antikid.module.Module;

public class AntiKidSettings implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!sender.hasPermission("Admin")) { return false; }

	if (args.length == 0) {
	    if (sender instanceof Player) {
		Player p = (Player) sender;
		new ModulesGui(p);
		return false;
	    }
	}

	if (args.length == 1) {
	    if (args[0].equals("autoban")) {

		AntikidData.getData().toggleBan();
		if (AntikidData.getData().isBanning()) {
		    sender.sendMessage("§c------------------");
		    sender.sendMessage("§6Bans are now §cdisabled!");
		    sender.sendMessage("§c------------------");
		    return false;

		} else {
		    sender.sendMessage("§c------------------");
		    sender.sendMessage("§6Bans are now §aenabled!");
		    sender.sendMessage("§c------------------");
		    return false;
		}
	    }
	}

	if (args.length > 0) {
	    for (Module module : Module.modules) {
		if (args.length == 2 && args[0].equals("ban") && args[1].equalsIgnoreCase(module.getName())) {
		    module.setBanning(!module.isBanning());
		    sender.sendMessage("§c------------------");
		    sender.sendMessage("§6The " + module.getName() + "module is now allowed to ban: " + module.isBanning());
		    sender.sendMessage("§c------------------");
		    return false;
		}

		if (args.length == 2 && args[0].equals("debug") && args[1].equalsIgnoreCase(module.getName())) {
		    module.setDebug(!module.isDebug());
		    sender.sendMessage("§c------------------");
		    sender.sendMessage("§6The " + module.getName() + "module is now in the debug mode: " + module.isDebug());
		    sender.sendMessage("§c------------------");
		    return false;
		}

		if (args[0].equalsIgnoreCase(module.getName())) {
		    module.setEnabled(!module.isEnabled());
		    sender.sendMessage("§c------------------");
		    sender.sendMessage("§6The " + module.getName() + " module is now enabled: " + module.isEnabled());
		    sender.sendMessage("§c------------------");
		    return false;
		}
	    }
	}
	return false;
    }
}
