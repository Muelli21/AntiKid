package me.Antikid.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;
import org.bukkit.material.Wool;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class ItemBuilder {

    protected ItemStack item;

    public ItemBuilder(Material type) {
	this.item = new ItemStack(type);
    }

    public ItemBuilder(ItemStack item) {
	this.item = item;
    }

    public ItemBuilder setUnbreakable() {
	item.getItemMeta().spigot().setUnbreakable(true);
	return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
	item.addUnsafeEnchantment(enchantment, level);
	return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
	for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
	    item.getItemMeta().addEnchant(entry.getKey(), entry.getValue(), true);
	}
	return this;
    }

    public ItemBuilder setName(String name) {
	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(name);
	item.setItemMeta(meta);
	return this;
    }

    public ItemBuilder setLore(List<String> lore) {
	ItemMeta meta = item.getItemMeta();
	meta.setLore(lore);
	item.setItemMeta(meta);
	return this;
    }

    public ItemStack build() {
	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(item.getItemMeta().getDisplayName());
	item.setItemMeta(meta);
	item.setAmount(1);
	return item;
    }

    public ItemStack build(int amount) {
	ItemMeta meta = item.getItemMeta();
	meta.setDisplayName(item.getItemMeta().getDisplayName());
	item.setItemMeta(meta);
	item.setAmount(amount);
	return item;
    }

    public static class WoolBuilder {

	protected Wool wool;

	public WoolBuilder(DyeColor color) {
	    this.wool = new Wool(color);
	}

	public ItemBuilder build() {
	    return new ItemBuilder(wool.toItemStack());
	}
    }

    public static class DyeBuilder {

	protected Dye dye;

	public DyeBuilder(DyeColor color) {
	    this.dye = new Dye();
	    this.dye.setColor(color);
	}

	public ItemBuilder build() {
	    return new ItemBuilder(dye.toItemStack());
	}
    }

    public static class PotionBuilder {

	protected Potion potion;

	public PotionBuilder(PotionType type, int level) {
	    this.potion = new Potion(type, level);
	}

	public PotionBuilder splash() {
	    potion.splash();
	    return this;
	}

	public PotionBuilder extend() {
	    potion.extend();
	    return this;
	}

	public ItemBuilder build() {
	    return new ItemBuilder(potion.toItemStack(1));
	}
    }

    public static class LeatherAmourBuilder {

	protected ItemStack item;

	public LeatherAmourBuilder(Material type, Color color) {

	    this.item = new ItemStack(type);
	    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
	    meta.setColor(color);
	    item.setItemMeta(meta);
	}

	public ItemBuilder build() {
	    return new ItemBuilder(item);
	}
    }

    public static class GlassPaneBuilder {

	protected ItemStack item;

	@SuppressWarnings("deprecation")
	public GlassPaneBuilder(DyeColor color) {
	    this.item = new ItemStack(Material.STAINED_GLASS_PANE, 1, color.getData());
	}

	public ItemBuilder build() {
	    return new ItemBuilder(item);
	}
    }

    public static class SkullHeadBuilder {

	protected ItemStack item;

	@SuppressWarnings("deprecation")
	public SkullHeadBuilder(OfflinePlayer player) {

	    ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3, (byte) SkullType.PLAYER.ordinal());
	    SkullMeta sm = (SkullMeta) skull.getItemMeta();
	    sm.setOwner(player.getName());
	    skull.setItemMeta(sm);

	    this.item = skull;
	}

	public ItemBuilder build() {
	    return new ItemBuilder(item);
	}
    }
}
