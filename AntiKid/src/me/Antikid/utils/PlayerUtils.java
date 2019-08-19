package me.Antikid.utils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Antikid.types.PlayerData;
import net.minecraft.server.v1_7_R4.AxisAlignedBB;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MinecraftServer;

public class PlayerUtils implements Listener {

    static final double ON_GROUND = -0.0784000015258789;
    static final double JUMP = 0.41999998688697815;

    public static boolean isCreative(Player player) {
	if (player.getGameMode() == GameMode.CREATIVE) { return true; }
	return false;
    }

    public static boolean checkAble(Player player) {

	if (ServerUtils.getTps() < 19.5) { return false; }
	if (PlayerUtils.getPing(player) > 250) { return false; }
	if (MinecraftServer.currentTick < 5 * 20) { return false; }
	return true;
    }

    public static boolean checkAbleWithoutPing(Player player) {

	if (ServerUtils.getTps() < 19.5) { return false; }
	if (MinecraftServer.currentTick < 5 * 20) { return false; }
	return true;
    }

    public static int getPing(Player player) {
	CraftPlayer craftplayer = (CraftPlayer) player;
	int ping = craftplayer.getHandle().ping;
	return ping;
    }

    public static boolean isFalling(Player p, PlayerMoveEvent e) {
	PlayerData pd = PlayerData.getPlayerData(p);
	if (pd.getFalldistance() - p.getFallDistance() < -0.3 || System.currentTimeMillis() < pd.getOnGroundcooldown()) { return true; }
	return false;
    }

    public static boolean isJumping(PlayerMoveEvent e) {
	if (e.getFrom().getY() != e.getTo().getY()) { return true; }
	return false;
    }

    public static boolean inAir(Player p, PlayerMoveEvent e) {

	CraftPlayer cp = (CraftPlayer) p;
	EntityPlayer ep = (EntityPlayer) cp.getHandle();
	AxisAlignedBB playerBoundingBox = AxisAlignedBB.a(ep.boundingBox.a, ep.boundingBox.b, ep.boundingBox.c, ep.boundingBox.d, ep.boundingBox.b - 0.5, ep.boundingBox.f);

	if (!((CraftWorld) p.getWorld()).getHandle().b(playerBoundingBox, net.minecraft.server.v1_7_R4.Material.AIR)) { return false; }

	for (int x = -1; x < 1; x++) {
	    for (int y = -1; y < 1; y++) {
		Block block = p.getLocation().add(x, 1, y).getBlock();
		if (block.getType().isSolid()) { return false; }
	    }
	}

	return true;
    }

    public static boolean isOngroundMicroMovement(Player p, PlayerMoveEvent e) {
	if (e.getFrom().getY() != e.getTo().getY()) { return false; }
	return true;
    }

    public static boolean isOngroundFieldOfBlocksBelow(Player p, PlayerMoveEvent e) {

	for (int x = -2; x < 1; x++) {
	    for (int y = -2; y < 1; y++) {
		Block block = p.getLocation().add(x + 1, -1, y + 1).getBlock();

		// Location loc = block.getLocation();
		// p.sendMessage(loc.getBlockX() + " " + loc.getY() + " " +
		// loc.getZ() + " " + block.getType().name());

		if (block.getType().isSolid()) {
		    // p.sendMessage("solid " + block.getType().name());
		    return true;
		}
	    }
	}

	return false;
    }

    public static boolean onHalfBlock(Player p) {

	Location loc = p.getLocation();
	Block block = loc.getBlock();
	Block blockUnder = block.getRelative(BlockFace.DOWN);

	if (block.getType().toString().contains("STEP") || (block.getType().toString().contains("STAIRS"))) { return true; }
	if (blockUnder.getType().toString().contains("STEP") || (blockUnder.getType().toString().contains("STAIRS"))) { return true; }
	return false;
    }

    public static boolean isLiquid(Player p) {
	if (p.getLocation().subtract(0, 1, 0).getBlock().isLiquid()) { return true; }
	return false;
    }

    public static boolean isIce(Player p, PlayerMoveEvent e) {
	if (e.getTo().clone().subtract(0, 1, 0).getBlock().getType() == (Material.ICE) || e.getTo().clone().subtract(0, 1, 0).getBlock().getType() == (Material.PACKED_ICE)) { return true; }
	return false;
    }

    public static boolean isBlockabove(Player p, PlayerMoveEvent e) {
	if (p.getLocation().clone().add(0, 2, 0).getBlock().getType() != (Material.AIR)) { return true; }
	if (e.getFrom().clone().add(0, 2, 0).getBlock().getType() != (Material.AIR)) { return true; }
	return false;
    }

    public static boolean isNormal(Player p, PlayerMoveEvent e) {
	if (e.getFrom().getBlock().isLiquid() || e.getFrom().getBlock().getRelative(BlockFace.DOWN).isLiquid()
		|| e.getFrom().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) { return false; }
	return true;
    }

    public static boolean hasSpeed(Player p, PlayerMoveEvent e) {

	PlayerData pd = PlayerData.getPlayerData(p);

	if (p.hasPotionEffect(PotionEffectType.SPEED)) {
	    for (PotionEffect effect : p.getActivePotionEffects()) {
		if (effect.getType().equals(PotionEffectType.SPEED)) {
		    pd.setSpeedamplifier(effect.getAmplifier());
		}
	    }
	    return true;
	}
	return false;
    }

    public static boolean isCobweb(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	CraftPlayer cp = (CraftPlayer) p;
	EntityPlayer ep = (EntityPlayer) cp.getHandle();

	if (((CraftWorld) p.getWorld()).getHandle().b(ep.boundingBox, net.minecraft.server.v1_7_R4.Material.WEB)) { return true; }
	return false;
    }

    public static boolean playersCollide(Player p, Player target) {

	CraftPlayer cP = (CraftPlayer) p;
	EntityPlayer eP = (EntityPlayer) cP.getHandle();

	CraftPlayer cTarget = (CraftPlayer) target;
	EntityPlayer eTarget = (EntityPlayer) cTarget.getHandle();

	return hitboxesCollide(eP.boundingBox, eTarget.boundingBox);
    }

    public static boolean entitiesCollide(Entity entity1, Entity entity2) {

	CraftEntity craftEntity1 = ((CraftEntity) entity1);
	CraftEntity craftEntity2 = ((CraftEntity) entity2);
	return hitboxesCollide(craftEntity1.getHandle().boundingBox, craftEntity2.getHandle().boundingBox);
    }

    public static boolean hitboxesCollide(AxisAlignedBB box1, AxisAlignedBB box2) {

	if (intersects(box1.a, box1.d, box2.a, box2.d)) { return false; }
	if (intersects(box1.b, box1.e, box2.b, box2.e)) { return false; }
	if (intersects(box1.c, box1.f, box2.c, box2.f)) { return false; }

	return true;
    }

    public static boolean intersects(double box1a, double box1b, double box2a, double box2b) {
	if ((box1a >= box2a && box1a <= box2b) || (box1b <= box2b && box1b >= box2a)) { return false; }
	return true;
    }
}
