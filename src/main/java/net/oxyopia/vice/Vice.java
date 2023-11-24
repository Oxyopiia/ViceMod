package net.oxyopia.vice;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.oxyopia.vice.commands.ViceCommand;
import net.oxyopia.vice.commands.TestCommand;
import net.oxyopia.vice.config.*;
import net.oxyopia.vice.events.core.EventManager;
import net.oxyopia.vice.features.misc.Fishing;
import net.oxyopia.vice.features.misc.PlacePlayerHeadBlocker;
import net.oxyopia.vice.features.misc.RevolverBlindnessHider;
import net.oxyopia.vice.features.arenas.ArenaEffectNotification;
import net.oxyopia.vice.features.arenas.ArenaSession;
import net.oxyopia.vice.features.hud.GamingMode;
import net.oxyopia.vice.features.itemabilities.ItemAbilityCooldown;
import net.oxyopia.vice.utils.Utils;
import org.slf4j.Logger;

public class Vice implements ClientModInitializer {
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final EventManager EVENT_MANAGER = new EventManager();
	public static final Config config = new Config();
	public static final DevConfig devConfig = new DevConfig();
	public static MinecraftClient client;

	public static final String chatPrefix = "§bVice §7|§r ";
	public static final String errorPrefix = "§bVice §cERROR §7|§c ";
	public static final String warningPrefix = "§bVice §eWARN §7|§e ";
	public static final String devPrefix = "§9Vice §7(Dev) |§r ";

	@Override
	public void onInitializeClient() {
		config.init();
		devConfig.init();

		client = MinecraftClient.getInstance();

		subscribeEventListeners();
		initConnectionEvents();
		registerCommands();
	}

	private void registerCommands() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			ViceCommand.register(dispatcher);
			TestCommand.register(dispatcher);
		});
	}

	private void initConnectionEvents() {
		ClientPlayConnectionEvents.DISCONNECT.register((phase, listener) -> Utils.inDoomTowers = false);
		ClientPlayConnectionEvents.JOIN.register((listener, packetSender, minecraftClient) -> {
			Utils.inDoomTowers = false;
			// Set to false in case server we are switching to does not have a scoreboard, and then let
			// MixinInGameHud#onRenderScoreboardSidebar re update if in DoomTowers
		});
	}

	private void subscribeEventListeners() {
		EVENT_MANAGER.subscribe(ArenaSession.INSTANCE);
		EVENT_MANAGER.subscribe(ArenaEffectNotification.INSTANCE);
		EVENT_MANAGER.subscribe(ItemAbilityCooldown.Companion);
		EVENT_MANAGER.subscribe(PlacePlayerHeadBlocker.INSTANCE);
		EVENT_MANAGER.subscribe(RevolverBlindnessHider.INSTANCE);
		EVENT_MANAGER.subscribe(GamingMode.INSTANCE);
		EVENT_MANAGER.subscribe(Fishing.INSTANCE);
	}
}

