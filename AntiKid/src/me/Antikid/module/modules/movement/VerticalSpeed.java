package me.Antikid.module.modules.movement;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import me.Antikid.listener.MoveListener;
import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Playerchecks;
import me.Antikid.types.Utils;

public class VerticalSpeed extends Module implements Listener {

    public VerticalSpeed() {
	super("VerticalSpeed", new ItemBuilder(Material.SPONGE).build());
    }

    @EventHandler
    public void verticalSpeed(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }
	if (System.currentTimeMillis() < pd.getVelocitycooldown() || System.currentTimeMillis() < pd.getHitcooldown()) { return; }
	if (Playerchecks.isCreative(p)) { return; }
	if (p.isFlying()) { return; }
	if (p.hasPotionEffect(PotionEffectType.JUMP)) { return; }

	double y1 = e.getFrom().getY();
	double y2 = e.getTo().getY();
	double difference = Utils.difference(y1, y2);

	if (y1 < y2) {
	    if (difference > 0.51) {
		pd.addVerticalSpeed();
		printOutDifference(p, difference, "VerticalSpeed", 0.5);
	    }
	}
    }

    public void printOutDifference(Player p, double differencey, String moveType, double mxs) {
	DecimalFormat df = new DecimalFormat("#.##");
	String stringdiffx = df.format(differencey);
	debug(p, Arrays.asList(moveType + stringdiffx + "mxs" + mxs));
    }

}
