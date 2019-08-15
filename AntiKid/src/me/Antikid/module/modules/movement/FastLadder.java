package me.Antikid.module.modules.movement;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.listener.MoveListener;
import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.LadderMovementType;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Playerchecks;
import me.Antikid.types.Utils;
import net.minecraft.server.v1_7_R4.EntityPlayer;

public class FastLadder extends Module implements Listener {

    public FastLadder() {
	super("FastLadder", new ItemBuilder(Material.LADDER).build());
    }

    @EventHandler
    public void ladder(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	CraftPlayer cp = (CraftPlayer) p;
	EntityPlayer ep = (EntityPlayer) cp.getHandle();

	if (System.currentTimeMillis() < pd.getVelocitycooldown() || System.currentTimeMillis() < pd.getHitcooldown()) { return; }
	if (Playerchecks.isCreative(p)) { return; }
	if (p.isFlying()) { return; }

	p.sendMessage(e.getEventName() + " " + p.getVelocity().getY());
	

	if (ep.h_() && !Playerchecks.onHalfBlock(p)) {
	    double y1 = e.getFrom().getY();
	    double y2 = e.getTo().getY();
	    double difference = Utils.difference(y1, y2);
	    boolean legit = true;
	    LadderMovementType movementType = LadderMovementType.UPWARDS;

	    if (y1 > y2)
		movementType = LadderMovementType.DOWNWARDS;

	    if (difference > movementType.getMaxMetersPerSecond()) {
		pd.setLadder(pd.getLadder() + movementType.getMultiplier());
		legit = false;
	    }

	    printOutDifference(p, difference, movementType, legit);
	}
    }

    public void printOutDifference(Player p, double differenceY, LadderMovementType movementType, boolean legit) {
	DecimalFormat df = new DecimalFormat("#.##");
	String stringdiffY = df.format(differenceY);
	String color = "§c";

	if (legit)
	    color = "§a";

	debug(p, Arrays.asList(movementType.name().toLowerCase() + " m/s: " + color + stringdiffY + "/" + movementType.getMaxMetersPerSecond()));
    }
}
