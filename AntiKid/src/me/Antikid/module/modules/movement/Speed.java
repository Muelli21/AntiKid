package me.Antikid.module.modules.movement;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.PlayerUtils;

public class Speed extends Module implements Listener {

    public Speed() {
	super("Speed", new ItemBuilder(Material.LEATHER_BOOTS).build(), 1, 3, 5, false, BanReason.SPEED);
    }

    @EventHandler
    public void onSpeedMove(PlayerMoveEvent e) {
	Player p = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(p);
	speed(e, p, pd, false);
    }

    public void speed(PlayerMoveEvent e, Player player, PlayerData pd, boolean walk) {

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }

	if (PlayerUtils.isCreative(player)) { return; }
	if (System.currentTimeMillis() < pd.getVelocitycooldown() || System.currentTimeMillis() < pd.getHitcooldown()) { return; }
	if (player.isInsideVehicle()) { return; }
	if (player.isFlying()) { return; }

	double walkSpeed = player.getWalkSpeed();
	MovementType movementType = checkMovementType(e);
	Vector move = e.getTo().toVector().subtract(e.getFrom().toVector());
	move.setY(0);

	if (System.currentTimeMillis() < pd.getWalkSpeedcooldown()) {
	    walkSpeed = pd.getLastWalkSpeed();
	}

	double mxs = movementType.getMaxMetersPerSecond();
	speedCheck(player, move, movementType, mxs, walkSpeed, false);
    }

    public void speedCheck(Player player, Vector move, MovementType movementType, Double mxs, Double walkSpeed, boolean walk) {

	PlayerData pd = PlayerData.getPlayerData(player);

	if (System.currentTimeMillis() < pd.getSpeedcooldown()) {
	    int amp = pd.getSpeedamplifier() + 1;
	    int percent = amp * 20;
	    mxs = (mxs + (mxs / 100) * percent);
	}

	if (walk) {
	    mxs = mxs * 0.77;
	}

	mxs = (mxs / 0.2) * player.getWalkSpeed();

	boolean legit;

	if (move.length() > movementType.getMaxMetersPerSecond()) {
	    addViolation(player);
	    legit = false;
	} else {
	    legit = true;
	}

	printOutDifference(player, move, movementType.name(), mxs, legit);
    }

    public MovementType checkMovementType(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(p);
	Double angle = Direction.directionAngle(e);

	if (PlayerUtils.isJumping(e))
	    pd.setJumpcooldown(500);

	if (PlayerUtils.isBlockabove(p, e))
	    pd.setBlockcooldown(800);

	if (PlayerUtils.isIce(p, e))
	    pd.setIcecooldown(700);

	if (PlayerUtils.hasSpeed(p, e))
	    pd.setSpeedcooldown(800);

	if (p.isSprinting())
	    pd.setSprintcooldown(1000);

	if (p.isSneaking() && System.currentTimeMillis() > pd.getSprintcooldown()) {
	    if (angle > 70) { return MovementType.SNEAKING_BACKWARDS; }
	    return MovementType.SNEAKING;
	}

	if (PlayerUtils.isCobweb(e)) { return MovementType.COBWEB; }

	if (System.currentTimeMillis() < pd.getJumpcooldown()) {

	    if (System.currentTimeMillis() < pd.getBlockcooldown() && System.currentTimeMillis() < pd.getIcecooldown()) { return MovementType.JUMPING_ON_ICE_UNDER_BLOCK; }
	    if (System.currentTimeMillis() < pd.getBlockcooldown()) { return MovementType.JUMPING_UNDER_BLOCK; }
	    if (System.currentTimeMillis() < pd.getIcecooldown()) { return MovementType.JUMPING_ON_ICE; }
	    if (angle > 70) { return MovementType.JUMPING_BACKWARDS; }
	    return MovementType.JUMPING;

	} else {

	    if (System.currentTimeMillis() < pd.getBlockcooldown() && System.currentTimeMillis() < pd.getIcecooldown()) { return MovementType.ON_ICE_UNDER_BLOCK; }
	    if (System.currentTimeMillis() < pd.getBlockcooldown()) { return MovementType.UNDER_BLOCK; }
	    if (System.currentTimeMillis() < pd.getIcecooldown()) { return MovementType.ON_ICE; }
	}

	return MovementType.NORMAL;
    }

    public void printOutDifference(Player p, Vector move, String moveType, double mxs, boolean legit) {

	DecimalFormat df = new DecimalFormat("#.##");
	String stringDifference = df.format(move.length());
	String stringMxs = df.format(mxs);
	String color = "§c";

	if (legit)
	    color = "§a";

	debug(p, Arrays.asList(moveType.toLowerCase() + " " + color + stringDifference + "§f/" + stringMxs + "m/s "));
    }

    enum MovementType {

	SNEAKING(
		0.2,
		2),
	SNEAKING_BACKWARDS(
		0.1,
		2),
	COBWEB(
		0.1,
		5),
	NORMAL(
		0.29,
		3),

	JUMPING(
		0.62,
		1),
	JUMPING_BACKWARDS(
		0.3,
		1),
	JUMPING_ON_ICE(
		0.58,
		1),
	JUMPING_ON_ICE_UNDER_BLOCK(
		0.92,
		1),
	JUMPING_UNDER_BLOCK(
		0.89,
		1),

	ON_ICE(
		0.39,
		1),
	ON_ICE_UNDER_BLOCK(
		0.92,
		1),
	UNDER_BLOCK(
		0.68,
		1);

	private Double maxMetersPerSecond;
	private Integer multiplier;

	private MovementType(Double maxMetersPerSecond, Integer multiplier) {
	    this.maxMetersPerSecond = maxMetersPerSecond;
	    this.multiplier = multiplier;
	}

	public Double getMaxMetersPerSecond() {
	    return maxMetersPerSecond;
	}

	public Integer getMultiplier() {
	    return multiplier;
	}
    }
}