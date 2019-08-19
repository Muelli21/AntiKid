package me.Antikid;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

import me.Antikid.command.AntiKidSettings;
import me.Antikid.command.BanCommand;
import me.Antikid.command.CheckCommand;
import me.Antikid.command.DebugCommand;
import me.Antikid.command.TempBanCommand;
import me.Antikid.command.UnbanCommand;
import me.Antikid.gui.AbstractGuiListener;
import me.Antikid.listener.JoinListener;
import me.Antikid.listener.MoveListener;
import me.Antikid.listener.PlayerListener;
import me.Antikid.managers.DataManager;
import me.Antikid.module.modules.combat.Aim;
import me.Antikid.module.modules.combat.AutoClicker;
import me.Antikid.module.modules.combat.HitCheck;
import me.Antikid.module.modules.combat.Killaura;
import me.Antikid.module.modules.combat.Reach;
import me.Antikid.module.modules.combat.Vape;
import me.Antikid.module.modules.combat.Velocity;
import me.Antikid.module.modules.movement.Direction;
import me.Antikid.module.modules.movement.FastLadder;
import me.Antikid.module.modules.movement.Fly;
import me.Antikid.module.modules.movement.FlyVelocity;
import me.Antikid.module.modules.movement.Glide;
import me.Antikid.module.modules.movement.Jesus;
import me.Antikid.module.modules.movement.MicroMovement;
import me.Antikid.module.modules.movement.NoFall;
import me.Antikid.module.modules.movement.NoSlowdown;
import me.Antikid.module.modules.movement.Speed;
import me.Antikid.module.modules.movement.VerticalSpeed;
import me.Antikid.module.modules.player.BlockGlitch;
import me.Antikid.module.modules.player.Heal;
import me.Antikid.module.modules.player.Packets;
import me.Antikid.module.modules.player.Timer;
import me.Antikid.types.PlayerData;
import me.Antikid.types.ViolationManagerRunnable;

public class Antikid extends JavaPlugin implements Listener {

    static AntikidData antikidData;

    public static Antikid getPlugin() {
	return antikidData.getPlugin();
    }

    public void onEnable() {

	antikidData = new AntikidData(this);
	System.out.print("§cAntikid has been enabled!");

	LoadListeners();
	LoadCommands();
	LoadPlayerDatas();
	DataManager.loadAntikid();
	Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new ViolationManagerRunnable(), 0, 5*20L);

    }

    public void onDisable() {
	DataManager.saveAntikid();
	System.out.print("§cAntikid has been disabled!");
    }

    private void LoadCommands() {

	getCommand("ban").setExecutor(new BanCommand());
	getCommand("tempban").setExecutor(new TempBanCommand());
	getCommand("unban").setExecutor(new UnbanCommand());
	getCommand("check").setExecutor(new CheckCommand());
	getCommand("debug").setExecutor(new DebugCommand());
	getCommand("asettings").setExecutor(new AntiKidSettings());
    }

    private void LoadListeners() {
	PluginManager pm = Bukkit.getPluginManager();
	pm.registerEvents(new JoinListener(), this);
	pm.registerEvents(new MoveListener(), this);
	pm.registerEvents(new PlayerListener(), this);
	pm.registerEvents(new AbstractGuiListener(), this);

	// Modules
	pm.registerEvents(new BlockGlitch(), this);
	pm.registerEvents(new HitCheck(), this);
	pm.registerEvents(new MicroMovement(), this);
	pm.registerEvents(new Speed(), this);
	pm.registerEvents(new Fly(), this);
	pm.registerEvents(new FastLadder(), this);
	pm.registerEvents(new Timer(), this);
	pm.registerEvents(new Direction(), this);
	pm.registerEvents(new Jesus(), this);
	pm.registerEvents(new Heal(), this);
	pm.registerEvents(new Killaura(), this);
	pm.registerEvents(new Aim(), this);
	pm.registerEvents(new Velocity(), this);
	pm.registerEvents(new Reach(), this);
	pm.registerEvents(new AutoClicker(), this);
	pm.registerEvents(new NoFall(), this);
	pm.registerEvents(new NoSlowdown(), this);
	pm.registerEvents(new Vape(), this);
	pm.registerEvents(new VerticalSpeed(), this);
	pm.registerEvents(new Glide(), this);
	pm.registerEvents(new FlyVelocity(), this);

	ProtocolLibrary.getProtocolManager().addPacketListener(new Packets());
    }

    @SuppressWarnings("deprecation")
    private void LoadPlayerDatas() {
	for (Player ps : Bukkit.getOnlinePlayers()) {
	    new PlayerData(ps);
	}
    }
}
