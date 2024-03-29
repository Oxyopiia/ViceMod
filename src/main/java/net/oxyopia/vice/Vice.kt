package net.oxyopia.vice

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
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
import net.oxyopia.vice.commands.DevDataCommand
import net.oxyopia.vice.commands.EventTreeCommand
import net.oxyopia.vice.commands.ViceCommand
import net.oxyopia.vice.config.Config
import net.oxyopia.vice.config.DevConfig
import net.oxyopia.vice.config.Storage
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.CommandRegisterEvent
import net.oxyopia.vice.events.core.EventManager
import net.oxyopia.vice.features.RenderTest
import net.oxyopia.vice.features.arenas.ArenaAPI
import net.oxyopia.vice.features.arenas.ArenaNotifications
import net.oxyopia.vice.features.arenas.ArenaSession
import net.oxyopia.vice.features.arenas.LiveArenaInformation
import net.oxyopia.vice.features.auxiliary.exonitas.BabyMode
import net.oxyopia.vice.features.auxiliary.exonitas.CitySpamHiders
import net.oxyopia.vice.features.auxiliary.exonitas.PowerBoxTimer
import net.oxyopia.vice.features.bosses.*
import net.oxyopia.vice.features.cooking.CookingAPI
import net.oxyopia.vice.features.cooking.OrderTracker
import net.oxyopia.vice.features.cooking.CurrentOrderDisplay
import net.oxyopia.vice.features.hud.GamingMode
import net.oxyopia.vice.features.itemabilities.AbilitySoundChanger
import net.oxyopia.vice.features.itemabilities.ItemAbilityCooldown
import net.oxyopia.vice.features.itemabilities.CooldownDisplayChanger
import net.oxyopia.vice.features.itemabilities.ItemDetailDisplay
import net.oxyopia.vice.features.misc.*
import net.oxyopia.vice.utils.HudUtils
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

		var isFirstUse = false

		@JvmField
		val EVENT_MANAGER: EventManager = EventManager()

		@JvmField
		val config: Config = Config()

		@JvmField
		val devConfig: DevConfig = DevConfig()

		@JvmField
		var storage: Storage = Storage()

		@JvmField
		val gson: Gson = GsonBuilder()
			.setPrettyPrinting()
			.excludeFieldsWithoutExposeAnnotation()
			.serializeSpecialFloatingPointValues()
			.enableComplexMapKeySerialization()
			.registerTypeAdapter(World::class.java, object : TypeAdapter<World>() {
				override fun write(out: JsonWriter, value: World) {
					out.value(value.id)
				}

				override fun read(reader: JsonReader): World {
					val text = reader.nextString()
					return World.getById(text) ?: error("Could not parse World from $text")
				}
			}.nullSafe())
			.create()

		const val CHAT_PREFIX: String = "§bVice §7|§r "
		const val ERROR_PREFIX: String = "§cVice §cERROR §7|§c "
		const val WARNING_PREFIX: String = "§eVice §eWARN §7|§e "
		const val DEV_PREFIX: String = "§9Vice §7(Dev) |§r "
	}

	override fun onInitializeClient() {
		config.init()
		devConfig.init()
		storage.initialize()

		subscribeEventListeners()
		initConnectionEvents()
		registerCommands()

		if (storage.lastVersion != version.friendlyString) {
			storage.lastVersion = version.friendlyString
			storage.markDirty()
		}

		if (storage.isFirstUse) {
			isFirstUse = true
			storage.isFirstUse = false
			storage.markDirty()
		}
	}

	private fun registerCommands() {
		ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>?, _: CommandRegistryAccess? ->
			dispatcher?.let {
				ViceCommand.register(it)
				DevDataCommand.register(it)
				EventTreeCommand.register(it)
				EVENT_MANAGER.publish(CommandRegisterEvent(it))
			}
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
		EVENT_MANAGER.subscribe(BackpackRenaming)
		EVENT_MANAGER.subscribe(ChatFilter)
		EVENT_MANAGER.subscribe(Fishing)
		EVENT_MANAGER.subscribe(RevolverBlindnessHider)
		EVENT_MANAGER.subscribe(ConsumeItemBlocker)
		EVENT_MANAGER.subscribe(DailyRewardsNotification)
		EVENT_MANAGER.subscribe(TrainTimer)
		EVENT_MANAGER.subscribe(PlayerStats)

		EVENT_MANAGER.subscribe(GamingMode)
		EVENT_MANAGER.subscribe(HudUtils)
		EVENT_MANAGER.subscribe(BossCounter)

		EVENT_MANAGER.subscribe(ItemAbilityCooldown)
		EVENT_MANAGER.subscribe(AbilitySoundChanger)
		EVENT_MANAGER.subscribe(CooldownDisplayChanger)
		EVENT_MANAGER.subscribe(ItemDetailDisplay)

		EVENT_MANAGER.subscribe(CookingAPI)
		EVENT_MANAGER.subscribe(OrderTracker)
		EVENT_MANAGER.subscribe(CurrentOrderDisplay)

		EVENT_MANAGER.subscribe(EvanNotification)
		EVENT_MANAGER.subscribe(EvanSolver)

		EVENT_MANAGER.subscribe(ArenaAPI)
		EVENT_MANAGER.subscribe(ArenaSession)
		EVENT_MANAGER.subscribe(ArenaNotifications)
		EVENT_MANAGER.subscribe(LiveArenaInformation)

		EVENT_MANAGER.subscribe(CitySpamHiders)
		EVENT_MANAGER.subscribe(PowerBoxTimer)
		EVENT_MANAGER.subscribe(BabyMode)

		EVENT_MANAGER.subscribe(ViceBoss)
		EVENT_MANAGER.subscribe(MinehutBoss)
		EVENT_MANAGER.subscribe(AbyssalVice)
		EVENT_MANAGER.subscribe(ShadowGelato)
		EVENT_MANAGER.subscribe(ElGelato)
		EVENT_MANAGER.subscribe(PPP)

		EVENT_MANAGER.subscribe(RenderTest)
	}
}

