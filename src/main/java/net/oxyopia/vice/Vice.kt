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
import net.oxyopia.vice.data.Colors
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
import net.oxyopia.vice.features.cooking.BurgerTimer
import net.oxyopia.vice.features.cooking.CookingAPI
import net.oxyopia.vice.features.cooking.OrderTracker
import net.oxyopia.vice.features.cooking.CurrentOrderDisplay
import net.oxyopia.vice.features.expeditions.DefibCounter
import net.oxyopia.vice.features.expeditions.DoubleTapDrop
import net.oxyopia.vice.features.expeditions.ExpeditionAPI
import net.oxyopia.vice.features.expeditions.MerchantOverlay
import net.oxyopia.vice.features.expeditions.RoomWaypoints
import net.oxyopia.vice.features.expeditions.RunOverview
import net.oxyopia.vice.features.expeditions.StylePointsHider
import net.oxyopia.vice.features.hud.CaveInPrediction
import net.oxyopia.vice.features.hud.DeliveryTimer
import net.oxyopia.vice.features.hud.ForgeTimers
import net.oxyopia.vice.features.hud.GamingMode
import net.oxyopia.vice.features.hud.PlayerStats
import net.oxyopia.vice.features.hud.TrainTimer
import net.oxyopia.vice.features.itemabilities.AbilitySoundChanger
import net.oxyopia.vice.features.itemabilities.ItemAbilityCooldown
import net.oxyopia.vice.features.itemabilities.CooldownDisplayChanger
import net.oxyopia.vice.features.itemabilities.SetHighlighting
import net.oxyopia.vice.features.misc.*
import net.oxyopia.vice.features.summer.BarTimer
import net.oxyopia.vice.features.summer.FishingDropsTracker
import net.oxyopia.vice.features.summer.SummerAPI
import net.oxyopia.vice.features.summer.SummerTimers
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.Utils.inDoomTowers
import org.slf4j.Logger

class Vice : ClientModInitializer {
	companion object {
		private const val MOD_ID = "vice"

		private val metadata: ModMetadata by lazy {
			FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().metadata
		}

		val version: Version by lazy { metadata.version }

		val logger: Logger = LogUtils.getLogger()

		var isFirstUse = false

		@JvmField
		val EVENT_MANAGER = EventManager()

		@JvmField
		val config = Config()

		@JvmField
		val devConfig = DevConfig()

		@JvmField
		var storage = Storage()

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

		const val CHAT_PREFIX = "§bVice §7|§r "
		const val ERROR_PREFIX = "§cVice §cERROR §7|§c "
		const val WARNING_PREFIX = "§eVice §eWARN §7|§e "
		const val DEV_PREFIX = "§9Vice §7(Dev) |§r "
		val PRIMARY = Colors.Wave
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
		EVENT_MANAGER.subscribe(CaveInPrediction)
		EVENT_MANAGER.subscribe(ChatFilter)
		EVENT_MANAGER.subscribe(ConsumeItemBlocker)
		EVENT_MANAGER.subscribe(Fishing)
		EVENT_MANAGER.subscribe(ItemProtection)
		EVENT_MANAGER.subscribe(RevolverBlindnessHider)
		EVENT_MANAGER.subscribe(WasteyardTimer)
		EVENT_MANAGER.subscribe(YetiHeadWarning)

		EVENT_MANAGER.subscribe(BossCounter)
		EVENT_MANAGER.subscribe(ForgeTimers)
		EVENT_MANAGER.subscribe(GamingMode)
		EVENT_MANAGER.subscribe(HudUtils)
		EVENT_MANAGER.subscribe(PlayerStats)
		EVENT_MANAGER.subscribe(TrainTimer)

		EVENT_MANAGER.subscribe(AbilitySoundChanger)
		EVENT_MANAGER.subscribe(CooldownDisplayChanger)
		EVENT_MANAGER.subscribe(ItemAbilityCooldown)
		EVENT_MANAGER.subscribe(SetHighlighting)

		EVENT_MANAGER.subscribe(BurgerTimer)
		EVENT_MANAGER.subscribe(CurrentOrderDisplay)
		EVENT_MANAGER.subscribe(CookingAPI)
		EVENT_MANAGER.subscribe(OrderTracker)

		EVENT_MANAGER.subscribe(EvanNotification)
//		EVENT_MANAGER.subscribe(EvanSolver)
		EVENT_MANAGER.subscribe(DeliveryTimer)

		EVENT_MANAGER.subscribe(ArenaAPI)
		EVENT_MANAGER.subscribe(ArenaNotifications)
		EVENT_MANAGER.subscribe(ArenaSession)
		EVENT_MANAGER.subscribe(LiveArenaInformation)

		EVENT_MANAGER.subscribe(BarTimer)
		EVENT_MANAGER.subscribe(FishingDropsTracker)
		EVENT_MANAGER.subscribe(SummerAPI)
		EVENT_MANAGER.subscribe(SummerTimers)

		EVENT_MANAGER.subscribe(DefibCounter)
		EVENT_MANAGER.subscribe(DoubleTapDrop)
		EVENT_MANAGER.subscribe(ExpeditionAPI)
		EVENT_MANAGER.subscribe(MerchantOverlay)
		EVENT_MANAGER.subscribe(RoomWaypoints)
		EVENT_MANAGER.subscribe(RunOverview)
		EVENT_MANAGER.subscribe(StylePointsHider)

		EVENT_MANAGER.subscribe(BabyMode)
		EVENT_MANAGER.subscribe(CitySpamHiders)
		EVENT_MANAGER.subscribe(PowerBoxTimer)

		EVENT_MANAGER.subscribe(AbyssalVice)
		EVENT_MANAGER.subscribe(Elderpork)
		EVENT_MANAGER.subscribe(ElGelato)
		EVENT_MANAGER.subscribe(MinehutBoss)
		EVENT_MANAGER.subscribe(PPP)
		EVENT_MANAGER.subscribe(ShadowGelato)
		EVENT_MANAGER.subscribe(ViceBoss)

		EVENT_MANAGER.subscribe(RenderTest)
	}
}

