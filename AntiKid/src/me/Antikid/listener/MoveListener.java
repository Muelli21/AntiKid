package me.Antikid.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.types.MoveTrail;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.PlayerUtils;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(p);

	if (MoveTrail.trails.get(p) == null) {
	    new MoveTrail(p);
	}

	MoveTrail.trails.get(p).addLoc(e.getTo(), p);

	if (PlayerUtils.isOngroundFieldOfBlocksBelow(p, e)) {
	    pd.setLastOnGround(p.getLocation());
	    pd.setOnGroundcooldown(1000);
	}

	if (PlayerUtils.isLiquid(p)) {
	    pd.setLastOnGround(p.getLocation());
	}
    }
}
