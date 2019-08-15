package me.Antikid.module.modules.combat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.BanUtils;
import me.Antikid.types.ItemBuilder;

public class Vape extends Module implements Listener, PluginMessageListener {

    public Vape() {
	super("Vape", new ItemBuilder(Material.EXP_BOTTLE).build());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
	Player p = e.getPlayer();
	p.sendMessage("§8 §8 §1 §3 §3 §7 §8 ");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] data) {
	if (channel.equals("LOLIMAHACKER")) {
	    BanUtils.delayedBan(Bukkit.getConsoleSender(), p, BanReason.MODIFIED_CLIENT, 60 * 20);
	}
    }
}