package net.oxyopia.vice.features.misc

import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.ServerChatMessageEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.timeDelta
import net.oxyopia.vice.data.World
import net.oxyopia.vice.utils.HudUtils.drawStrings
import java.util.concurrent.TimeUnit

object TrainTimer {
	private const val SPAWN_COOLDOWN_TIME_SECONDS = 45 * 60
	private const val SPAWN_MESSAGE = "The Train Conductor has returned!"
	private const val CONDUCTOR_NAME = "The Train Conductor"
	private const val PORTER_NAME = "Porter"

	private var spawnTime = 0L
	private var aliveCount = 0

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!Vice.config.TRAIN_TIMER) return
		if (!Vice.config.TRAIN_TIMER_OUTSIDE && !World.Showdown.isInWorld()) return

		val pos = Position(event.scaledWidth / 2f, 260f)
		val list: MutableList<String> = mutableListOf()

		if (Vice.config.DEVMODE) {
			pos.x = (event.scaledWidth / 2f - 1) + Vice.devConfig.TRAIN_TIMER_HUD_X_OFFSET_LOCATION
			pos.y = (event.scaledHeight / 2f - 1) + Vice.devConfig.TRAIN_TIMER_HUD_Y_OFFSET_LOCATION
		}

		val secondaryText = when {
			aliveCount > 1 && World.Showdown.isInWorld() -> "&&6${aliveCount - 1} Porters"

			aliveCount == 1 && World.Showdown.isInWorld() -> "&&aConductor Alive"

			spawnTime > 0 -> {
				val seconds = TimeUnit.MILLISECONDS.toSeconds(spawnTime.timeDelta()) % SPAWN_COOLDOWN_TIME_SECONDS
				val formatted = Utils.formatDuration(SPAWN_COOLDOWN_TIME_SECONDS - seconds)
				"&&6Next Train arrives in: &&a${formatted}"
			}

			else -> "&&6Next Train arrives in: &&cUnknown"
		}

		if (aliveCount > 0) {
			list.add("&&6&&lTrain Arrived!")
		}

		list.add(secondaryText)
		pos.drawStrings(list, event.context)
	}

	@SubscribeEvent
	fun onChatEvent(event: ServerChatMessageEvent) {
		if (event.string.contains(SPAWN_MESSAGE)) {
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
