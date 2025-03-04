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

object Vatican : Boss(
    World.Vatican,
    Regex("The Vatican - (?<health>\\d+(?:\\.\\d+)?)/\\d+(?:\\.\\d+)? ❤ \\[PHASE (?<phase>\\d)]"),
    phaseTimesSec = listOf(60 * 60, 60 * 60, 60 * 60)
) {
    private val MONITOR_MAX_TIME = 30.seconds

    private var monitorLastStart = -0L

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        if (event.string.contains("is preparing to fight The Vatican!") && event.hasNoSender) {
            monitorLastStart = System.currentTimeMillis()
        }
    }

    @SubscribeEvent
    fun onBossbarAfter(event: BossBarEvents.Insert) {
        if (!Vice.config.VATICAN_START_TIMER) return
        if (monitorLastStart.timeDeltaWithin(MONITOR_MAX_TIME)) {
            val elapsedTime = monitorLastStart.timeDelta()
            val percentageComplete = elapsedTime / MONITOR_MAX_TIME

            event.add(
                "THE VATICAN starts in ${elapsedTime.formatTimer(MONITOR_MAX_TIME, showClock = false)}".toText(Colors.Vatican),
                1f - percentageComplete.toFloat(),
                BossBar.Color.BLUE,
                BossBar.Style.NOTCHED_10
            )
        }
    }
}