package me.Antikid.module.modules.player;

import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.potion.PotionType;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.PlayerUtils;

public class Heal extends Module implements Listener {

    public Heal() {
	super("Heal", new ItemBuilder.PotionBuilder(PotionType.REGEN, 1).build().build(), 1, 3, 5, false, BanReason.HEAL);
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent e) {

	if (!(e.getEntity() instanceof Player)) { return; }

	Player player = (Player) e.getEntity();
	PlayerData pd = PlayerData.getPlayerData(player);

	if (!isEnabled() || !PlayerUtils.checkAble(player)) { return; }

	if (e.getRegainReason() == RegainReason.SATIATED) {

	    if (System.currentTimeMillis() - pd.getHealtime() < 3500) {
		addViolation(player);
		debug(player, Arrays.asList("unlegit heal"));
	    }

	    pd.setHealtime(System.currentTimeMillis());
	}
    }
}
