package net.oxyopia.vice.features.bosses

import net.minecraft.entity.boss.BossBar
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.BossBarEvents
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.TimeUtils.timeDeltaWithin
import kotlin.time.Duration.Companion.seconds

object Elderpork : Boss(
	World.Elderpork,
	Regex("Elderpork the Great - (?:(?<health>\\d+(?:\\.\\d+)?)/\\d+(?:\\.\\d+)?|YOU/SHOULD/KILL/YOURSELF) ❤ \\[PHASE (?<phase>\\d+)]"),
	phaseTimesSec = listOf(60 * 2, 60 * 10, 60 * 5, 60 * 2)
){
	private val MONITOR_MAX_TIME = 30.seconds

	private var monitorLastStart = -0L

	@SubscribeEvent
	fun onChatMessage(event: ChatEvent) {
		if (event.string.contains("is preparing to fight Elderpork the Great!") && event.hasNoSender) {
			monitorLastStart = System.currentTimeMillis()
		}
	}

	@SubscribeEvent
	fun onBossbarAfter(event: BossBarEvents.Insert) {
		if (!Vice.config.ELDERPORK_START_TIMER) return
		if (monitorLastStart.timeDeltaWithin(MONITOR_MAX_TIME)) {
			val elapsedTime = monitorLastStart.timeDelta()
			val percentageComplete = elapsedTime / MONITOR_MAX_TIME

			event.add(
				"Elderpork starts in ${elapsedTime.formatTimer(MONITOR_MAX_TIME, showClock = false)}".toText(Colors.Elderpork),
				1f - percentageComplete.toFloat(),
				BossBar.Color.PINK,
				BossBar.Style.NOTCHED_10
			)
		}
	}
}