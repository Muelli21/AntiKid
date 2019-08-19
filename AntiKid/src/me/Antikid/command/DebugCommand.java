package me.Antikid.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Antikid.types.PlayerData;

public class DebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player)) { return false; }

	Player p = (Player) sender;
	PlayerData pd = PlayerData.getPlayerData(p);

	if (args.length != 0) { return false; }

	pd.setDebug(!pd.isDebug());
	p.sendMessage("Debugmode: " + pd.isDebug());
	return false;
    }
}
