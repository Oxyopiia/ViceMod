package net.oxyopia.vice.features.bosses

import net.minecraft.entity.boss.BossBar
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.BossBarEvents
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.misc.Delivery
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.TimeUtils.timeDeltaWithin
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object Elderpork : Boss(
    World.Elderpork,
    Regex("Elderpork the Great - (?:(\\d+)\\/\\d+|YOU\\/SHOULD\\/KILL\\/YOURSELF) ❤ \\[PHASE (\\d+)]"),
    phaseTimesSec = listOf(60 * 2, 60 * 10, 60 * 5, 60 * 2)
){

    private var monitorMaxTime = 30
    private var monitorLastStart = -0L

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        val content = event.string
        if (content.contains("is preparing to fight Elderpork the Great!")) {
                monitorLastStart = System.currentTimeMillis()
        }
    }

    @SubscribeEvent
    fun onBossbarAfter(event: BossBarEvents.Insert) {
        if (monitorLastStart.timeDeltaWithin(monitorMaxTime.seconds)) {

            val total_time = monitorMaxTime
            val elapsed_time = monitorLastStart.timeDelta()
            val remaining_time = total_time.milliseconds - elapsed_time
            val percentage_remaining = 1 - (((remaining_time / total_time.milliseconds) / 1000) * -1)

            event.add(
                Text.translatable("Elderpork Boss Start in${elapsed_time.formatTimer(monitorMaxTime.seconds)}")
                    .formatted(Formatting.LIGHT_PURPLE),
                percentage_remaining.toFloat(),
                BossBar.Color.PINK,
                BossBar.Style.NOTCHED_10
            )
        }
    }
}