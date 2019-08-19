package me.Antikid.module.modules.player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.injector.GamePhase;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.BanUtils;
import me.Antikid.utils.PlayerUtils;
import me.Antikid.utils.Utils;

public class Packets implements PacketListener {

    @Override
    public Plugin getPlugin() {
	return null;
    }

    @Override
    public ListeningWhitelist getReceivingWhitelist() {
	return ListeningWhitelist.newBuilder().normal().gamePhase(GamePhase.PLAYING).options(new ListenerOptions[] {})
		.types(PacketType.Play.Client.ARM_ANIMATION, PacketType.Play.Client.KEEP_ALIVE, PacketType.Play.Client.POSITION, PacketType.Play.Client.FLYING, PacketType.Play.Client.POSITION_LOOK,
			PacketType.Play.Client.TAB_COMPLETE, PacketType.Play.Client.LOOK, PacketType.Play.Client.WINDOW_CLICK, PacketType.Play.Client.ARM_ANIMATION,
			PacketType.Play.Client.HELD_ITEM_SLOT, PacketType.Play.Client.RESOURCE_PACK_STATUS, PacketType.Play.Client.CUSTOM_PAYLOAD, PacketType.Play.Client.ABILITIES,
			PacketType.Play.Client.SETTINGS, PacketType.Play.Client.TRANSACTION, PacketType.Play.Client.CLIENT_COMMAND)
		.build();
    }

    @Override
    public ListeningWhitelist getSendingWhitelist() {
	return null;
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {

	PacketContainer packet = e.getPacket();
	Player player = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(player);
	PacketType packetType = packet.getType();

	// if (pd.msOnline() > 10 * 1000) {
	// if (!pd.isJoinTickSet()) {
	// pd.setJoinTickSet(true);
	// pd.setJoinTick();
	// return;
	// }
	//
	// if (pd.getPacketTick() != MinecraftServer.currentTick) {
	// pd.setPacketType(null);
	// }
	//
	// if (e.getPacketType() == PacketType.Play.Client.FLYING &&
	// (pd.getPacketTick() == MinecraftServer.currentTick &&
	// pd.getPacketType() == packet.getType()) || pd.getPacketType() ==
	// null) {
	// pd.addTotalMovepacket();
	// pd.setPacketType(packet.getType());
	// pd.setPacketTick(MinecraftServer.currentTick);
	//
	// } else if (e.getPacketType() == PacketType.Play.Client.POSITION_LOOK
	// && (pd.getPacketTick() == MinecraftServer.currentTick &&
	// pd.getPacketType() == packet.getType())
	// || pd.getPacketType() == null) {
	//
	// pd.addTotalMovepacket();
	// pd.setPacketType(packet.getType());
	// pd.setPacketTick(MinecraftServer.currentTick);
	//
	// } else if (e.getPacketType() == PacketType.Play.Client.LOOK &&
	// (pd.getPacketTick() == MinecraftServer.currentTick &&
	// pd.getPacketType() == packet.getType())
	// || pd.getPacketType() == null) {
	//
	// pd.addTotalMovepacket();
	// pd.setPacketType(packet.getType());
	// pd.setPacketTick(MinecraftServer.currentTick);
	//
	// } else if (e.getPacketType() == PacketType.Play.Client.POSITION &&
	// (pd.getPacketTick() == MinecraftServer.currentTick &&
	// pd.getPacketType() == packet.getType())
	// || pd.getPacketType() == null) {
	//
	// pd.addTotalMovepacket();
	// pd.setPacketType(packet.getType());
	// pd.setPacketTick(MinecraftServer.currentTick);
	// }
	//
	// p.sendMessage("§cmoveticks " + pd.getTotalMovepackets());
	// p.sendMessage("§6ticksOnline " + pd.ticksOnline());
	//
	// if (pd.getTotalMovepackets() > pd.ticksOnline()) {
	// pd.getRoast().resetRoasts();
	// BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(),
	// BanReason.PACKETS);
	// }
	// }

	if (PlayerUtils.checkAbleWithoutPing(player)) {
	    HashMap<PacketType, AtomicInteger> packetAmount = pd.getPacketAmount();

	    if (pd.getLastPacketResetTime().get(packetType) == null) {
		pd.getLastPacketResetTime().put(packetType, System.currentTimeMillis());
		packetAmount.put(packetType, new AtomicInteger(1));
	    }

	    if (System.currentTimeMillis() - pd.getLastPacketResetTime().get(packetType) > 50) {
		pd.getLastPacketResetTime().put(packetType, System.currentTimeMillis());
		packetAmount.put(packetType, new AtomicInteger(1));
	    }

	    if (packetAmount.get(packetType) != null) {
		packetAmount.get(packetType).incrementAndGet();
		if (packetAmount.get(packetType).get() > Utils.maxPacketAmount(packetType)) {
		    BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.PACKETS);
		}
	    } else {
		packetAmount.put(packetType, new AtomicInteger(1));
	    }
	}

	if (e.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
	    if (packet.getEntityUseActions() != null) {
		if (pd.getFakeplayer() != null) {
		    if (packet.getIntegers().getValues().get(0) == pd.getFakeplayer().getEp().getId()) {
			if (pd.getSittingCheck() != null) {
			    pd.getSittingCheck().ban();
			    return;
			}

			Module module = Module.getModuleByName("killaura");
			module.addViolation(player);
			module.debug(player, Arrays.asList("hit"));
		    }
		}
	    }
	}
    }

    @Override
    public void onPacketSending(PacketEvent arg0) {

    }
}
