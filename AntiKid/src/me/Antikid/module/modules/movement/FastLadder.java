package me.Antikid.module.modules.movement;

import java.text.DecimalFormat;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.MathUtils;
import me.Antikid.utils.PlayerUtils;
import net.minecraft.server.v1_7_R4.EntityPlayer;

public class FastLadder extends Module implements Listener {

    public FastLadder() {
	super("FastLadder", new ItemBuilder(Material.LADDER).build(), 1, 3, 5, false, BanReason.FAST_LADDER);
    }

    @EventHandler
    public void ladder(PlayerMoveEvent e) {

	Player player = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(player);

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }

	CraftPlayer cp = (CraftPlayer) player;
	EntityPlayer ep = (EntityPlayer) cp.getHandle();

	if (System.currentTimeMillis() < pd.getVelocitycooldown() || System.currentTimeMillis() < pd.getHitcooldown()) { return; }
	if (PlayerUtils.isCreative(player)) { return; }
	if (player.isFlying()) { return; }

	if (ep.h_() && !PlayerUtils.onHalfBlock(player)) {
	    double y1 = e.getFrom().getY();
	    double y2 = e.getTo().getY();
	    double difference = MathUtils.difference(y1, y2);
	    boolean legit = true;
	    LadderMovementType movementType = LadderMovementType.UPWARDS;

	    if (y1 > y2)
		movementType = LadderMovementType.DOWNWARDS;

	    if (difference > movementType.getMaxMetersPerSecond()) {
		addViolation(player);
		legit = false;
	    }

	    printOutDifference(player, difference, movementType, legit);
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

    enum LadderMovementType {

	UPWARDS(
		0.13,
		1),
	DOWNWARDS(
		0.16,
		2);

	private Double maxMetersPerSecond;
	private Integer multiplier;

	private LadderMovementType(Double maxMetersPerSecond, Integer multiplier) {
	    this.maxMetersPerSecond = maxMetersPerSecond;
	    this.multiplier = multiplier;
	}

	public Double getMaxMetersPerSecond() {
	    return maxMetersPerSecond;
	}

	public Integer getMultiplier() {
	    return multiplier;
	}
    }
}
