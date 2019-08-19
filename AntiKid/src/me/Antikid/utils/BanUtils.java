package me.Antikid.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.Antikid.AntikidData;
import me.Antikid.managers.YamlFileManager;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;

public class BanUtils {

    public static void ban(OfflinePlayer offlinePlayer, String reason, long time, String dateString, long executeTime) {
	YamlFileManager fileManager = new YamlFileManager();
	YamlConfiguration file = fileManager.loadFile("plugins/PlayerFiles", offlinePlayer.getUniqueId().toString());

	file.set("ban", true);
	file.set("banReason", reason);
	file.set("banTime", time);
	file.set("banDate", dateString);
	file.set("executeTime", executeTime);
	fileManager.save();
    }

    public static void unban(OfflinePlayer offlinePlayer) {
	YamlFileManager fileManager = new YamlFileManager();
	YamlConfiguration file = fileManager.loadFile("plugins/PlayerFiles", offlinePlayer.getUniqueId().toString());

	file.set("ban", false);
	file.set("banReason", null);
	file.set("banTime", null);
	file.set("banDate", null);
	file.set("executeTime", null);
	fileManager.save();
    }

    public static void unban(CommandSender sender, OfflinePlayer target) {

	YamlFileManager fileManager = new YamlFileManager();
	YamlConfiguration file = fileManager.loadFile("plugins/PlayerFiles", target.getUniqueId().toString());

	if (!file.getBoolean("ban")) {
	    sender.sendMessage("§cThis player is not banned at all!");
	    return;
	}

	unban(target);
	sender.sendMessage("§aThe Player " + target.getName() + " has been unbanned!");
    }

    public static void ban(CommandSender sender, OfflinePlayer target, String reason, long banTime, long executeTime) {

	if (target.isOnline()) {
	    Player onlineTarget = (Player) target;
	    PlayerData td = PlayerData.getPlayerData(onlineTarget);
	    if (td.isBanned()) { return; }
	}

	if (!AntikidData.getData().isBanning()) {
	    banalert(sender, target, reason);
	    return;
	}

	LocalDateTime date = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	String dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

	if (System.currentTimeMillis() < executeTime) {
	    ban(target, reason, (banTime - System.currentTimeMillis()) + executeTime, dateString, executeTime);
	    return;
	}

	ban(target, reason, banTime, dateString, executeTime);

	Bukkit.broadcastMessage(" ");
	Bukkit.broadcastMessage("§c----------------------------------");
	Bukkit.broadcastMessage("§c" + sender.getName() + " banned player " + target.getName());
	Bukkit.broadcastMessage("§cfor " + reason);
	Bukkit.broadcastMessage("§c----------------------------------");
	Bukkit.broadcastMessage(" ");

	sender.sendMessage("§aYou have successfully banned the player " + target.getName() + " for " + reason);

	if (target.isOnline()) {
	    Player onlineTarget = (Player) target;
	    PlayerData td = PlayerData.getPlayerData(onlineTarget);
	    td.setBanned(true);
	    Bukkit.getPlayer(target.getUniqueId()).kickPlayer("§cYou have been banned from this server for " + reason + " !");
	}
    }

    public static void ban(CommandSender sender, OfflinePlayer target, BanReason reason) {
	ban(sender, target, reason.getName(), System.currentTimeMillis() + reason.getBanTime(), 0);
    }

    public static void delayedBan(CommandSender sender, OfflinePlayer target, BanReason reason, long delayInSeconds) {
	ban(sender, target, reason.getName(), System.currentTimeMillis() + reason.getBanTime(), System.currentTimeMillis() + delayInSeconds * 1000);
    }

    public static void banalert(CommandSender sender, OfflinePlayer target, String reason) {
	Bukkit.broadcastMessage("§c" + sender.getName() + " would have banned player " + target.getName());
	Bukkit.broadcastMessage("§cfor " + reason);
    }
}
