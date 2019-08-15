package me.Antikid.module.modules.player;

import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.potion.PotionType;

import me.Antikid.listener.MoveListener;
import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.PlayerData;

public class Heal extends Module implements Listener {

    public Heal() {
	super("Heal", new ItemBuilder.PotionBuilder(PotionType.REGEN, 1).build().build());
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent e) {

	if (!(e.getEntity() instanceof Player)) { return; }

	Player p = (Player) e.getEntity();
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	if (e.getRegainReason() == RegainReason.SATIATED) {

	    if (System.currentTimeMillis() - pd.getHealtime() < 3500) {
		pd.setHeal(pd.getHeal() + 1);
		debug(p, Arrays.asList("unlegit heal"));
	    }

	    pd.setHealtime(System.currentTimeMillis());
	}
    }
}
