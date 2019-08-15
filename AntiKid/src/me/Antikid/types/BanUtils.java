package me.Antikid.types;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Antikid.main.Main;

public class BanUtils {

    public static void unban(CommandSender sender, OfflinePlayer target, String uuid) {

	YamlFileManager fileManager = new YamlFileManager();
	YamlConfiguration file = fileManager.loadFile("plugins/PlayerFiles", target.getUniqueId().toString());

	if (!file.getBoolean("ban")) {
	    sender.sendMessage("§cThis player is not banned at all!");
	    return;
	}

	SavedDataManager.unban(target);
	sender.sendMessage("§aThe Player " + target.getName() + " has been unbanned!");
    }

    public static void ban(CommandSender sender, OfflinePlayer target, String reason, long banTime) {

	if (target.isOnline()) {
	    Player onlineTarget = (Player) target;
	    PlayerData td = Main.getPlayerData(onlineTarget);
	    if (td.isBanned()) { return; }
	}

	if (!Main.ban) {
	    banalert(sender, target, reason);
	    return;
	}

	new BukkitRunnable() {

	    @Override
	    public void run() {

		LocalDateTime date = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();
		String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
		SavedDataManager.ban(target, reason, banTime, dateString);

		Bukkit.broadcastMessage(" ");
		Bukkit.broadcastMessage("§c----------------------------------");
		Bukkit.broadcastMessage("§c" + sender.getName() + " banned player " + target.getName());
		Bukkit.broadcastMessage("§cfor " + reason);
		Bukkit.broadcastMessage("§c----------------------------------");
		Bukkit.broadcastMessage(" ");

		sender.sendMessage("§aYou have successfully banned the player " + target.getName() + " for " + reason);

		if (target.isOnline()) {
		    Player onlineTarget = (Player) target;
		    PlayerData td = Main.getPlayerData(onlineTarget);
		    td.setBanned(true);
		    Bukkit.getPlayer(target.getUniqueId()).kickPlayer("§cYou have been banned from this server for " + reason + " !");
		}
	    }
	}.runTaskLater(Main.getPlugin(), 0);
    }

    public static void ban(CommandSender sender, OfflinePlayer target, BanReason reason) {
	ban(sender, target, reason.getName(), System.currentTimeMillis() + reason.getBanTime());
    }

    public static void delayedBan(CommandSender sender, OfflinePlayer target, BanReason reason, long delay) {
	new BukkitRunnable() {
	    @Override
	    public void run() {
		ban(sender, target, reason);
	    }
	}.runTaskLater(Main.getPlugin(), delay);
    }

    public static void banalert(CommandSender sender, OfflinePlayer target, String reason) {
	Bukkit.broadcastMessage("§c" + sender.getName() + " would have banned player " + target.getName());
	Bukkit.broadcastMessage("§cfor " + reason);
    }
}
