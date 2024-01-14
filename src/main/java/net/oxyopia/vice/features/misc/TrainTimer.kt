package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.EntitySpawnEvent
import net.oxyopia.vice.events.RenderInGameHudEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.enums.World
import java.util.concurrent.TimeUnit

object TrainTimer {
	val CONDUCTOR_NAME = "The Train Conductor" // The Train Conductor // Abducted Cow
	val PORTER_NAME = "Porter"

	var timeLeft = 0L
	var entity: Entity? = null
	var aliveCount = 0

	@SubscribeEvent
	fun onHudRender(event: RenderInGameHudEvent) {
		if (!Vice.config.TRAIN_TIMER || !World.Showdown.isInWorld()) return

		val xPos = event.scaledWidth - 160
		val yPos = 300

		if(aliveCount > 0) {

			HudUtils.drawText(
				event.context.matrices,
				MinecraftClient.getInstance().textRenderer,
				"Train Arrived!",
				xPos,
				yPos,
				centered = true
			)

			if(aliveCount == 1) {
				HudUtils.drawText(
					event.context.matrices,
					MinecraftClient.getInstance().textRenderer,
					"The Train Conductor - Alive",
					xPos,
					yPos + 10,
					centered = true
				)

				HudUtils.drawText(
					event.context.matrices,
					MinecraftClient.getInstance().textRenderer,
					"Porters - Dead",
					xPos,
					yPos + 20,
					centered = true
				)
			} else {
				HudUtils.drawText(
					event.context.matrices,
					MinecraftClient.getInstance().textRenderer,
					"The Train Conductor - Kill more ${aliveCount - 1} Porters",
					xPos,
					yPos + 10,
					centered = true
				)

				HudUtils.drawText(
					event.context.matrices,
					MinecraftClient.getInstance().textRenderer,
					"Porters - ${aliveCount - 1} Alive",
					xPos,
					yPos + 20,
					centered = true
				)
			}

		} else {

			val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft)

			HudUtils.drawText(
				event.context.matrices,
				MinecraftClient.getInstance().textRenderer,
				"Train arrive in: ${seconds} seconds",
				xPos,
				yPos,
				centered = true
			)
		}
	}

	@SubscribeEvent
	fun onTrainSpawned(event: EntitySpawnEvent) {
		if (!Vice.config.TRAIN_TIMER) return
		if (event.entity.customName.toString().contains(CONDUCTOR_NAME)) {
			aliveCount = 3
			Utils.playSound("block.bell.use", volume = 9999f)
		}
	}

	@SubscribeEvent
	fun onEntityDeath(event: EntityDeathEvent) {
		if (!Vice.config.TRAIN_TIMER || !World.Showdown.isInWorld()) return

		if (event.entity.customName.toString().contains(CONDUCTOR_NAME) || event.entity.customName.toString().contains(PORTER_NAME)) {
			aliveCount--
		}
	}
}
