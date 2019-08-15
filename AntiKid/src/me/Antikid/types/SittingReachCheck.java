package me.Antikid.types;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.Antikid.main.Main;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class SittingReachCheck {

    private Player p;
    private Entity toSitOn;
    private Location loc;
    private FakeEntity entity;
    private BukkitTask task;

    public SittingReachCheck(Player p, Entity toSitOn, long time) {
	PlayerData pd = Main.getPlayerData(p);
	Location loc = p.getLocation();
	Location spawnLoc = loc.add(0, 1, 0);
	Player random = Utils.getRandomPlayer(p);
	CraftPlayer crandom = (CraftPlayer) random;
	EntityPlayer erandom = (EntityPlayer) crandom.getHandle();
	GameProfile profile = erandom.getProfile();
	FakeEntity entity = new FakeEntity(p, profile, spawnLoc);

	pd.setSittingCheck(this);
	toSitOn.teleport(spawnLoc);
	toSitOn.setPassenger(p);

	BukkitTask task = new BukkitRunnable() {

	    @Override
	    public void run() {
		toSitOn.teleport(spawnLoc);
		Vector moveVector = Utils.getDirectionVector(0, p.getLocation().getYaw());
		moveVector.normalize().multiply(3.41);
		Location move = p.getLocation().add(Utils.getDirectionVector(0, p.getLocation().getYaw()));
		entity.move(p.getLocation().getYaw(), p.getLocation().getPitch(), true, move, false);
	    }
	}.runTaskTimer(Main.getPlugin(), 0, time);
	this.setTask(task);
	this.entity = entity;
    }

    public void stop(boolean killEntity) {
	PlayerData pd = Main.getPlayerData(p);
	pd.setSittingCheck(null);
	toSitOn.setPassenger(null);
	task.cancel();
	entity.kill();

	if (killEntity) {
	    final WorldServer world = ((CraftWorld) p.getWorld()).getHandle();
	    world.removeEntity(((CraftEntity) toSitOn).getHandle());
	}

	this.toSitOn = null;
	this.entity = null;
	this.loc = null;
	this.p = null;
    }

    public void ban() {
	new BukkitRunnable() {

	    @Override
	    public void run() {
		int time = (30 * 24 * 60 * 60 * 1000);
		long timetounban = System.currentTimeMillis() + (time);
		BanUtils.ban(Bukkit.getConsoleSender(), p, "reach", timetounban);
		return;
	    }
	}.runTaskLater(Main.getPlugin(), 30 * 20);
    }

    public Player getPlayer() {
	return p;
    }

    public Entity getEntity() {
	return toSitOn;
    }

    public void setEntity(Entity toSitOn) {
	this.toSitOn = toSitOn;
    }

    public Location getLoc() {
	return loc;
    }

    public void setLoc(Location loc) {
	this.loc = loc;
    }

    public FakeEntity getFakeEntity() {
	return entity;
    }

    public void setFakeEntity(FakeEntity entity) {
	this.entity = entity;
    }

    public BukkitTask getTask() {
	return task;
    }

    public void setTask(BukkitTask task) {
	this.task = task;
    }
}
