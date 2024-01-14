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
import java.awt.Color
import net.oxyopia.vice.utils.enums.World
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object TrainTimer {
	val CONDUCTOR_NAME = "The Train Conductor"
	val PORTER_NAME = "Porter"

	var cooldownEndTime: Long = 0
	val cooldownDurationSeconds: Long = 60 * 40
	val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
	var periodicTask: ScheduledFuture<*>? = null
	var periodicTask2: ScheduledFuture<*>? = null
	// val entityType = "entity.minecraft.wither_skeleton" // entity.minecraft.wither_skeleton // entity.minecraft.zombie
	val entityName = "The Train Conductor" // The Train Conductor // Abducted Cow
	val entityName2 = "Porter"
	var timeLeft = 0L
	var entity: Entity? = null
	var aliveCount = 0

	@SubscribeEvent
	fun onHudRender(event: RenderInGameHudEvent) {
		if (!Vice.config.TRAIN_TIMER || !World.Showdown.isInWorld()) return

		val xPos = event.scaledWidth - 160
		var yPos = 300

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
		if (event.entity.customName.toString().contains(entityName)) {
			aliveCount = 3
			Utils.playSound("block.bell.use", volume = 9999f)

			startCooldown()
			if (periodicTask == null) {
				periodicTask = scheduler.scheduleAtFixedRate({
					if (isOnCooldown()) {
						timeLeft = cooldownEndTime - System.currentTimeMillis()

						if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) < 10) {
							// DevUtils.sendDebugChat("Cooldown: ${TimeUnit.MILLISECONDS.toSeconds(timeLeft)} seconds left")
						}
					} else {
						// once timer end
						periodicTask?.cancel(true)
						periodicTask = null
					}
				}, 0, 1, TimeUnit.SECONDS)
			}
		}
	}

	@SubscribeEvent
	fun onEntityDeath(event: EntityDeathEvent) {
		if (!Vice.config.TRAIN_TIMER || !World.Showdown.isInWorld() || !conductorIsAlive) return

		if (event.entity.customName.toString().contains(CONDUCTOR_NAME) || event.entity.customName.toString().contains(PORTER_NAME)) {
			aliveCount--
		}
	}

	fun startCooldown() {
		cooldownEndTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(cooldownDurationSeconds)
	}

	fun isOnCooldown(): Boolean {
		return System.currentTimeMillis() < cooldownEndTime
	}
}
