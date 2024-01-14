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
	val CONDUCTOR_NAME = "The Train Conductor"
	val PORTER_NAME = "Porter"

	var timeLeft = 0L
	var entity: Entity? = null
	var aliveCount = 0

	@SubscribeEvent
	fun onHudRender(event: RenderInGameHudEvent) {
		if (!Vice.config.TRAIN_TIMER || !World.Showdown.isInWorld()) return

		val xPos = event.scaledWidth / 2
		val yPos = 260

		if(aliveCount > 0) {

			HudUtils.drawText(
				event.context.matrices,
				MinecraftClient.getInstance().textRenderer,
				"&&6&&lTrain Arrived!",
				xPos,
				yPos,
				centered = true
			)

			if(aliveCount == 1) {
				HudUtils.drawText(
					event.context.matrices,
					MinecraftClient.getInstance().textRenderer,
					"&&aConductor Alive",
					xPos,
					yPos + 10,
					centered = true
				)

			} else {
				HudUtils.drawText(
					event.context.matrices,
					MinecraftClient.getInstance().textRenderer,
					"&&6${aliveCount - 1} Porters",
					xPos,
					yPos + 10,
					centered = true
				)
			}

		} else {

			val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft)

			HudUtils.drawText(
				event.context.matrices,
				MinecraftClient.getInstance().textRenderer,
				"&&6Train arrives in: &&a${seconds} seconds",
				xPos,
				yPos,
				centered = true
			)
		}
	}

	@SubscribeEvent
	fun onTrainSpawned(event: EntitySpawnEvent) {
		if (event.entity.customName.toString().contains(CONDUCTOR_NAME)) {
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
