package net.oxyopia.vice.features.summer

import net.minecraft.entity.boss.BossBar
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.BossBarEvents
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.TimeUtils.timeDeltaWithin
import java.awt.Color
import kotlin.time.Duration.Companion.seconds

object BarTimer {
	private val BAR_MAX_TIME = 46.seconds

	@SubscribeEvent
	fun onBossbarAfter(event: BossBarEvents.Insert) {
		if (!Vice.config.SUMMER_BAR_MINIGAME_BOSSBAR || !World.Summer.isInWorld()) return

		if (SummerAPI.lastBarStart.timeDeltaWithin(BAR_MAX_TIME)) {
			val elapsedTime = SummerAPI.lastBarStart.timeDelta()
			val percentageComplete = elapsedTime / BAR_MAX_TIME
			event.add(
				"Bar Time Left ${elapsedTime.formatTimer(BAR_MAX_TIME)}".toText(Color(122, 252, 133)),
				1f - percentageComplete.toFloat(),
				BossBar.Color.GREEN,
				BossBar.Style.NOTCHED_10
			)
		}
	}
}