package me.Antikid.types;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Antikid.utils.BanUtils;

public class Roast {

    private Player player;
    private int roasts;
    private int roastsExperimental;
    private long lastRoast;

    public Roast(Player player) {
	this.player = player;
    }

    public int getRoasts() {
	return roasts;
    }

    public void setRoasts(int roasts) {
	this.roasts = roasts;
	setLastRoast();
    }

    public void addRoast() {
	roasts++;
	setLastRoast();
    }

    public void addRoasts(int roasts) {
	this.roasts = this.roasts + roasts;
	setLastRoast();
    }

    public int getRoastsExperimental() {
	return roastsExperimental;
    }

    public void setRoastsExperimental(int roastsExperimental) {
	this.roastsExperimental = roastsExperimental;
	setLastRoast();
    }

    public void addRoastsExperimental() {
	roastsExperimental++;
	setLastRoast();
    }

    public void addRoastsExperimental(int roasts) {
	this.roastsExperimental = this.roastsExperimental + roasts;
	setLastRoast();
    }

    public long getLastRoast() {
	return lastRoast;
    }

    public void setLastRoast() {
	this.lastRoast = System.currentTimeMillis();
    }

    public void resetRoasts() {
	this.roasts = 0;
	this.roastsExperimental = 0;
    }

    public void resetRoast() {
	this.roasts = 0;
    }

    public void resetRoastExperimental() {
	this.roastsExperimental = 0;
    }

    public void handleRoasts() {
	PlayerData pd = PlayerData.getPlayerData(player);
	long time = 5 * 60 * 1000;

	if (System.currentTimeMillis() - lastRoast > time) {
	    if (roasts > 0)
		roasts--;
	}

	if (System.currentTimeMillis() - lastRoast > time / 2) {
	    if (roastsExperimental > 0)
		roastsExperimental--;
	}

	if (getRoasts() >= 5 || getRoasts() + (getRoastsExperimental() / 2) >= 5 || getRoastsExperimental() >= 10) {
	    BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.UNFAIRADVANTAGE);
	    resetRoast();
	    resetRoastExperimental();
	}
    }
}
