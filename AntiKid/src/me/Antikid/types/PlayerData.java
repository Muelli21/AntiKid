package me.Antikid.types;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;

import me.Antikid.Antikid;
import me.Antikid.gui.AbstractGui;
import me.Antikid.managers.ViolationManager;
import net.minecraft.server.v1_7_R4.MinecraftServer;

public class PlayerData {

    public static HashMap<Player, PlayerData> playerdatas = new HashMap<>();

    private Player player, lastDamager = this.getPlayer();
    private boolean frozen = false;
    private boolean debug = false;

    private Block lastBrokenBlock;
    private long lastBreakTime;

    private long joinTick;
    private boolean joinTickSet = false;
    private long joinTime = System.currentTimeMillis();

    private Location frozenloc;
    private long lastReport;
    private int clicks = 0;

    private int cps = 0;

    private long lastReach;
    private int movepackets = 0;
    private int totalMovepackets = 0;
    private long healtime;

    private ClickActions hits = new ClickActions();

    private int speedamplifier = 0;

    private long jumpcooldown;
    private long icecooldown;
    private long blockcooldown;
    private long velocitycooldown;
    private long flycooldown;
    private long normalcooldown;
    private long Speedcooldown;
    private long Hitcooldown;
    private Location lastOnGround;
    private long sprintcooldown;
    private long sneakCooldown;
    private long walkSpeedcooldown;
    private double lastWalkSpeed;

    private double falldistance;
    private long onGroundcooldown;

    private FakeEntity fakeplayer;
    private boolean fakeplayervisible = false;
    private boolean velocityCheck = false;
    private SittingReachCheck sittingCheck;
    private OfflinePlayerData dataManager;

    private PacketType packetType;
    private long packetTick;
    private HashMap<PacketType, AtomicInteger> packetAmount = new HashMap<>();
    private HashMap<PacketType, Long> lastPacketResetTime = new HashMap<>();

    private Roast roast;
    private ViolationManager violationManager;

    private Vector lastVelocity;
    private Location lastServerSidedPosition;
    private boolean hit;
    private boolean banned;

    private AbstractGui currentGui;

    public PlayerData(Player player) {
	this.player = player;
	this.roast = new Roast(player);
	this.lastOnGround = player.getLocation();
	this.violationManager = new ViolationManager(player);
	playerdatas.put(player, this);
    }

    public void delete() {
	new BukkitRunnable() {
	    @Override
	    public void run() {
		playerdatas.remove(player);
	    }
	}.runTaskLater(Antikid.getPlugin(), 5 * 20);
    }

    public void report() {
	this.lastReport = System.currentTimeMillis();
    }

    public long getLastReport() {
	return this.lastReport;
    }

    public Player getLastDamager() {
	return this.lastDamager;
    }

    public void setLastDamager(Player p) {
	this.lastDamager = p;
    }

    public void setFrozen(boolean mode) {
	this.frozen = mode;
    }

    public Boolean isFrozen() {
	return this.frozen;
    }

    public Player getPlayer() {
	return this.player;
    }

    public void setFrozenLocation(Location loc) {
	this.frozenloc = loc;
    }

    public Location getFrozenLocation() {
	return this.frozenloc;
    }

    public int getClicks() {
	return clicks;
    }

    public void addClicks() {
	clicks++;
    }

    public void setClicks(int clicks) {
	this.clicks = clicks;
    }

    public long getFlycooldown() {
	return flycooldown;
    }

    public void setFlycooldown(long delay) {
	this.flycooldown = System.currentTimeMillis() + delay;
    }

    public int getCps() {
	return cps;
    }

    public void addCps() {
	cps++;
    }

    public long getJumpcooldown() {
	return jumpcooldown;
    }

    public void setJumpcooldown(long jumpcooldown) {
	this.jumpcooldown = System.currentTimeMillis() + jumpcooldown;
    }

    public long getIcecooldown() {
	return icecooldown;
    }

    public void setIcecooldown(long icecooldown) {
	this.icecooldown = System.currentTimeMillis() + icecooldown;
    }

