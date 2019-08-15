package me.Antikid.module.modules.movement;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import me.Antikid.listener.MoveListener;
import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.MovementType;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Playerchecks;

public class Speed extends Module implements Listener {

    public Speed() {
	super("Speed", new ItemBuilder(Material.LEATHER_BOOTS).build());
    }

    @EventHandler
    public void onSpeedMove(PlayerMoveEvent e) {
	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	speed(e, p, pd, false);
    }

    public void speed(PlayerMoveEvent e, Player p, PlayerData pd, boolean walk) {

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	if (Playerchecks.isCreative(p)) { return; }
	if (System.currentTimeMillis() < pd.getVelocitycooldown() || System.currentTimeMillis() < pd.getHitcooldown()) { return; }
	if (p.isInsideVehicle()) { return; }
	if (p.isFlying()) { return; }

	double walkSpeed = p.getWalkSpeed();
	MovementType movementType = checkMovementType(e);
	Vector move = e.getTo().toVector().subtract(e.getFrom().toVector());
	move.setY(0);

	if (System.currentTimeMillis() < pd.getWalkSpeedcooldown()) {
	    walkSpeed = pd.getLastWalkSpeed();
	}

	double mxs = movementType.getMaxMetersPerSecond();
	speedCheck(p, move, movementType, mxs, walkSpeed, false);
    }

    public void speedCheck(Player p, Vector move, MovementType movementType, Double mxs, Double walkSpeed, boolean walk) {

	PlayerData pd = Main.getPlayerData(p);

	if (System.currentTimeMillis() < pd.getSpeedcooldown()) {
	    int amp = pd.getSpeedamplifier() + 1;
	    int percent = amp * 20;
	    mxs = (mxs + (mxs / 100) * percent);
	}

	if (walk) {
	    mxs = mxs * 0.77;
	}

	mxs = (mxs / 0.2) * p.getWalkSpeed();

	boolean legit;

	if (move.length() > movementType.getMaxMetersPerSecond()) {
	    pd.setSpeed(pd.getSpeed() + movementType.getMultiplier());
	    legit = false;
	} else {
	    legit = true;
	}

	printOutDifference(p, move, movementType.name(), mxs, legit);
    }

    public MovementType checkMovementType(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	Double angle = Direction.directionAngle(e);

	if (Playerchecks.isJumping(e))
	    pd.setJumpcooldown(500);

	if (Playerchecks.isBlockabove(p, e))
	    pd.setBlockcooldown(800);

	if (Playerchecks.isIce(p, e))
	    pd.setIcecooldown(700);

	if (Playerchecks.hasSpeed(p, e))
	    pd.setSpeedcooldown(800);

	if (p.isSprinting())
	    pd.setSprintcooldown(1000);

	// If player is sneaking
	if (p.isSneaking() && System.currentTimeMillis() > pd.getSprintcooldown()) {

	    // If player is sneaking backwards
	    if (angle > 70) { return MovementType.SNEAKING_BACKWARDS; }
	    return MovementType.SNEAKING;
	}

	// If player is walking in cobweb
	if (Playerchecks.isCobweb(e)) { return MovementType.COBWEB; }

	// If player is jumping
	if (System.currentTimeMillis() < pd.getJumpcooldown()) {

	    // If player is jumping on ice and has a block above him
	    if (System.currentTimeMillis() < pd.getBlockcooldown() && System.currentTimeMillis() < pd.getIcecooldown()) { return MovementType.JUMPING_ON_ICE_UNDER_BLOCK; }

	    // If player is jumping and has a block above him
	    if (System.currentTimeMillis() < pd.getBlockcooldown()) { return MovementType.JUMPING_UNDER_BLOCK; }

	    // If player is jumping on ice
	    if (System.currentTimeMillis() < pd.getIcecooldown()) { return MovementType.JUMPING_ON_ICE; }

	    // If player is jumping and walking backwards
	    if (angle > 70) { return MovementType.JUMPING_BACKWARDS; }

	    // If player is jumping
	    return MovementType.JUMPING;

	} else {

	    // If player is on ice and under a block
	    if (System.currentTimeMillis() < pd.getBlockcooldown() && System.currentTimeMillis() < pd.getIcecooldown()) { return MovementType.ON_ICE_UNDER_BLOCK; }

	    // If player is under a block
	    if (System.currentTimeMillis() < pd.getBlockcooldown()) { return MovementType.UNDER_BLOCK; }

	    // If player is on ice
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
}