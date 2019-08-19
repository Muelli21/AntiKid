package me.Antikid.module.modules.movement;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.PlayerUtils;

public class MicroMovement extends Module implements Listener {

    public MicroMovement() {
	super("Micromovement", new ItemBuilder(Material.CHAINMAIL_BOOTS).build(), 1, 3, 5, false, BanReason.MICROMOVEMENT);
    }

    @EventHandler
    public void microMove(PlayerMoveEvent e) {

	Player player = e.getPlayer();
	Entity entity = player;
	PlayerData pd = PlayerData.getPlayerData(player);

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }

	Vector move = e.getTo().toVector().subtract(e.getFrom().toVector());
	move.setY(0);

	if ((PlayerUtils.isCreative(player)) || (PlayerUtils.isBlockabove(player, e)) || (PlayerUtils.isCobweb(e)) || (PlayerUtils.isLiquid(player)) || (PlayerUtils.isOngroundMicroMovement(player, e))
		|| (!entity.isOnGround())) { return; }
	if (System.currentTimeMillis() < pd.getVelocitycooldown()) { return; }

	if (move.length() < 0.1 && move.length() > 0) {
	    addViolation(player);
	    debug(player, Arrays.asList(move.length() + ""));
	}
    }
}
