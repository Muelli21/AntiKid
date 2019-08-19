package me.Antikid.utils;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Material;

import com.comphenix.protocol.PacketType;

public class Utils {

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
