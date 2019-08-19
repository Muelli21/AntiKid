package me.Antikid.types;

import org.bukkit.entity.Player;

public class OfflinePlayerData {

    private transient Player p;
    private String uuid;
    private boolean ban;
    private String banReason;
    private long banTime;
    private boolean mute;
    private long muteTime;
    private String muteReason;
    private long futureBanTime;

    public OfflinePlayerData(Player p) {
	this.p = p;
	this.uuid = p.getUniqueId().toString();

	PlayerData pd = PlayerData.getPlayerData(p);
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

    public long getFutureBanTime() {
	return futureBanTime;
    }

    public void setFutureBanTime(long futureBanTime) {
	this.futureBanTime = futureBanTime;
    }
}
