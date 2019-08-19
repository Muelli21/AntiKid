package me.Antikid.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Antikid.utils.BanUtils;

public class TempBanCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!sender.hasPermission("Staff")) { return false; }
	if (args.length != 4) {
	    sender.sendMessage(ChatColor.RED + "Please use /tempban [Name] [time] [timeunit] [Reason]!");
	    return false;
	}

	if (Bukkit.getOfflinePlayer(args[0]) == null) {
	    sender.sendMessage(ChatColor.RED + "This player does not exist!");
	    return false;
	}

	OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
	String reason = args[3];

	if (args[2].equalsIgnoreCase("d")) {

	    int time = (Integer.parseInt(args[1]) * 24 * 60 * 60 * 1000);
	    long timetounban = System.currentTimeMillis() + (time);
	    BanUtils.ban(sender, target, reason, timetounban, 0);
	    return false;

	} else

	if (args[2].equalsIgnoreCase("h")) {

	    int time = (Integer.parseInt(args[1]) * 60 * 60 * 1000);
	    long timetounban = System.currentTimeMillis() + (time);
	    BanUtils.ban(sender, target, reason, timetounban, 0);
	    return false;

	} else

	if (args[2].equalsIgnoreCase("m")) {

	    int time = (Integer.parseInt(args[1]) * 60 * 1000);
	    long timetounban = System.currentTimeMillis() + (time);
	    BanUtils.ban(sender, target, reason, timetounban, 0);
	    return false;

	} else

	if (args[2].equalsIgnoreCase("s")) {

	    int time = (Integer.parseInt(args[1]) * 1000);
	    long timetounban = System.currentTimeMillis() + (time);
	    BanUtils.ban(sender, target, reason, timetounban, 0);
	    return false;

	} else
	    sender.sendMessage(ChatColor.RED + "Please use for the timeunit d/h/m/s!");
	return false;
    }
}
