package me.Antikid;

import me.Antikid.types.NettyInjection;

public class AntikidData {

    private long lastban;
    private boolean ban = true;
    private Antikid plugin;
    private NettyInjection nettyInjection;

    public AntikidData(Antikid antikid) {
	this.plugin = antikid;
    }

    public long getLastban() {
	return lastban;
    }

    public void setLastban(long lastban) {
	this.lastban = lastban;
    }

    public boolean isBanning() {
	return ban;
    }

    public void setBanning(boolean ban) {
	this.ban = ban;
    }

    public Antikid getPlugin() {
	return plugin;
    }

    public NettyInjection getNettyInjection() {
	return nettyInjection;
    }

    public void setNettyInjection(NettyInjection nettyInjection) {
	this.nettyInjection = nettyInjection;
    }

    public void toggleBan() {
	setBanning(isBanning());
    }

    public static AntikidData getData() {
	return Antikid.antikidData;
    }
}
