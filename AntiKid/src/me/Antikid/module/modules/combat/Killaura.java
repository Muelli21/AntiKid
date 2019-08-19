package me.Antikid.module.modules.combat;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import me.Antikid.module.Module;
import me.Antikid.types.BanReason;
import me.Antikid.types.FakeEntity;
import me.Antikid.types.PlayerData;
import me.Antikid.utils.ItemBuilder;
import me.Antikid.utils.MathUtils;
import me.Antikid.utils.ServerUtils;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class Killaura extends Module implements Listener {

    public Killaura() {
	super("Killaura", new ItemBuilder(Material.DIAMOND_SWORD).build(), 1, 3, 5, false, BanReason.KILLAURA);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {

	if (!isEnabled()) { return; }
	if (!(e.getDamager() instanceof Player)) { return; }

	Player p = (Player) e.getDamager();
	PlayerData pd = PlayerData.getPlayerData(p);

	if (pd.getFakeplayer() != null) {
	    pd.getFakeplayer().setRemovetime(10 * 1000);
	    return;
	}

	Player random = ServerUtils.getRandomPlayer(p);
	CraftPlayer crandom = (CraftPlayer) random;
	EntityPlayer erandom = (EntityPlayer) crandom.getHandle();
	GameProfile profile = erandom.getProfile();

	Location spawnloc = p.getLocation().clone();
	spawnloc.add(0, p.getEyeHeight() * 1.2, 0);
	spawnloc.setPitch(0);
	Vector mod = spawnloc.getDirection();
	spawnloc.subtract(MathUtils.rotate(mod, 100).multiply(2.3));
	FakeEntity fakeplayer = new FakeEntity(p, profile, spawnloc);

	fakeplayer.setRemovetime(10 * 1000);
	fakeplayer.damage();
	pd.setFakeplayer(fakeplayer);

	if (!pd.isFakeplayervisible()) {
	    fakeplayer.setInvisible();
	}

	pd.setFakeplayervisible(false);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = PlayerData.getPlayerData(p);
	FakeEntity fakeplayer = pd.getFakeplayer();
	Location loc = e.getFrom();

	if (fakeplayer == null) { return; }

	if (fakeplayer.getRemovetime() != 0) {
	    if (fakeplayer.getRemovetime() < System.currentTimeMillis()) {
		fakeplayer.kill();
		pd.setFakeplayer(null);
		return;
	    }
	}

	loc.add(0, p.getEyeHeight() * 1.2, 0);
	loc.setPitch(0);
	Vector mod = loc.getDirection();
	loc.subtract(MathUtils.rotate(mod, 100).multiply(2.3));

	fakeplayer.move(p.getLocation().getYaw(), p.getLocation().getPitch(), true, loc, false);
	fakeplayer.swingArm();
    }
}
