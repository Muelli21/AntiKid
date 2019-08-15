package me.Antikid.module.modules.combat;

import org.bukkit.Material;
import org.bukkit.event.Listener;

import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;

public class ExperimentalVelocity extends Module implements Listener {

    public ExperimentalVelocity() {
	super("Experimental velocity", new ItemBuilder(Material.IRON_BOOTS).build());
    }

    // @EventHandler(priority = EventPriority.MONITOR)
    // public void onKnockBack(PlayerVelocityEvent e) {
    //
    // Player p = (Player) e.getPlayer();
    // PlayerData pd = Main.getPlayerData(p);
    // pd.setLastVelocity(e.getVelocity());
    // pd.setLastServerSidedPosition(p.getLocation());
    // Entity entity = p;
    //
    // if (!isEnabled() || !MoveListener.checkAble(p))
    // return;
    //
    // if (Playerchecks.isCreative(p) || !entity.isOnGround())
    // return;
    //
    // if (pd.isHit() && !e.isCancelled()) {
    // pd.addVelocity();
    // if (isDebug() && pd.isDebug()) {
    // p.sendMessage("AntiKb");
    // }
    // }
    //
    // if (e.getVelocity().length() > 0) {
    // pd.setHit(true);
    // pd.setVelocityCheck(true);
    // }
    // }
    //
    // @EventHandler
    // public void velocity(PlayerMoveEvent e) {
    //
    // Player p = e.getPlayer();
    // PlayerData pd = Main.getPlayerData(p);
    //
    // if (Playerchecks.isCreative(p))
    // return;
    //
    // if (Playerchecks.isBlockabove(p, e) || Playerchecks.isCobweb(e) ||
    // Playerchecks.isLiquid(p)) {
    // pd.setVelocityCheck(false);
    // pd.setHit(false);
    // return;
    // }
    //
    // if (e.getFrom() != e.getTo()) {
    // pd.setHit(false);
    // }
    //
    // if (!isEnabled() || !MoveListener.checkAble(p))
    // return;
    //
    // if (pd.isVelocityCheck()) {
    //
    // double distanceX =
    // Utils.difference(pd.getLastServerSidedPosition().getX(),
    // e.getTo().getX());
    // double distanceZ =
    // Utils.difference(pd.getLastServerSidedPosition().getZ(),
    // e.getTo().getZ());
    // double distanceY =
    // Utils.difference(pd.getLastServerSidedPosition().getY(),
    // e.getTo().getY());
    //
    // double velocityX = pd.getLastVelocity().getX();
    // double velocityY = pd.getLastVelocity().getY();
    // double velocityZ = pd.getLastVelocity().getZ();
    //
    // double distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY,
    // 2) + Math.pow(distanceZ, 2));
    // double velocity = Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY,
    // 2) + Math.pow(velocityZ, 2));
    //
    // double ratio = Math.floor((distance / velocity) * 100) + 1;
    //
    // pd.setVelocityCheck(false);
    //
    // if (ratio < 99 && ratio > 0) {
    // if (isDebug() && pd.isDebug()) {
    // p.sendMessage("Vertical knockback percentage: §c " + ratio);
    // p.sendMessage("AntiKb distance: §6 " + distance);
    // p.sendMessage("AntiKb velocity: §e " + pd.getLastVelocity().getY());
    // }
    // pd.addVelocity();
    // }
    // }
    // }
}
