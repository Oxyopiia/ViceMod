package net.oxyopia.vice

import com.mojang.brigadier.CommandDispatcher
import com.mojang.logging.LogUtils
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.Version
import net.fabricmc.loader.api.metadata.ModMetadata
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.command.CommandRegistryAccess
import net.oxyopia.vice.commands.TestCommand
import net.oxyopia.vice.commands.ViceCommand
import net.oxyopia.vice.config.Config
import net.oxyopia.vice.config.DevConfig
import net.oxyopia.vice.events.core.EventManager
import net.oxyopia.vice.features.arenas.ArenaEffectNotification
import net.oxyopia.vice.features.arenas.ArenaSession
import net.oxyopia.vice.features.bosses.PPP
import net.oxyopia.vice.features.hud.GamingMode
import net.oxyopia.vice.features.itemabilities.AbilitySoundChanger
import net.oxyopia.vice.features.itemabilities.ItemAbilityCooldown
import net.oxyopia.vice.features.misc.Fishing
import net.oxyopia.vice.features.misc.PlacePlayerHeadBlocker
import net.oxyopia.vice.features.misc.RevolverBlindnessHider
import net.oxyopia.vice.features.misc.World4Features
import net.oxyopia.vice.utils.Utils.inDoomTowers
import org.slf4j.Logger

class Vice : ClientModInitializer {
	companion object {
		const val MOD_ID = "vice"

		private val metadata: ModMetadata by lazy {
			FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().metadata
		}

		val version: Version by lazy { metadata.version }

		val logger: Logger = LogUtils.getLogger()

		@JvmField
		val EVENT_MANAGER: EventManager = EventManager()

		@JvmField
		val config: Config = Config()

		@JvmField
		val devConfig: DevConfig = DevConfig()

		const val CHAT_PREFIX: String = "§bVice §7|§r "
		const val ERROR_PREFIX: String = "§cVice §cERROR §7|§c "
		const val WARNING_PREFIX: String = "§eVice §eWARN §7|§e "
		const val DEV_PREFIX: String = "§9Vice §7(Dev) |§r "
	}

	override fun onInitializeClient() {
		config.init()
		devConfig.init()

		subscribeEventListeners()
		initConnectionEvents()
		registerCommands()
	}

	private fun registerCommands() {
		ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>?, _: CommandRegistryAccess? ->
			ViceCommand.register(dispatcher)
			TestCommand.register(dispatcher)
		})
	}

	private fun initConnectionEvents() {
		// If still in DoomTowers, will be updated back to true by Mixin
		ClientPlayConnectionEvents.DISCONNECT.register(ClientPlayConnectionEvents.Disconnect { _: ClientPlayNetworkHandler?, _: MinecraftClient? ->
			inDoomTowers = false
		})
		ClientPlayConnectionEvents.JOIN.register(ClientPlayConnectionEvents.Join { _: ClientPlayNetworkHandler?, _: PacketSender?, _: MinecraftClient? ->
			inDoomTowers = false
		})
	}

	private fun subscribeEventListeners() {
		EVENT_MANAGER.subscribe(ArenaSession)
		EVENT_MANAGER.subscribe(ArenaEffectNotification)
		EVENT_MANAGER.subscribe(AbilitySoundChanger)
		EVENT_MANAGER.subscribe(ItemAbilityCooldown)
		EVENT_MANAGER.subscribe(PlacePlayerHeadBlocker)
		EVENT_MANAGER.subscribe(RevolverBlindnessHider)
		EVENT_MANAGER.subscribe(GamingMode)
		EVENT_MANAGER.subscribe(Fishing)
		EVENT_MANAGER.subscribe(PPP)
		EVENT_MANAGER.subscribe(World4Features)
	}
}

