package me.Antikid.module.modules.combat;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import me.Antikid.listener.MoveListener;
import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Playerchecks;
import me.Antikid.types.Utils;

public class Velocity extends Module implements Listener {

    public Velocity() {
	super("Velocity", new ItemBuilder(Material.ANVIL).build());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKnockBack(PlayerVelocityEvent e) {

	Player p = (Player) e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	pd.setLastVelocity(e.getVelocity());
	pd.setLastServerSidedPosition(p.getLocation());
	Entity entity = p;

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }
	if (Playerchecks.isCreative(p) || !entity.isOnGround()) { return; }

	if (pd.isHit() && !e.isCancelled()) {
	    pd.addVelocity();
	    debug(p, Arrays.asList("no PlayerMoveEvent"));
	}

	if (e.getVelocity().getY() > 0) {
	    pd.setHit(true);
	    pd.setVelocityCheck(true);
	}
    }

    // @EventHandler(priority = EventPriority.MONITOR)
    // public void onHit(EntityDamageByEntityEvent e) {
    //
    // if (!(e.getEntity() instanceof Player)) { return; }
    // if (!(e.getDamager() instanceof Player)) { return; }
    //
    // Player p = (Player) e.getEntity();
    // PlayerData pd = Main.getPlayerData(p);
    //
    // if (!isEnabled() || !MoveListener.checkAble(p)) { return; }
    // if (Playerchecks.isCreative(p)) { return; }
    //
    // if (pd.isHit() && !e.isCancelled()) {
    // pd.addVelocity();
    // if (isDebug() && pd.isDebug()) {
    // p.sendMessage("AntiKb");
    // }
    // }
    //
    // pd.setHit(true);
    // }

    @EventHandler
    public void velocity(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (Playerchecks.isCreative(p))
	    return;

	if (Playerchecks.isBlockabove(p, e) || Playerchecks.isCobweb(e) || Playerchecks.isLiquid(p)) {
	    pd.setVelocityCheck(false);
	    pd.setHit(false);
	    return;
	}

	if (e.getFrom().getY() != e.getTo().getY()) {
	    pd.setHit(false);
	}

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	if (pd.isVelocityCheck()) {

	    double distance = Utils.difference(pd.getLastServerSidedPosition().getY(), e.getTo().getY());
	    double ratio = Math.floor((distance / pd.getLastVelocity().getY()) * 100) + 1;

	    DecimalFormat df = new DecimalFormat("#.##");
	    String stringDistance = df.format(distance);
	    String stringYVelocity = df.format(pd.getLastVelocity().getY());

	    pd.setVelocityCheck(false);

	    if ((ratio < 99 && ratio > 0 && distance != 0)) {
		debug(p, Arrays.asList("§c" + ratio, " - AntiKb distance: §6 " + stringDistance, " - AntiKb velocity: §e " + stringYVelocity));
		pd.addVelocity();
	    }
	}
    }
}
