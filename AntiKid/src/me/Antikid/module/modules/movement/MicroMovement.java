package me.Antikid.module.modules.movement;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
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
import me.Antikid.types.Playerchecks;

public class MicroMovement extends Module implements Listener {

    public MicroMovement() {
	super("Micromovement", new ItemBuilder(Material.CHAINMAIL_BOOTS).build());
    }

    @EventHandler
    public void microMove(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	Entity entity = p;
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	Vector move = e.getTo().toVector().subtract(e.getFrom().toVector());
	move.setY(0);

	if ((Playerchecks.isCreative(p)) || (Playerchecks.isBlockabove(p, e)) || (Playerchecks.isCobweb(e)) || (Playerchecks.isLiquid(p)) || (Playerchecks.isOngroundMicroMovement(p, e))
		|| (!entity.isOnGround())) { return; }
	if (System.currentTimeMillis() < pd.getVelocitycooldown()) { return; }

	if (move.length() < 0.1 && move.length() > 0) {
	    pd.addMicroMovement();
	    debug(p, Arrays.asList(move.length() + ""));
	}
    }
}
