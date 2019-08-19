package me.Antikid.gui;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.Antikid.types.BanReason;
import me.Antikid.utils.BanUtils;

public class BanGui extends AbstractGui {

    public BanGui(Player p, OfflinePlayer target) {
	super(p, 27, "ban", true);

	clearGui();

	for (BanReason reason : BanReason.values()) {
	    setItem(reason.ordinal(), reason.getInvItem(), new AbstractAction() {

		@Override
		public void click(Player player) {
		    BanUtils.ban(p, target, reason);
		    delete();
		}
	    }, null);
	}

	p.openInventory(getInventory());
    }
}
