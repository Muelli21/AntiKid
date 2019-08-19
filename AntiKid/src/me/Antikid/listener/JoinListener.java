package me.Antikid.listener;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Antikid.managers.YamlFileManager;
import me.Antikid.types.MoveTrail;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.BanUtils;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = new PlayerData(p);
	pd.setLastOnGround(p.getLocation());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(p);
	pd.delete();
	MoveTrail.trails.remove(p);
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {

	String uuid = e.getUniqueId().toString();
	OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(e.getUniqueId());

	YamlFileManager fileManager = new YamlFileManager();
	YamlConfiguration file = fileManager.loadFile("plugins/PlayerFiles", uuid);

	if (file == null || !file.getBoolean("ban") || !(file.getLong("executeTime") != 0)) { return; }

	String reason = file.getString("banReason");
	long executeTime = file.getLong("executeTime");
	long time = file.getLong("banTime");
	long timetounban = time - System.currentTimeMillis();
	long seconds = timetounban / 1000;

	long s = seconds % 60;
	long m = (seconds / 60) % 60;
	long h = (seconds / (60 * 60)) % 24;

	if (System.currentTimeMillis() > executeTime) {
	    e.disallow(Result.KICK_BANNED, "§cYou are banned for " + reason + " and will be unbanned in " + h + ":" + m + ":" + s);
	    BanUtils.ban(Bukkit.getConsoleSender(), offlinePlayer, reason, time, 0);
	}

	if (timetounban < 0) {
	    BanUtils.unban(offlinePlayer);
	    return;
	}

	e.disallow(Result.KICK_BANNED, "§cYou are banned for " + reason + " and will be unbanned in " + h + ":" + m + ":" + s);
	return;
    }
}
