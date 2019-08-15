package me.Antikid.types;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum BanReason {

    UNFAIRADVANTAGE(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder.WoolBuilder(DyeColor.RED).build().setName("Unfair Advantage").build()),
    AUTOCLICKER(
	    30 * 24 * 60 * 60 * 1000,

	    new ItemBuilder(Material.GOLD_SWORD).setName("Autoclicker").build()),
    REACH(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.SKULL_ITEM).setName("Reach").build()),
    KILLAURA(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.SKULL_ITEM).setName("Killaura").build()),
    MODIFIED_CLIENT(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.REDSTONE_BLOCK).setName("Modified client").build()),
    SPEED(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.LEATHER_BOOTS).setName("Speed").build()),

    MICROMOVEMENT(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.GOLD_BOOTS).setName("MicroMovement").build()),

    FAST_LADDER(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.LADDER).setName("Fast ladder").build()),
    FLY(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.FEATHER).setName("Fly").build()),
    JESUS(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.WATER).setName("Jesus").build()),
    HEAL(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.BOW).setName("Heal or Fastbow").build()),
    NOFALL(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.ENDER_PEARL).setName("NoFall").build()),
    SLOWDOWN(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.SOUL_SAND).setName("NoSlowdown").build()),
    AIM(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.IRON_AXE).setName("Aim").build()),
    PACKETS(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.BOOK).setName("Packets").build()),
    VELOCITY(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.ANVIL).setName("Velocity").build()),
    VERTICALSPEED(
	    30 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.STRING).setName("VerticalSpeed").build()),
    REFUSING_SCREENSHARE(
	    20 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.SKULL_ITEM).setName("Screenshare refuse").build()),
    PERMANENT(
	    365 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.IRON_FENCE).setName("Permanent").build()),
    OTHER(
	    20 * 24 * 60 * 60 * 1000,
	    new ItemBuilder(Material.STONE).setName("Other...").build()),;

    private long banTime;
    private ItemStack invItem;

    private BanReason(long banTime, ItemStack invItem) {
	this.banTime = banTime;
	this.invItem = invItem;
    }

    public long getBanTime() {
	return banTime;
    }

    public String getName() {
	return this.toString().toLowerCase();
    }

    public ItemStack getInvItem() {
	return invItem;
    }
}
