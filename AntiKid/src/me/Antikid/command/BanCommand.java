package me.Antikid.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Antikid.gui.BanGui;
import me.Antikid.types.BanReason;
import me.Antikid.utils.BanUtils;

public class BanCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	if (!sender.hasPermission("Staff")) { return false; }

	if (Bukkit.getOfflinePlayer(args[0]) == null) {
	    sender.sendMessage(ChatColor.RED + "This player does not exist!");
	    return false;
	}

	OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

	if (args.length == 1) {

	    Player p = (Player) sender;
	    if (sender instanceof Player) {
		new BanGui(p, target);
		return false;
	    }
	}

	if (args.length == 2) {
	    if (args[1] == null) {
		sender.sendMessage(ChatColor.RED + "Please use /ban [Name] [Reason]!");
		return false;
	    }

	    String reason = args[1];
	    BanUtils.ban(sender, target, reason, BanReason.PERMANENT.getBanTime(), 0);

	} else
	    sender.sendMessage(ChatColor.RED + "Please use /ban [Name] [Reason]!");
	return false;
    }
}
