package me.Antikid.module.modules.movement;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.listener.MoveListener;
import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Playerchecks;

public class NoFall extends Module implements Listener {

    public NoFall() {
	super("NoFall", new ItemBuilder(Material.ENDER_PEARL).build());
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void noFall(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }
	if (Playerchecks.isCreative(p)) { return; }
	if (System.currentTimeMillis() < pd.getVelocitycooldown()) { return; }
	if (System.currentTimeMillis() < pd.getHitcooldown()) { return; }

	if (p.isOnGround() && Playerchecks.inAir(p, e) && !Playerchecks.onHalfBlock(p)) {
	    pd.addNofall();
	    debug(p, Arrays.asList("nofall"));
	}
    }
}
