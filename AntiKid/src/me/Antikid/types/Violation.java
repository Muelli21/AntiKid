package me.Antikid.types;

import org.bukkit.entity.Player;

import me.Antikid.module.Module;
import me.Antikid.utils.PlayerUtils;

public class Violation {

    private Player player;
    private Module module;
    private long systemTime;
    private int tps;
    private int ping;
    private int violations = 0;
    private long lastViolation;

    public Violation(Player player, Module module) {
	this.player = player;
	this.module = module;
	this.systemTime = System.currentTimeMillis();
	this.ping = PlayerUtils.getPing(player);
	this.violations = 1;
	this.lastViolation = System.currentTimeMillis();
	PlayerData pd = PlayerData.getPlayerData(player);
	pd.getViolationManager().getViolations().put(module, this);
    }

    public void reset() {
	this.systemTime = System.currentTimeMillis();
	this.ping = PlayerUtils.getPing(player);
	this.violations = 0;
    }

    public void handle() {
	if (System.currentTimeMillis() > lastViolation + 60 * 1000) {
	    setViolations(getViolations() - 1);
	}
    }

    public Player getPlayer() {
	return player;
    }

    public Module getModule() {
	return module;
    }

    public long getSystemTime() {
	return systemTime;
    }

    public int getTps() {
	return tps;
    }

    public int getPing() {
	return ping;
    }

    public int getViolations() {
	return violations;
    }

    public void setViolations(int violations) {

	if (violations < 0) {
	    violations = 0;
	}

	if (violations != 0) {
	    this.lastViolation = System.currentTimeMillis();
	}

	this.violations = violations;
    }

    public long getLastViolation() {
	return lastViolation;
    }

    public void setLastViolation(long lastViolation) {
	this.lastViolation = lastViolation;
    }
}
