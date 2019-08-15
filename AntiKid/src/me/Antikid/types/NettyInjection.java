package me.Antikid.types;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.PacketLoginInStart;
import net.minecraft.server.v1_7_R4.ServerConnection;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.io.netty.channel.Channel;
import net.minecraft.util.io.netty.channel.ChannelDuplexHandler;
import net.minecraft.util.io.netty.channel.ChannelFuture;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import net.minecraft.util.io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.util.io.netty.channel.ChannelInitializer;
import net.minecraft.util.io.netty.channel.ChannelPipeline;
import net.minecraft.util.io.netty.channel.ChannelPromise;

public class NettyInjection {

    private String handlerName = "nettyinjection_packets";
    private Map<String, PacketHandler> handlerList = new HashMap<String, PacketHandler>();
    private Listener listener;

    private final HashMap<String, Channel> playerChannel = new HashMap<String, Channel>();

    private final List<Channel> globalChannel = new ArrayList<Channel>();
    private ChannelInboundHandlerAdapter globalHandler;

    public interface PacketHandler {

	public default Object onPacketIn(Player sender, Channel channel, Object packet) {
	    return packet;
	}

	public default Object onPacketOut(Player target, Channel channel, Object packet) {
	    return packet;
	}

	public default void exceptionCaught(Player player, Channel channel, Throwable throwable) {}

    }

    @SuppressWarnings("deprecation")
    public NettyInjection(Plugin plugin, String handlerName) {
	this.handlerName = "inject_" + handlerName;

	Bukkit.getPluginManager().registerEvents(this.listener = new Listener() {
	    @EventHandler
	    public final void onPlayerLogin(PlayerLoginEvent event) {
		try {
		    NettyInjection.this.inject(event.getPlayer());
		} catch (NullPointerException e) {
		    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Bitte versuche es nochmal!");
		    return;
		}
	    }

	    @EventHandler
	    public void onDisabled(PluginDisableEvent event) {
		if (event.getPlugin().equals(plugin))
		    NettyInjection.this.disable();
	    }
	}, plugin);

	ChannelInitializer<Channel> last = new ChannelInitializer<Channel>() {
	    @Override
	    protected void initChannel(Channel channel) throws Exception {
		NettyInjection.this.injectChannel(channel);
	    }
	};

	ChannelInitializer<Channel> first = new ChannelInitializer<Channel>() {
	    @Override
	    protected void initChannel(Channel channel) throws Exception {
		channel.pipeline().addLast(last);
	    }
	};

	this.globalHandler = new ChannelInboundHandlerAdapter() {
	    @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		((Channel) msg).pipeline().addFirst(first);
		super.channelRead(ctx, msg);
	    }
	};

	this.registerGlobalChannel();

	for (Player player : Bukkit.getOnlinePlayers())
	    this.inject(player);
    }

    @SuppressWarnings("unchecked")
    private final void registerGlobalChannel() {
	MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
	ServerConnection connection = server.getServerConnection();

	List<Object> channelFuture = (List<Object>) get(connection, "g");
	for (Object item : channelFuture) {
	    if (!ChannelFuture.class.isInstance(item))
		break;
	    Channel channel = ((ChannelFuture) item).channel();
	    this.globalChannel.add(channel);
	    channel.pipeline().addFirst("NettyInjectionGlobal", this.globalHandler);
	}
    }

    private final void unregisterGlobalChannel() {
	for (Channel global : this.globalChannel) {
	    final ChannelPipeline pipe = global.pipeline();
	    global.eventLoop().execute(() -> {
		pipe.remove("NettyInjectionGlobal");
	    });
	}
    }

    public final void addHandler(String name, PacketHandler handler) {
	this.handlerList.put(name, handler);
    }

    public final void removeHandler(String name) {
	if (this.handlerList.containsKey(name))
	    this.handlerList.remove(name);
    }

    public final void inject(Player player) {
	this.injectChannel(this.getChannel(player)).player = player;
    }

    public final void uninject(Player player) {
	this.uninjectChannel(this.getChannel(player));
    }

    private final Channel getChannel(Player player) {
	Channel channel = this.playerChannel.get(player.getName());
	if (channel == null) {
	    NetworkManager manager = ((CraftPlayer) player).getHandle().playerConnection.networkManager;
	    channel = (Channel) get(manager, "channel");
	    this.playerChannel.put(player.getName(), channel);
	}
	return channel;
    }

    private final PacketInjection injectChannel(Channel channel) {
	try {
	    PacketInjection handel = (PacketInjection) channel.pipeline().get(this.handlerName);
	    if (handel == null) {
		handel = new PacketInjection();
		channel.pipeline().addBefore("packet_handler", this.handlerName, handel);
	    }
	    return handel;
	} catch (Exception e) {

	}
	return null;
    }

    private final void uninjectChannel(Channel channel) {
	Object handel = channel.pipeline().get(this.handlerName);
	if (handel != null)
	    channel.pipeline().remove(this.handlerName);
    }

    @SuppressWarnings("deprecation")
    public final void disable() {
	for (Player player : Bukkit.getOnlinePlayers())
	    this.uninject(player);
	HandlerList.unregisterAll(this.listener);
	this.unregisterGlobalChannel();
    }

    private Object get(Object instance, String name) {
	try {
	    Field field = instance.getClass().getDeclaredField(name);
	    field.setAccessible(true);
	    return field.get(instance);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private class PacketInjection extends ChannelDuplexHandler {

	public Player player;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	    if (msg instanceof PacketLoginInStart)
		NettyInjection.this.playerChannel.put(((GameProfile) NettyInjection.this.get(msg, "a")).getName(), ctx.channel());
	    for (PacketHandler handel : NettyInjection.this.handlerList.values()) {
		if (msg == null)
		    break;
		msg = handel.onPacketIn(this.player, ctx.channel(), msg);
	    }
	    if (msg != null)
		super.channelRead(ctx, msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
	    for (PacketHandler handel : NettyInjection.this.handlerList.values())
		msg = handel.onPacketOut(this.player, ctx.channel(), msg);
	    super.write(ctx, msg, promise);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable throwable) throws Exception {
	    for (PacketHandler handel : NettyInjection.this.handlerList.values())
		handel.exceptionCaught(this.player, ctx.channel(), throwable);
	    super.exceptionCaught(ctx, throwable);
	}
    }
}