package net.oxyopia.vice.features.hud

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.SoundUtils
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

object TrainTimer : HudElement("Train Timer", Vice.storage.showdown.trainTimerPos){
	private const val SPAWN_COOLDOWN_TIME_SECONDS = 45 * 60
	private const val SPAWN_MESSAGE = "The Train Conductor has returned!"
	private const val CONDUCTOR_NAME = "The Train Conductor"
	private const val PORTER_NAME = "Porter"

	private val spawnTime get() = Vice.storage.showdown.lastKnownTrainSpawn
	private var aliveCount = 0

	override fun shouldDraw(): Boolean = Vice.config.TRAIN_TIMER
	override fun drawCondition(): Boolean = Vice.config.TRAIN_TIMER_OUTSIDE || World.Showdown.isInWorld()

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!Vice.config.TRAIN_TIMER || !drawCondition()) return

		val list: MutableList<String> = mutableListOf()

		if (World.Showdown.isInWorld()) {
			val text = "&&6&&lTrain Arrived!" + when {
				aliveCount > 1 -> " &&e${aliveCount - 1} Porters"
				aliveCount == 1 -> " &&aVulnerable"
				else -> ""
			}

			list.add(text)
		}

		val diff = spawnTime.timeDelta()
		val seconds = diff.inWholeSeconds % SPAWN_COOLDOWN_TIME_SECONDS
		val formatted = (SPAWN_COOLDOWN_TIME_SECONDS - seconds).seconds.formatDuration(false)

		list.add(
			"&&6Next train in " + when {
				diff <= 12.hours -> "&&a${formatted}"
				diff > 12.hours -> "&&cUnknown &&eInaccurate!"
				else -> "&&cUnknown"
			}
		)

		position.drawStrings(list, event.context)
	}

	@SubscribeEvent
	fun onChatEvent(event: ChatEvent) {
		if (event.string.contains(SPAWN_MESSAGE)) {
			Vice.storage.showdown.lastKnownTrainSpawn = System.currentTimeMillis()
			Vice.storage.markDirty()
			aliveCount = 3

			if (Vice.config.TRAIN_TIMER) SoundUtils.playSound("block.bell.use", volume = 9999f)
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

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		val list = listOf(
			"&&6&&lTrain Arrived! &&e2 Porters",
			"&&6Next train in: &&a13:56"
		)

		return position.drawStrings(list, context)
	}
}
