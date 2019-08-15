package me.Antikid.module.modules.player;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.Antikid.main.Main;
import me.Antikid.module.Module;
import me.Antikid.types.ItemBuilder;
import me.Antikid.types.PlayerData;
import net.minecraft.server.v1_7_R4.AxisAlignedBB;

public class BlockGlitch extends Module implements Listener {

    public BlockGlitch() {
	super("BlockGlitch", new ItemBuilder.GlassPaneBuilder(DyeColor.RED).build().build());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent e) {

	if (e.isCancelled()) {
	    Block b = e.getBlock();
	    Player p = e.getPlayer();
	    PlayerData pd = Main.getPlayerData(p);

	    if (b.getType().isSolid() || b.getType() == Material.GLASS)
		pd.setLastBrokenBlock(b);
	}
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent e) {
	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	if (e.isCancelled() || !e.canBuild()) {

	    Block b = e.getBlockPlaced();
	    AxisAlignedBB playerbox = ((CraftPlayer) p).getHandle().boundingBox;
	    AxisAlignedBB blockbox = AxisAlignedBB.a(b.getX(), b.getY(), b.getZ(), b.getX() + 1, b.getY() + 2, b.getZ() + 1);
	    if (playerbox.b(blockbox)) {
		p.teleport(pd.getLastOnGround());
		p.sendMessage("§cBlock glitching is not allowed");
	    }
	} else {
	    pd.setLastOnGround(e.getBlock().getRelative(BlockFace.UP).getLocation());
	}
    }
}
