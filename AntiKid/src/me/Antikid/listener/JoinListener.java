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

import me.Antikid.main.Main;
import me.Antikid.types.MoveTrail;
import me.Antikid.types.PlayerData;
import me.Antikid.types.SavedDataManager;
import me.Antikid.types.YamlFileManager;

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
	PlayerData pd = Main.getPlayerData(p);
	pd.delete();
	MoveTrail.trails.remove(p);
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {

	String uuid = e.getUniqueId().toString();
	OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(e.getUniqueId());

	YamlFileManager fileManager = new YamlFileManager();
	YamlConfiguration file = fileManager.loadFile("plugins/PlayerFiles", uuid);

	if (file == null || !file.getBoolean("ban")) { return; }

	String reason = file.getString("banReason");
	long time = file.getLong("banTime");
	long timetounban = time - System.currentTimeMillis();
	long seconds = timetounban / 1000;

	if (timetounban < 0) {
	    SavedDataManager.unban(offlinePlayer);
	    return;
	}

	long s = seconds % 60;
	long m = (seconds / 60) % 60;
	long h = (seconds / (60 * 60)) % 24;

	e.disallow(Result.KICK_BANNED, "§cYou are banned for " + reason + " and will be unbanned in " + h + ":" + m + ":" + s);
	return;
    }
}
