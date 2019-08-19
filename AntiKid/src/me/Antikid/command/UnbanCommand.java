package me.Antikid.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Antikid.utils.BanUtils;

public class UnbanCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!sender.hasPermission("Staff")) { return false; }

	if (Bukkit.getOfflinePlayer(args[0]) == null) {
	    sender.sendMessage(ChatColor.RED + "This player does not exist!");
	    return false;
	}

	OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
	BanUtils.unban(sender, target);
	return false;
    }
}
