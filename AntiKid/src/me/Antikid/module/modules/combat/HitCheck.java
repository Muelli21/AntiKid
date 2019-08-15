package me.Antikid.module.modules.combat;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Antikid.listener.MoveListener;
import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ClickActions;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Roast;
import me.Antikid.types.Utils;

public class HitCheck extends Module implements Listener {

    public HitCheck() {
	super("Hitcheck", new ItemBuilder(Material.GOLD_SWORD).build());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

	if (!(e.getDamager() instanceof Player)) { return; }

	Player p = (Player) e.getDamager();
	PlayerData pd = Main.getPlayerData(p);
	Material material = p.getItemInHand().getType();

	pd.getHits().addHit();

	if (p.isBlocking() && Utils.swordMaterials().contains(material)) {
	    pd.getHits().addBlockhit();
	}

	if (p.isSneaking() && Utils.swordMaterials().contains(material)) {
	    pd.getHits().addSneakHit();
	}

	if (isEnabled() && MoveListener.checkAble(p)) {
	    checkHits(p);
	}
    }

    @EventHandler
    public void onHit(PlayerInteractEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	Material material = p.getItemInHand().getType();

	pd.getHits().addInteract();

	if (p.isBlocking() && Utils.swordMaterials().contains(material)) {
	    pd.getHits().addBlockInteract();
	}
    }

    public void checkHits(Player p) {

	PlayerData pd = Main.getPlayerData(p);
	Roast roast = pd.getRoast();
	ClickActions hits = pd.getHits();

	if (hits.getTotalHitAmount() > 100) {

	    debug(p, Arrays.asList(" ", "hitamount: " + hits.getTotalHitAmount(), "blockhit click: " + hits.getBlockhit(), "block interact: " + hits.getBlockhit(), "sneakhits: " + hits.getSneakHit(),
		    "hits: " + hits.getHit()));

	    if (hits.getHit() != 0 && hits.getBlockhit() != 0 && hits.getBlockInteract() != 0 && hits.getInteract() != 0) {

		double blockHitPercentage = (hits.getBlockhit() / hits.getHit()) * 100;
		double blockInteractPercentage = (hits.getBlockInteract() / hits.getHit()) * 100;

		if (blockHitPercentage >= 90 && blockInteractPercentage <= 10) {
		    hits.setLastCheck(System.currentTimeMillis());
		    roast.addRoast();
		    Main.alert(pd.getPlayer(), "administration", "blockhit to hit percentage", blockHitPercentage);
		}
	    }

	    if (hits.getHit() != 0 && hits.getSneakHit() != 0) {
		double sneakHitPercentage = (hits.getSneakHit() / hits.getHit()) * 100;

		if (sneakHitPercentage >= 90) {
		    hits.setLastCheck(System.currentTimeMillis());
		    roast.addRoast();
		    Main.alert(pd.getPlayer(), "administration", "sneakHit to hit percentage", sneakHitPercentage);
		}
	    }
	}
    }
}
