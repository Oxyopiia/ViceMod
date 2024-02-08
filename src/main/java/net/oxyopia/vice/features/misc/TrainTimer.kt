package net.oxyopia.vice.features.misc

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.ServerChatMessageEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.timeDelta
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.Utils.ms
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.hours

object TrainTimer : HudElement("Train Timer", Vice.storage.showdown.trainTimerPos){
	private const val SPAWN_COOLDOWN_TIME_SECONDS = 45 * 60
	private const val SPAWN_MESSAGE = "The Train Conductor has returned!"
	private const val CONDUCTOR_NAME = "The Train Conductor"
	private const val PORTER_NAME = "Porter"

	private val spawnTime get() = Vice.storage.showdown.lastKnownTrainSpawn
	private var aliveCount = 0

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!Vice.config.TRAIN_TIMER) return
		if (!Vice.config.TRAIN_TIMER_OUTSIDE && !World.Showdown.isInWorld()) return

		val list: MutableList<String> = mutableListOf()

		val secondaryText = when {
			aliveCount > 1 && World.Showdown.isInWorld() -> "&&6${aliveCount - 1} Porters"

			aliveCount == 1 && World.Showdown.isInWorld() -> "&&aConductor Alive"

			spawnTime > 0 -> {
				val seconds = TimeUnit.MILLISECONDS.toSeconds(spawnTime.timeDelta()) % SPAWN_COOLDOWN_TIME_SECONDS
				val formatted = Utils.formatDuration(SPAWN_COOLDOWN_TIME_SECONDS - seconds)

				if (spawnTime.timeDelta() >= 6.hours.ms()) {
					"&&6Train arrives in: &&cUnknown"
				} else {
					"&&6Train arrives in: &&a${formatted}"
				}
			}

			else -> "&&6Train arrives in: &&cUnknown"
		}

		if (aliveCount > 0) {
			list.add("&&6&&lTrain Arrived!")
		}

		list.add(secondaryText)
		position.drawStrings(list, event.context)
	}

	@SubscribeEvent
	fun onChatEvent(event: ServerChatMessageEvent) {
		if (event.string.contains(SPAWN_MESSAGE)) {
			Vice.storage.showdown.lastKnownTrainSpawn = System.currentTimeMillis()
			Vice.storage.markDirty()
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

	override fun storePosition(position: Position) {
		Vice.storage.showdown.trainTimerPos = position
		Vice.storage.markDirty()
	}

	override fun shouldDraw(): Boolean = Vice.config.TRAIN_TIMER

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		val list = listOf(
			"&&6&&lTrain Arrived!",
			"&&6Next Train arrives in: &&a13:56"
		)

		return position.drawStrings(list, context)
	}
}
