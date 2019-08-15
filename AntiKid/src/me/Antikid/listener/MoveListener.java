package me.Antikid.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.main.Main;
import me.Antikid.types.Lag;
import me.Antikid.types.MoveTrail;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Playerchecks;
import net.minecraft.server.v1_7_R4.MinecraftServer;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (MoveTrail.trails.get(p) == null) {
	    new MoveTrail(p);
	}

	MoveTrail.trails.get(p).addLoc(e.getTo(), p);

	if (Playerchecks.isOngroundFieldOfBlocksBelow(p, e)) {
	    pd.setLastOnGround(p.getLocation());
	    pd.setOnGroundcooldown(1000);
	}

	if (Playerchecks.isLiquid(p)) {
	    pd.setLastOnGround(p.getLocation());
	    p.sendMessage("not ground");
	}

    }

    public static boolean checkAble(Player p) {

	if (Lag.getTPS() < 19.5) { return false; }
	if (Playerchecks.ping(p) > 250) { return false; }
	if (MinecraftServer.currentTick < 5 * 20) { return false; }
	return true;
    }

    public static boolean checkAbleWithoutPing(Player p) {

	if (Lag.getTPS() < 19.5) { return false; }
	if (MinecraftServer.currentTick < 5 * 20) { return false; }
	return true;
    }
}
