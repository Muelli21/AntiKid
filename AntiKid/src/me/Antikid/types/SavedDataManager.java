package me.Antikid.types;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.Antikid.main.Main;

public class SavedDataManager {

    private transient Player p;
    private String uuid;
    private boolean ban;
    private String banReason;
    private long banTime;
    private boolean mute;
    private long muteTime;
    private String muteReason;

    public SavedDataManager(Player p) {
	this.p = p;
	this.uuid = p.getUniqueId().toString();

	PlayerData pd = Main.getPlayerData(p);
	pd.setDataManager(this);
    }

    public boolean isMuted() {
	return mute;
    }

    public void setMuted(boolean mute) {
	this.mute = mute;
    }

    public boolean getBan() {
	return ban;
    }

    public void setBan(boolean ban) {
	this.ban = ban;
    }

    public String getUuid() {
	return uuid;
    }

    public void setUuid(String uuid) {
	this.uuid = uuid;
    }

    public String getBanReason() {
	return banReason;
    }

    public void setBanReason(String banReason) {
	this.banReason = banReason;
    }

    public long getBanTime() {
	return banTime;
    }

    public void setBanTime(long banTime) {
	this.banTime = banTime;
    }

    public Player getPlayer() {
	return p;
    }

    public long getMuteTime() {
	return muteTime;
    }

    public void setMuteTime(long muteTime) {
	this.muteTime = muteTime;
    }

    public String getMuteReason() {
	return muteReason;
    }

    public void setMuteReason(String muteReason) {
	this.muteReason = muteReason;
    }

    public static void ban(OfflinePlayer offlinePlayer, String reason, long time, String dateString) {
	YamlFileManager fileManager = new YamlFileManager();
	YamlConfiguration file = fileManager.loadFile("plugins/PlayerFiles", offlinePlayer.getUniqueId().toString());

	file.set("ban", true);
	file.set("banReason", reason);
	file.set("banTime", time);
	file.set("banDate", dateString);
	fileManager.save();
    }

    public static void unban(OfflinePlayer offlinePlayer) {
	YamlFileManager fileManager = new YamlFileManager();
	YamlConfiguration file = fileManager.loadFile("plugins/PlayerFiles", offlinePlayer.getUniqueId().toString());

	file.set("ban", false);
	file.set("banReason", null);
	file.set("banTime", null);
	file.set("banDate", null);
	fileManager.save();
    }
}
