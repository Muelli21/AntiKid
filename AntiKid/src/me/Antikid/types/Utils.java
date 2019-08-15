package me.Antikid.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;

public class Utils {

    @SuppressWarnings("deprecation")
    public static Player getRandomPlayer(Player not) {

	ArrayList<Player> allplayers = new ArrayList<>();
	for (Player ps : Bukkit.getOnlinePlayers())
	    allplayers.add(ps);

	if (allplayers.size() > 1) {
	    allplayers.remove(not);
	}

	int random = (int) (Math.random() * allplayers.size());
	Player target = allplayers.get(random);
	allplayers.clear();

	return target;
    }

    public static double difference(double a, double b) {
	return Math.abs(a - b);
    }

    public static Vector rotate(Vector input, double degrees) {
	Vector rotVec = new Vector();
	double rx = (input.getX() * Math.cos(degrees)) - (input.getZ() * Math.sin(degrees));
	double rz = (input.getX() * Math.sin(degrees)) + (input.getZ() * Math.cos(degrees));
	rotVec.setX(rx);
	rotVec.setZ(rz);
	return rotVec;
    }

    public static Vector getDirectionVector(float pitch, float yaw) {
	double pitchRadians = Math.toRadians(pitch);
	double yawRadians = Math.toRadians(yaw);

	double sinPitch = Math.sin(pitchRadians);
	double cosPitch = Math.cos(pitchRadians);
	double sinYaw = Math.sin(yawRadians);
	double cosYaw = Math.cos(yawRadians);

	return new Vector(-cosPitch * sinYaw, sinPitch, -cosPitch * cosYaw);
    }

    public static HashSet<Material> swordMaterials() {

	HashSet<Material> swordMaterials = new HashSet<>();
	swordMaterials.add(Material.WOOD_SWORD);
	swordMaterials.add(Material.GOLD_SWORD);
	swordMaterials.add(Material.STONE_SWORD);
	swordMaterials.add(Material.IRON_SWORD);
	swordMaterials.add(Material.DIAMOND_SWORD);
	return swordMaterials;
    }

    public static Integer maxPacketAmount(PacketType type) {

	HashMap<PacketType, Integer> maxPackets = new HashMap<>();
	maxPackets.put(PacketType.Play.Client.ARM_ANIMATION, 100);
	maxPackets.put(PacketType.Play.Client.KEEP_ALIVE, 100);
	maxPackets.put(PacketType.Play.Client.POSITION, 100);
	maxPackets.put(PacketType.Play.Client.FLYING, 100);
	maxPackets.put(PacketType.Play.Client.POSITION_LOOK, 100);
	maxPackets.put(PacketType.Play.Client.TAB_COMPLETE, 100);
	maxPackets.put(PacketType.Play.Client.LOOK, 100);
	maxPackets.put(PacketType.Play.Client.WINDOW_CLICK, 100);
	maxPackets.put(PacketType.Play.Client.ARM_ANIMATION, 100);
	maxPackets.put(PacketType.Play.Client.HELD_ITEM_SLOT, 100);
	maxPackets.put(PacketType.Play.Client.RESOURCE_PACK_STATUS, 100);
	maxPackets.put(PacketType.Play.Client.CUSTOM_PAYLOAD, 100);
	maxPackets.put(PacketType.Play.Client.ABILITIES, 100);
	maxPackets.put(PacketType.Play.Client.SETTINGS, 100);
	maxPackets.put(PacketType.Play.Client.TRANSACTION, 100);
	maxPackets.put(PacketType.Play.Client.CLIENT_COMMAND, 100);

	return maxPackets.get(type);
    }
}
