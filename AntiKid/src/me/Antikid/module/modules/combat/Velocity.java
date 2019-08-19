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

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.MathUtils;
import me.Antikid.utils.PlayerUtils;

public class Velocity extends Module implements Listener {

    public Velocity() {
	super("Velocity", new ItemBuilder(Material.ANVIL).build(), 1, 3, 5, false, BanReason.VELOCITY);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onKnockBack(PlayerVelocityEvent e) {

	Player player = (Player) e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(player);
	pd.setLastVelocity(e.getVelocity());
	pd.setLastServerSidedPosition(player.getLocation());
	Entity entity = player;

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }
	if (PlayerUtils.isCreative(player) || !entity.isOnGround()) { return; }

	if (pd.isHit() && !e.isCancelled()) {
	    addViolation(player);
	    debug(player, Arrays.asList("no PlayerMoveEvent"));
	}

	if (e.getVelocity().getY() > 0) {
	    pd.setHit(true);
	    pd.setVelocityCheck(true);
	}
    }

    @EventHandler
    public void velocity(PlayerMoveEvent e) {

	Player player = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(player);

	if (PlayerUtils.isCreative(player)) { return; }

	if (PlayerUtils.isBlockabove(player, e) || PlayerUtils.isCobweb(e) || PlayerUtils.isLiquid(player)) {
	    pd.setVelocityCheck(false);
	    pd.setHit(false);
	    return;
	}

	if (e.getFrom().getY() != e.getTo().getY()) {
	    pd.setHit(false);
	}

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }

	if (pd.isVelocityCheck()) {

	    double distance = MathUtils.difference(pd.getLastServerSidedPosition().getY(), e.getTo().getY());
	    double ratio = Math.floor((distance / pd.getLastVelocity().getY()) * 100) + 1;

	    DecimalFormat df = new DecimalFormat("#.##");
	    String stringDistance = df.format(distance);
	    String stringYVelocity = df.format(pd.getLastVelocity().getY());

	    pd.setVelocityCheck(false);

	    if ((ratio < 99 && ratio > 0 && distance != 0)) {
		debug(player, Arrays.asList("§c" + ratio, " - AntiKb distance: §6 " + stringDistance, " - AntiKb velocity: §e " + stringYVelocity));
		addViolation(player);
	    }
	}
    }
}
