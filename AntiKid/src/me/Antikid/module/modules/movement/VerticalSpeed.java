package me.Antikid.module.modules.movement;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.MathUtils;
import me.Antikid.utils.PlayerUtils;

public class VerticalSpeed extends Module implements Listener {

    public VerticalSpeed() {
	super("VerticalSpeed", new ItemBuilder(Material.SPONGE).build(), 1, 3, 5, false, BanReason.VERTICALSPEED);
    }

    @EventHandler
    public void verticalSpeed(PlayerMoveEvent e) {

	Player player = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(player);

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }
	if (System.currentTimeMillis() < pd.getVelocitycooldown() || System.currentTimeMillis() < pd.getHitcooldown()) { return; }
	if (PlayerUtils.isCreative(player)) { return; }
	if (player.isFlying()) { return; }
	if (player.hasPotionEffect(PotionEffectType.JUMP)) { return; }

	double y1 = e.getFrom().getY();
	double y2 = e.getTo().getY();
	double difference = MathUtils.difference(y1, y2);

	if (y1 < y2) {
	    if (difference > 0.51) {
		addViolation(player);
		printOutDifference(player, difference, "VerticalSpeed", 0.5);
	    }
	}
    }

    public void printOutDifference(Player player, double differencey, String moveType, double mxs) {
	DecimalFormat df = new DecimalFormat("#.##");
	String stringdiffx = df.format(differencey);
	debug(player, Arrays.asList(moveType + stringdiffx + "mxs" + mxs));
    }

}
