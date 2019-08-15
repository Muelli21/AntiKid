package me.Antikid.module.modules.movement;

import java.util.Arrays;

import org.bukkit.Color;
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
import me.Antikid.types.PlayerData;

public class Direction extends Module implements Listener {

    public Direction() {
	super("Direction", new ItemBuilder.LeatherAmourBuilder(Material.LEATHER_HELMET, Color.ORANGE).build().build());
    }

    @EventHandler
    public void direction(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	double angle = Direction.directionAngle(e);
	String color = "§a";

	if (angle > 70) {
	    Speed speedModule = (Speed) Module.getModuleByName("speed");
	    speedModule.speed(e, p, pd, true);
	    color = "§c";
	}

	debug(p, Arrays.asList(color + angle + " §fdegrees"));
    }

    public static double directionAngle(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	Vector direction = p.getEyeLocation().getDirection();
	Vector move = e.getTo().toVector().subtract(e.getFrom().toVector());

	move.setY(0);
	direction.setY(0);
	return Direction.angleBetweenVectors(direction, move);
    }

    public static double angleBetweenVectors(Vector vector1, Vector vector2) {

	double cos = (vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ()) / (vector1.length() * vector2.length());
	double radian = Math.acos(cos);
	double angle = Math.toDegrees(radian);

	return angle;
    }
}
