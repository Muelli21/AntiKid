package me.Antikid.types;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Antikid.main.Main;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutAnimation;
import net.minecraft.server.v1_7_R4.PacketPlayOutBed;
import net.minecraft.server.v1_7_R4.PacketPlayOutCollect;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityLook;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityVelocity;
import net.minecraft.server.v1_7_R4.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_7_R4.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class FakeEntity {

    private Player p;
    private GameProfile profile;
    private EntityPlayer ep;
    private Location loc;

    private boolean dead, fresh;
    private int refX, refY, refZ, refPitch, refYaw;
    private long removetime = 0;

    public FakeEntity(Player p, GameProfile profile, Location loc) {

	this.profile = profile;
	this.p = p;

	this.ep = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) loc.getWorld()).getHandle(), profile,
		new PlayerInteractManager(((CraftWorld) loc.getWorld()).getHandle()));

	DataWatcher watcher = new DataWatcher(ep);
	watcher.a(0, Byte.valueOf((byte) 0));
	watcher.a(1, Short.valueOf((short) 0));
	watcher.a(8, Byte.valueOf((byte) 0));
	ep.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());

	PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(ep);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawn);
	refX = (int) (loc.getX() * 32);
	refY = (int) (loc.getY() * 32);
	refZ = (int) (loc.getZ() * 32);
	refPitch = (int) (loc.getPitch() * 32);
	refYaw = (int) (loc.getYaw() * 32);
	fresh = true;
	move(loc.getYaw(), loc.getPitch(), ep.onGround, loc, true);
	fresh = false;
    }

    public EntityPlayer getHuman() {
	return ep;
    }

    private int getCompressedAngle(float value) {
	return MathHelper.d(value * 256.0F / 360.0F);
    }

    public void kill() {
	PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ep.getId());
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public void updateItems(ItemStack hand) {
	PacketPlayOutEntityEquipment ps = new PacketPlayOutEntityEquipment(ep.getId(), 0, CraftItemStack.asNMSCopy(hand));

	((CraftPlayer) p).getHandle().playerConnection.sendPacket(ps);
    }

    public void setName(String s) {
	DataWatcher watcher = ep.getDataWatcher();
	watcher.watch(10, s);

	PacketPlayOutEntityMetadata packetmetadata = new PacketPlayOutEntityMetadata(ep.getId(), watcher, true);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetmetadata);
    }

    public void hideForPlayer(Player p) {
	DataWatcher watcher = new DataWatcher(ep);
	watcher.a(0, Byte.valueOf((byte) 32));
	watcher.a(1, Short.valueOf((short) 0));
	watcher.a(8, Byte.valueOf((byte) 0));
	PacketPlayOutEntityMetadata packetmetadata = new PacketPlayOutEntityMetadata(ep.getId(), watcher, true);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetmetadata);
    }

    public void showForPlayer(Player p) {
	DataWatcher watcher = new DataWatcher(ep);
	watcher.a(0, Byte.valueOf((byte) 0));
	watcher.a(1, Short.valueOf((short) 0));
	watcher.a(8, Byte.valueOf((byte) 0));
	PacketPlayOutEntityMetadata packetmetadata = new PacketPlayOutEntityMetadata(ep.getId(), watcher, true);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetmetadata);
    }

    public void addPotionColor(Color r) {
	int color = r.asBGR();
	DataWatcher dw = new DataWatcher(ep);
	dw.a(7, Integer.valueOf(color));
	PacketPlayOutEntityMetadata packetmetadata = new PacketPlayOutEntityMetadata(ep.getId(), dw, true);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetmetadata);
    }

    public void addPotionColor(int color) {
	DataWatcher dw = new DataWatcher(ep);
	dw.a(7, Integer.valueOf(color));
	PacketPlayOutEntityMetadata packetmetadata = new PacketPlayOutEntityMetadata(ep.getId(), dw, true);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetmetadata);
    }

    public void move(float yaw, float pitch, boolean onGround, Location real, boolean tickFlag) {
	ep.setLocation(real.getX(), real.getY(), real.getZ(), yaw, pitch);
	int a = ep.as.a(real.getX());
	int b = (int) (real.getY() * 32D);
	int c = ep.as.a(real.getZ());

	int x = a - refX;
	int y = b - refY;
	int z = c - refZ;

	int rotYaw = getCompressedAngle(yaw);
	int rotPitch = getCompressedAngle(pitch);

	boolean moveFlag = Math.abs(x) >= 1 || Math.abs(y) >= 1 || Math.abs(z) >= 1 || tickFlag;
	boolean rotFlag = Math.abs(rotYaw - refYaw) >= 1 || Math.abs(rotPitch - refPitch) >= 1;

	Packet packet = null;
	if (moveFlag || rotFlag && (x >= -128) && (x < 128) && (y >= -128) && (y < 128) && (z >= -128) && (z < 128) || fresh) {
	    if (moveFlag && rotFlag || dead) {
		packet = new PacketPlayOutRelEntityMoveLook(ep.getId(), (byte) x, (byte) y, (byte) z, (byte) rotYaw, (byte) rotPitch, onGround);
	    } else if (moveFlag) {
		packet = new PacketPlayOutRelEntityMove(ep.getId(), (byte) x, (byte) y, (byte) z, onGround);
	    } else if (rotFlag) {
		packet = new PacketPlayOutEntityLook(ep.getId(), (byte) rotYaw, (byte) rotPitch, onGround);
	    } else {
		try {
		    throw new Exception("Error while ticking Human walk, passed through all flags...");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	} else {
	    packet = new PacketPlayOutEntityTeleport(ep);
	}
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);

	if (rotFlag || fresh) {
	    PacketPlayOutEntityHeadRotation p2 = new PacketPlayOutEntityHeadRotation(ep, (byte) rotYaw);
	    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(p2);
	}

	if (rotFlag) {
	    refYaw = rotYaw;
	    refPitch = rotPitch;
	}

	if (moveFlag) {
	    refX = a;
	    refY = b;
	    refZ = c;
	}
	this.loc = real;
    }

    public void setInvisible() {
	DataWatcher watcher = ep.getDataWatcher();
	watcher.watch(0, 32);
	PacketPlayOutEntityMetadata packetmetadata = new PacketPlayOutEntityMetadata(ep.getId(), watcher, true);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetmetadata);
    }

    public void setCrouched() {
	DataWatcher watcher = ep.getDataWatcher();
	watcher.watch(0, Byte.valueOf((byte) 2));
	PacketPlayOutEntityMetadata packetmetadata = new PacketPlayOutEntityMetadata(ep.getId(), watcher, true);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetmetadata);
    }

    public void reset() {
	DataWatcher watcher = ep.getDataWatcher();
	watcher.watch(0, Byte.valueOf((byte) 0));
	PacketPlayOutEntityMetadata packetmetadata = new PacketPlayOutEntityMetadata(ep.getId(), watcher, true);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetmetadata);
    }

    public void sprint() {
	DataWatcher watcher = ep.getDataWatcher();
	watcher.watch(0, Byte.valueOf((byte) 8));
	PacketPlayOutEntityMetadata packetmetadata = new PacketPlayOutEntityMetadata(ep.getId(), watcher, true);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetmetadata);
    }

    public void block() {
	DataWatcher watcher = ep.getDataWatcher();
	watcher.watch(0, Byte.valueOf((byte) 16));
	PacketPlayOutEntityMetadata packetmetadata = new PacketPlayOutEntityMetadata(ep.getId(), watcher, true);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetmetadata);
    }

    public void death() {
	PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus(ep, (byte) 3);
	this.dead = true;
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    public void crit() {
	PacketPlayOutAnimation packetanimation = new PacketPlayOutAnimation(ep, 4);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetanimation);
    }

    public void damage() {
	PacketPlayOutAnimation packetanimation = new PacketPlayOutAnimation(ep, 1);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetanimation);
    }

    public void swingArm() {
	PacketPlayOutAnimation packetanimation = new PacketPlayOutAnimation(ep, 0);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetanimation);
    }

    public void eatInHand() {
	PacketPlayOutAnimation packetanimation = new PacketPlayOutAnimation(ep, 5);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packetanimation);
    }

    public void collect(Item item) {

	PacketPlayOutCollect packet = new PacketPlayOutCollect(item.getEntityId(), ep.getId());
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	item.remove();
    }

    public void sleep() {
	PacketPlayOutBed packet17 = new PacketPlayOutBed(ep, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet17);
    }

    public void setVelocity(Vector vel) {
	vel.multiply(1.3);
	double initialY = loc.getY();
	ep.motX = vel.getX();
	ep.motY = vel.getY();
	ep.motZ = vel.getZ();
	ep.onGround = false;
	new BukkitRunnable() {
	    int i = 0;

	    @Override
	    public void run() {
		if (ep.motY < 0.1) {
		    ep.motY = -0.2;
		}

		loc.add(ep.motX, ep.motY, ep.motZ);
		if (loc.getY() < initialY) {
		    loc.setY(initialY);
		}

		move(ep.yaw, ep.pitch, ep.onGround, loc, true);
		ep.motX *= 0.8;
		ep.motY *= 0.8;
		ep.motZ *= 0.8;
		i++;
		if (i > 20)
		    cancel();
	    }
	}.runTaskTimer(Main.getPlugin(), 0, 1);
	PacketPlayOutEntityVelocity velPacket = new PacketPlayOutEntityVelocity(ep);
	((CraftPlayer) p).getHandle().playerConnection.sendPacket(velPacket);
    }

    public GameProfile getProfile() {
	return profile;
    }

    public EntityPlayer getEp() {
	return ep;
    }

    public long getRemovetime() {
	return removetime;
    }

    public void setRemovetime(long removetime) {
	this.removetime = System.currentTimeMillis() + removetime;
    }
}