package me.Antikid.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
import me.Antikid.types.BanReason;
import me.Antikid.types.BanUtils;
import me.Antikid.types.DataManager;
import me.Antikid.types.Lag;
import me.Antikid.types.NettyInjection;
import me.Antikid.types.PlayerData;
import me.Antikid.types.Playerchecks;
import me.Antikid.types.Roast;

public class Main extends JavaPlugin implements Listener {

    public static long lastban;
    public static boolean ban = true;
    public static Main plugin;
    public static NettyInjection nettyInjection;

    public static Main getPlugin() {
	return plugin;
    }

    public void onEnable() {

	plugin = this;
	System.out.print("§cAntikid has been enabled!");

	LoadListeners();
	LoadCommands();
	LoadPlayerDatas();
	LoadCheatCheck();
	Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
	DataManager.loadAntikid();

	// nettyInjection = new NettyInjection(this, "AntiCheatSystem");
	// nettyInjection.addHandler("timerCheck", new PacketChecker());
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
	pm.registerEvents(new Playerchecks(), this);
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
	// PacketListenerAPI.addPacketHandler(new PacketChecker(getPlugin()));
    }

    @SuppressWarnings("deprecation")
    private void LoadPlayerDatas() {
	for (Player ps : Bukkit.getOnlinePlayers()) {
	    PlayerData pd = new PlayerData(ps);
	    pd.setLastOnGround(ps.getLocation());
	}
    }

    private void LoadCheatCheck() {
	new BukkitRunnable() {
	    @Override
	    public void run() {

		for (PlayerData pd : PlayerData.playerdatas.values()) {
		    Roast roast = pd.getRoast();

		    // Roasted
		    if (roast.getRoasts() >= 5 || roast.getRoasts() + (roast.getRoastsExperimental() / 2) >= 5 || roast.getRoastsExperimental() >= 10) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.UNFAIRADVANTAGE);
			roast.resetRoast();
			roast.resetRoastExperimental();
			continue;
		    }

		    // Fly
		    if (pd.getFly() > 10) {
			alert(pd.getPlayer(), "administration", "flyhack", pd.getFly());
			roast.addRoast();
		    }

		    if (pd.getFly() > 25) {
			roast.addRoast();
		    }

		    if (pd.getFly() > 40) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.FLY);
			roast.resetRoasts();
			pd.setFly(0);
			continue;
		    }
		    pd.setFly(0);

		    // Speed
		    if (pd.getSpeed() > 5) {
			alert(pd.getPlayer(), "administration", "speedhack", pd.getSpeed());
			roast.addRoast();
		    }

		    if (pd.getSpeed() > 15) {
			roast.addRoast();
		    }

