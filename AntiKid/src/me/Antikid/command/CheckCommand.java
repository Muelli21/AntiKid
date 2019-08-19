package me.Antikid.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.Antikid.managers.YamlFileManager;
import me.Antikid.module.Module;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.BanUtils;

public class CheckCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!sender.hasPermission("administration")) { return false; }

	if (args.length == 2) {

	    if (args[0].equals("log")) {
		if (Bukkit.getOfflinePlayer(args[1]) == null) {
		    sender.sendMessage(ChatColor.RED + "This player does not exist!");
		    return false;
		}
	    }

	    if (args[0].equals("cheat")) {
		if (Bukkit.getPlayer(args[1]) == null) {
		    sender.sendMessage(ChatColor.RED + "This player is not online!");
		    return false;
		}

		Player target = Bukkit.getPlayer(args[1]);
		PlayerData td = PlayerData.getPlayerData(target);

		int roasts = td.getRoast().getRoasts();
		int roastsExperimental = td.getRoast().getRoastsExperimental();
		int cps = td.getCps();

		Module reachModule = Module.getModuleByName("reach");
		Module killauraModule = Module.getModuleByName("killaura");

		int reachhits = td.getViolationManager().getViolationAmount(reachModule);
		int killaura = td.getViolationManager().getViolationAmount(killauraModule);

		sender.sendMessage("-----------------------------");
		sender.sendMessage("§cThe player " + target.getName() + " has:");
		sender.sendMessage("-----------------------------");
		sender.sendMessage("§r- §6" + roasts + " roasts");
		sender.sendMessage("§r- §6" + roastsExperimental + " roastsExperimental");
		sender.sendMessage("§r- §6" + cps + " clicks per second");
		sender.sendMessage("§r- §6" + reachhits + " reachhits");
		sender.sendMessage("§r- §6" + killaura + " killaurahits");
		sender.sendMessage("-----------------------------");

	    }

	    if (args[0].equals("ban")) {
		if (Bukkit.getOfflinePlayer(args[1]) == null) {
		    sender.sendMessage("§cThis player has never been online!");
		    return false;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
		String uuid = target.getUniqueId().toString();

		YamlFileManager fileManager = new YamlFileManager();
		YamlConfiguration file = fileManager.loadFile("plugins/PlayerFiles", uuid);

		if (file == null || !file.getBoolean("ban")) {
		    sender.sendMessage("§cThis player is not banned at all!");
		    return false;
		}

		String reason = file.getString("banReason");
		String date = file.getString("banDate");
		long time = file.getLong("banTime");
		long executeTime = file.getLong("executeTime");
		long timetounban = time - System.currentTimeMillis();
		long seconds = timetounban / 1000;

		if (timetounban < 0) {
		    BanUtils.unban(target);
		    sender.sendMessage("§cThis player is not banned at all!");
		    return false;
		}

		if (System.currentTimeMillis() > executeTime) {
		    BanUtils.ban(Bukkit.getConsoleSender(), target, reason, time, 0);
		}

		long s = seconds % 60;
		long m = (seconds / 60) % 60;
		long h = (seconds / (60 * 60)) % 24;

		sender.sendMessage("§c---------------------");
		sender.sendMessage("§4The player " + target.getName());
		sender.sendMessage("§cwith the uuid " + uuid);
		sender.sendMessage("§chas been banned for " + reason);
		sender.sendMessage("§cand will be unbanned in " + h + ":" + m + ":" + s + ":");
		sender.sendMessage("§cDate: " + date);
		sender.sendMessage("§c---------------------");
	    }
	}
	return false;
    }
}