    public long getBlockcooldown() {
	return blockcooldown;
    }

    public void setBlockcooldown(long blockcooldown) {
	this.blockcooldown = System.currentTimeMillis() + blockcooldown;
    }

    public long getVelocitycooldown() {
	return velocitycooldown;
    }

    public void setVelocitycooldown(long velocitycooldown) {
	if (System.currentTimeMillis() + velocitycooldown + 100 < this.velocitycooldown)
	    return;
	this.velocitycooldown = System.currentTimeMillis() + velocitycooldown;
    }

    public long getNormalcooldown() {
	return normalcooldown;
    }

    public void setNormalcooldown(long normalcooldown) {
	this.normalcooldown = System.currentTimeMillis() + normalcooldown;
    }

    public long getSpeedcooldown() {
	return Speedcooldown;
    }

    public void setSpeedcooldown(long speedcooldown) {
	Speedcooldown = System.currentTimeMillis() + speedcooldown;
    }

    public int getSpeedamplifier() {
	return speedamplifier;
    }

    public void setSpeedamplifier(int speedamplifier) {
	this.speedamplifier = speedamplifier;
    }

    public long getHitcooldown() {
	return Hitcooldown;
    }

    public void setHitcooldown(long hitcooldown) {
	Hitcooldown = System.currentTimeMillis() + hitcooldown;
    }

    public Location getLastOnGround() {
	return lastOnGround;
    }

    public void setLastOnGround(Location lastOnGround) {
	this.lastOnGround = lastOnGround;
    }

    public int getMovepackets() {
	return movepackets;
    }

    public void setMovepackets(int movepackets) {
	this.movepackets = movepackets;
    }

    public void addMovepacket() {
	movepackets++;
    }

    public boolean isDebug() {
	return debug;
    }

    public void setDebug(boolean debug) {
	this.debug = debug;
    }

    public long getHealtime() {
	return healtime;
    }

    public void setHealtime(long healtime) {
	this.healtime = healtime;
    }

    public long getSprintcooldown() {
	return sprintcooldown;
    }

    public void setSprintcooldown(long sprintcooldown) {
	this.sprintcooldown = System.currentTimeMillis() + sprintcooldown;
    }

    public double getFalldistance() {
	return falldistance;
    }

    public void setFalldistance(double falldistance) {
	this.falldistance = falldistance;
    }

    public long getOnGroundcooldown() {
	return onGroundcooldown;
    }

    public void setOnGroundcooldown(long onGroundcooldown) {
	this.onGroundcooldown = System.currentTimeMillis() + onGroundcooldown;
    }

    public FakeEntity getFakeplayer() {
	return fakeplayer;
    }

    public void setFakeplayer(FakeEntity fakeplayer) {
	this.fakeplayer = fakeplayer;
    }

    public boolean isFakeplayervisible() {
	return fakeplayervisible;
    }

    public void setFakeplayervisible(boolean fakeplayervisible) {
	this.fakeplayervisible = fakeplayervisible;
    }

    public boolean isVelocityCheck() {
	return velocityCheck;
    }

    public void setVelocityCheck(boolean velocityCheck) {
	this.velocityCheck = velocityCheck;
    }

    public SittingReachCheck getSittingCheck() {
	return sittingCheck;
    }

    public void setSittingCheck(SittingReachCheck sittingCheck) {
	this.sittingCheck = sittingCheck;
    }

    public void setCps(int cps) {
	this.cps = cps;
    }

    public OfflinePlayerData getDataManager() {
	return dataManager;
    }

    public void setDataManager(OfflinePlayerData dataManager) {
	this.dataManager = dataManager;
    }

    public Block getLastBrokenBlock() {
	return lastBrokenBlock;
    }

    public void setLastBrokenBlock(Block lastBrokenBlock) {
	this.lastBrokenBlock = lastBrokenBlock;
    }

    public Long getLastBreakTime() {
	return lastBreakTime;
    }

    public void setLastBreakTime(Long lastBreakTime) {
	this.lastBreakTime = lastBreakTime;
    }

