package me.Antikid.module.modules.movement;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.PlayerUtils;

public class NoFall extends Module implements Listener {

    public NoFall() {
	super("NoFall", new ItemBuilder(Material.ENDER_PEARL).build(), 1, 3, 5, false, BanReason.NOFALL);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void noFall(PlayerMoveEvent e) {

	Player player = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(player);

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }
	if (PlayerUtils.isCreative(player)) { return; }
	if (System.currentTimeMillis() < pd.getVelocitycooldown()) { return; }
	if (System.currentTimeMillis() < pd.getHitcooldown()) { return; }

	if (player.isOnGround() && PlayerUtils.inAir(player, e) && !PlayerUtils.onHalfBlock(player)) {
	    addViolation(player);
	    debug(player, Arrays.asList("nofall"));
	}
    }
}
