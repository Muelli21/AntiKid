package me.Antikid.module.modules.movement;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.Antikid.listener.MoveListener;
import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Playerchecks;
import net.minecraft.server.v1_7_R4.AxisAlignedBB;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.WorldServer;

public class Jesus extends Module implements Listener {

    public Jesus() {
	super("Jesus", new ItemBuilder(Material.WATER).build());
    }

    @EventHandler
    public void jesus(PlayerMoveEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (!isEnabled() || !MoveListener.checkAble(p)) { return; }

	CraftPlayer cp = (CraftPlayer) p;
	EntityPlayer ep = (EntityPlayer) cp.getHandle();
	WorldServer world = ((CraftWorld) p.getWorld()).getHandle();

	if (world.b(AxisAlignedBB.a(ep.boundingBox.a, ep.boundingBox.b + 0.5, ep.boundingBox.c, ep.boundingBox.d, ep.boundingBox.e, ep.boundingBox.f), net.minecraft.server.v1_7_R4.Material.WATER)
		|| world.b(AxisAlignedBB.a(ep.boundingBox.a, ep.boundingBox.b + 0.5, ep.boundingBox.c, ep.boundingBox.d, ep.boundingBox.e, ep.boundingBox.f),
			net.minecraft.server.v1_7_R4.Material.LAVA)) {
	    pd.setNormalcooldown(300);
	}

	if (Playerchecks.isNormal(p, e)) {
	    pd.setNormalcooldown(1000);
	}

	if (Playerchecks.isCreative(p)) { return; }
	if (System.currentTimeMillis() < pd.getNormalcooldown() || System.currentTimeMillis() < pd.getVelocitycooldown()) {
	    debug(p, Arrays.asList("normal- or velocitycooldown"));
	    return;
	}

	if (world.b(AxisAlignedBB.a(ep.boundingBox.a, ep.boundingBox.b, ep.boundingBox.c, ep.boundingBox.d, ep.boundingBox.b, ep.boundingBox.f), net.minecraft.server.v1_7_R4.Material.WATER) && world
		.b(AxisAlignedBB.a(ep.boundingBox.a, ep.boundingBox.b, ep.boundingBox.c, ep.boundingBox.d, ep.boundingBox.b + 0.25, ep.boundingBox.f), net.minecraft.server.v1_7_R4.Material.AIR)) {

	    pd.addJesus();
	    debug(p, Arrays.asList("Jesus"));
	}

	if (world.b(AxisAlignedBB.a(ep.boundingBox.a, ep.boundingBox.b, ep.boundingBox.c, ep.boundingBox.d, ep.boundingBox.b, ep.boundingBox.f), net.minecraft.server.v1_7_R4.Material.LAVA) && world
		.b(AxisAlignedBB.a(ep.boundingBox.a, ep.boundingBox.b, ep.boundingBox.c, ep.boundingBox.d, ep.boundingBox.b + 0.25, ep.boundingBox.f), net.minecraft.server.v1_7_R4.Material.AIR)) {

	    pd.addJesus();
	    debug(p, Arrays.asList("Jesus"));
	}
    }
}