    public ClickActions getHits() {
	return hits;
    }

    public void setHits(ClickActions hits) {
	this.hits = hits;
    }

    public Roast getRoast() {
	return roast;
    }

    public void setRoast(Roast roast) {
	this.roast = roast;
    }

    public long getLastReach() {
	return lastReach;
    }

    public void setLastReach(long lastReach) {
	this.lastReach = lastReach;
    }

    public Vector getLastVelocity() {
	return lastVelocity;
    }

    public void setLastVelocity(Vector lastVelocity) {
	this.lastVelocity = lastVelocity;
    }

    public Location getLastServerSidedPosition() {
	return lastServerSidedPosition;
    }

    public void setLastServerSidedPosition(Location lastknown) {
	this.lastServerSidedPosition = lastknown;
    }

    public boolean isHit() {
	return hit;
    }

    public void setHit(boolean hit) {
	this.hit = hit;
    }

    public long getJoinTick() {
	return joinTick;
    }

    public void setJoinTick() {
	this.joinTick = MinecraftServer.currentTick;
    }

    public long ticksOnline() {
	return MinecraftServer.currentTick - joinTick;
    }

    public long msOnline() {
	return System.currentTimeMillis() - joinTime;
    }

    public PacketType getPacketType() {
	return packetType;
    }

    public void setPacketType(PacketType packetType) {
	this.packetType = packetType;
    }

    public int getTotalMovepackets() {
	return totalMovepackets;
    }

    public void setTotalMovepackets(int totalMovepackets) {
	this.totalMovepackets = totalMovepackets;
    }

    public void addTotalMovepacket() {
	totalMovepackets++;
    }

    public boolean isJoinTickSet() {
	return joinTickSet;
    }

    public void setJoinTickSet(boolean joinTickSet) {
	this.joinTickSet = joinTickSet;
    }

    public long getJoinTime() {
	return joinTime;
    }

    public void setJoinTime(long joinTime) {
	this.joinTime = joinTime;
    }

    public long getPacketTick() {
	return packetTick;
    }

    public void setPacketTick(long packetTick) {
	this.packetTick = packetTick;
    }

    public long getWalkSpeedcooldown() {
	return walkSpeedcooldown;
    }

    public void setWalkSpeedcooldown(long walkSpeedcooldown) {
	this.walkSpeedcooldown = walkSpeedcooldown;
    }

    public double getLastWalkSpeed() {
	return lastWalkSpeed;
    }

    public void setLastWalkSpeed(double lastWalkSpeed) {
	this.lastWalkSpeed = System.currentTimeMillis() + lastWalkSpeed;
    }

    public long getSneakCooldown() {
	return sneakCooldown;
    }

    public void setSneakCooldown(long sneakCooldown) {
	this.sneakCooldown = System.currentTimeMillis() + sneakCooldown;
    }

    public HashMap<PacketType, AtomicInteger> getPacketAmount() {
	return packetAmount;
    }

    public void setPacketAmount(HashMap<PacketType, AtomicInteger> packetAmount) {
	this.packetAmount = packetAmount;
    }

    public HashMap<PacketType, Long> getLastPacketResetTime() {
	return lastPacketResetTime;
    }

    public void setLastPacketResetTime(HashMap<PacketType, Long> lastPacketResetTime) {
	this.lastPacketResetTime = lastPacketResetTime;
    }

    public boolean isBanned() {
	return banned;
    }

    public void setBanned(boolean banned) {
	this.banned = banned;
    }

    public AbstractGui getCurrentGui() {
	return currentGui;
    }

    public void setCurrentGui(AbstractGui currentGui) {
	this.currentGui = currentGui;
    }

    public static PlayerData getPlayerData(Player player) {
	PlayerData playerData = PlayerData.playerdatas.get(player);
	return playerData;
    }

    public ViolationManager getViolationManager() {
	return violationManager;
    }

    public static HashMap<Player, PlayerData> getPlayerDatas() {
	return playerdatas;
    }
}