		    if (pd.getSpeed() > 20) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.SPEED);
			pd.setSpeed(0);
			roast.resetRoasts();
			continue;
		    }
		    pd.setSpeed(0);

		    // VerticalSpeed
		    if (pd.getVerticalSpeed() > 1) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.VERTICALSPEED);
			pd.setVerticalSpeed(0);
			roast.resetRoasts();
			continue;
		    }
		    pd.setVerticalSpeed(0);

		    // Micromovement
		    if (pd.getMicroMovement() > 10) {
			alert(pd.getPlayer(), "administration", "micromovement", pd.getMicroMovement());
			roast.addRoast();
		    }

		    if (pd.getMicroMovement() > 15) {
			roast.addRoast();
		    }

		    if (pd.getMicroMovement() > 20) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.MICROMOVEMENT);
			pd.setMicroMovement(0);
			roast.resetRoasts();
			continue;
		    }
		    pd.setMicroMovement(0);

		    // Jesus
		    if (pd.getJesus() > 10) {
			alert(pd.getPlayer(), "administration", "jesus", pd.getJesus());
			roast.addRoast();
		    }

		    if (pd.getJesus() > 25) {
			roast.addRoast();
		    }

		    if (pd.getJesus() > 40) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.JESUS);
			pd.setJesus(0);
			roast.resetRoasts();
			continue;
		    }
		    pd.setJesus(0);

		    // Autoclicker
		    pd.setCps(pd.getClicks() / 5);

		    if (pd.getClicks() > 20 * 5) {
			alert(pd.getPlayer(), "administration", "autoclicker. He has " + pd.getClicks() / 5 + "cps", 0);
		    }

		    if (pd.getClicks() > 25 * 5 && pd.getClicks() < 60 * 5) {
			roast.addRoast();
		    }

		    if (pd.getClicks() > 60 * 5) {
			alert(pd.getPlayer(), "administration", "autoclicker. He has " + pd.getClicks() / 5 + "cps", 0);
			roast.addRoasts(2);
			pd.setClicks(0);
		    }
		    pd.setClicks(0);

		    // Movepackets
		    if (pd.getMovepackets() >= 110) {
			alert(pd.getPlayer(), "administration", "movepackets", pd.getMovepackets());
			roast.addRoast();
		    }
		    pd.setMovepackets(0);

		    // Heal
		    if (pd.getHeal() > 3) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.HEAL);
			pd.setHeal(0);
			roast.resetRoasts();
			continue;
		    }
		    pd.setHeal(0);

		    // NoSlowdown
		    if (pd.getNoslowdown() > 6) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.SLOWDOWN);
			pd.setNoslowdown(0);
			roast.resetRoasts();
			continue;
		    }
		    pd.setNoslowdown(0);

		    // FastLadder
		    if (pd.getLadder() > 3) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.FAST_LADDER);
			pd.setLadder(0);
			roast.resetRoasts();
			continue;
		    }
		    pd.setLadder(0);

		    // Reach
		    if (pd.getReach() > 10) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.REACH);
			pd.setReach(0);
			roast.resetRoasts();
			continue;
		    }

		    // Killaura
		    if (pd.getKillaura() > 17 && pd.getKillaura() < 25) {
			roast.addRoasts(2);
			pd.setKillaura(0);
			pd.setFakeplayervisible(true);
		    }

		    if (pd.getKillaura() > 25) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.KILLAURA);
			pd.setKillaura(0);
			roast.resetRoasts();
			continue;
		    }

		    // Nofall
		    if (pd.getNofall() > 10) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.NOFALL);
			pd.setNofall(0);
			roast.resetRoasts();
			continue;
		    }

		    // Velocity
		    if (pd.getVelocity() > 10) {
			BanUtils.ban(Bukkit.getConsoleSender(), pd.getPlayer(), BanReason.VELOCITY);
			pd.setVelocity(0);
			roast.resetRoasts();
			continue;
		    }
		}
	    }
	}.runTaskTimer(Main.getPlugin(), 0L, 5 * 20L);

	new BukkitRunnable() {

	    @Override
	    public void run() {

		for (PlayerData pd : PlayerData.playerdatas.values()) {
		    Roast roast = pd.getRoast();

		    roast.handleRoasts();
		    Reach.handleReach(pd);
		}
	    }
	}.runTaskTimer(Main.getPlugin(), 20L, 30 * 20L);
    }

    public static PlayerData getPlayerData(Player p) {
	PlayerData pd = PlayerData.playerdatas.get(p);
	return pd;
    }

    @SuppressWarnings("deprecation")
    public static void alert(Player p, String permission, String reason, double frequency) {

	int roasts = Main.getPlayerData(p).getRoast().getRoasts();

	for (Player ps : Bukkit.getOnlinePlayers()) {
	    // if (!ps.hasPermission(permission)) { return; }
	    if (!ps.hasPermission("Developer")) { return; }

	    ps.sendMessage(" ");
	    ps.sendMessage("§r[" + roasts + "]§6" + Bukkit.getConsoleSender().getName() + " detected the player " + p.getPlayer().getName());
	    ps.sendMessage("§r    - He might be using " + reason + ": " + frequency);
	    ps.sendMessage(" ");
	}

	// Console
	System.out.print(" ");
	System.out.print("§r[" + roasts + "]§6" + Bukkit.getConsoleSender().getName() + " detected the player " + p.getPlayer().getName());
	System.out.print("§r    - He might be using " + reason + ": " + frequency);
	System.out.print(" ");
    }

    public static void toggleBan() {
	Main.ban = !Main.ban;
    }

    public static boolean isBanning() {
	return Main.ban;
    }
}
