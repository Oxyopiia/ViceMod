package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.RenderInGameHudEvent
import net.oxyopia.vice.events.ServerChatMessageEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.enums.World
import java.util.concurrent.TimeUnit

object TrainTimer {
	private const val SPAWN_COOLDOWN_TIME_SECONDS = 45 * 60
	private const val SPAWN_MESSAGE = "The Train Conductor has returned!"
	private const val CONDUCTOR_NAME = "The Train Conductor"
	private const val PORTER_NAME = "Porter"

	private var spawnTime = 0L
	private var aliveCount = 0

	@SubscribeEvent
	fun onHudRender(event: RenderInGameHudEvent) {
		if (!Vice.config.TRAIN_TIMER) return
		if (!Vice.config.TRAIN_TIMER_OUTSIDE && !World.Showdown.isInWorld()) return

		var xPos = event.scaledWidth / 2
		var yPos = 260

		if (Vice.config.DEVMODE) {
			xPos = (event.scaledWidth / 2 - 1) + Vice.devConfig.TRAIN_TIMER_HUD_X_OFFSET_LOCATION
			yPos = (event.scaledHeight / 2 - 1) + Vice.devConfig.TRAIN_TIMER_HUD_Y_OFFSET_LOCATION
		}

		val matrices = event.context.matrices
		val textRenderer = MinecraftClient.getInstance().textRenderer

		val secondaryText = when {
			aliveCount > 1 && World.Showdown.isInWorld() -> "&&6${aliveCount - 1} Porters"

			aliveCount == 1 && World.Showdown.isInWorld() -> "&&aConductor Alive"

			spawnTime > 0 -> {
				val seconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - spawnTime) % SPAWN_COOLDOWN_TIME_SECONDS
				val formatted = Utils.formatDuration(SPAWN_COOLDOWN_TIME_SECONDS - seconds)
				"&&6Next Train arrives in: &&a${formatted}"
			}

			else -> "&&6Next Train arrives in: &&cUnknown"
		}

		if (aliveCount > 0) {
			HudUtils.drawText(matrices, textRenderer, "&&6&&lTrain Arrived", xPos, yPos, centered = true)
			yPos += 10
		}

		HudUtils.drawText(matrices, textRenderer, secondaryText, xPos, yPos, centered = true)
	}

	@SubscribeEvent
	fun onChatEvent(event: ServerChatMessageEvent) {
		if (event.content.string.contains(SPAWN_MESSAGE)) {
			spawnTime = System.currentTimeMillis()
			aliveCount = 3

			if (Vice.config.TRAIN_TIMER) Utils.playSound("block.bell.use", volume = 9999f)
		}
	}

	@SubscribeEvent
	fun onEntityDeath(event: EntityDeathEvent) {
		if (!World.Showdown.isInWorld()) return

		if (event.entity.customName.toString().contains(CONDUCTOR_NAME) || event.entity.customName.toString().contains(PORTER_NAME)) {
			aliveCount--
		}
	}
}
