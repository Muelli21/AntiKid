package me.Antikid.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Antikid.types.PlayerData;

public class ServerUtils {

    public static double getTps() {
	return net.minecraft.server.v1_7_R4.MinecraftServer.getServer().recentTps[0];
    }

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

    @SuppressWarnings("deprecation")
    public static void alert(Player p, String permission, String reason, double frequency) {

	int roasts = PlayerData.getPlayerData(p).getRoast().getRoasts();

	for (Player ps : Bukkit.getOnlinePlayers()) {
	    // if (!ps.hasPermission(permission)) { return; }
	    if (!ps.hasPermission("Developer")) { return; }

	    ps.sendMessage(" ");
	    ps.sendMessage("§r[" + roasts + "]§6" + Bukkit.getConsoleSender().getName() + " detected the player " + p.getPlayer().getName());
	    ps.sendMessage("§r    - He might be using " + reason + ": " + frequency);
	    ps.sendMessage(" ");
	}

	// Console
	System.out.print(" ");
	System.out.print("§r[" + roasts + "]§6" + Bukkit.getConsoleSender().getName() + " detected the player " + p.getPlayer().getName());
	System.out.print("§r    - He might be using " + reason + ": " + frequency);
	System.out.print(" ");
    }
}
