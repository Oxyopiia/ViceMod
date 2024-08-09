package net.oxyopia.vice.features.misc

import net.minecraft.entity.boss.BossBar
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.*
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.TimeUtils.timeDeltaWithin
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object Delivery {
    private var deliveryMaxTime = 0
    private var deliveryLastStart = -0L
    private val deliverySecondsRegex = Regex("• After (\\d*) seconds, Glitch Security")

    private var vanMaxTime = 20
    private var vanLastStart = -0L

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        val content = event.string
        if (World.GlitchHQ.isInWorld()) {
            if (content.contains("Delivery Dispatches in T-20 Seconds")) {
                vanLastStart = System.currentTimeMillis()
            }
        }

        if (World.GlitchHQ.isInWorld() || World.Warehouse.isInWorld()) {
            deliverySecondsRegex.find(content)?.apply {
                deliveryMaxTime = groupValues[1].toInt()
                deliveryLastStart = System.currentTimeMillis()
            }
        }
    }

    @SubscribeEvent
    fun onBossbarAfter(event: BossBarEvents.Insert) {
        if (World.GlitchHQ.isInWorld()) {
            if (vanLastStart.timeDeltaWithin(vanMaxTime.seconds)) {

                val total_time = vanMaxTime
                val elapsed_time = vanLastStart.timeDelta()
                val remaining_time = total_time.milliseconds - elapsed_time
                val percentage_remaining = 1 - (((remaining_time / total_time.milliseconds) / 1000) * -1)

                event.add(
                    Text.translatable("Van Time Left${elapsed_time.formatTimer(vanMaxTime.seconds)}")
                        .formatted(Formatting.RED),
                    percentage_remaining.toFloat(),
                    BossBar.Color.RED,
                    BossBar.Style.NOTCHED_10
                )
            }
        }

        if (World.GlitchHQ.isInWorld() || World.Warehouse.isInWorld()) {
            if (deliveryLastStart.timeDeltaWithin(deliveryMaxTime.seconds)) {

                val total_time = deliveryMaxTime
                val elapsed_time = deliveryLastStart.timeDelta()
                val remaining_time = total_time.milliseconds - elapsed_time
                val percentage_remaining = 1 - (((remaining_time / total_time.milliseconds) / 1000) * -1)

                event.add(
                    Text.translatable("Delivery Time Left${elapsed_time.formatTimer(deliveryMaxTime.seconds)}")
                        .formatted(Formatting.YELLOW),
                    percentage_remaining.toFloat(),
                    BossBar.Color.YELLOW,
                    BossBar.Style.NOTCHED_10
                )
            }
        }
    }
}