package net.oxyopia.vice.features.misc

import net.oxyopia.vice.Vice
import net.oxyopia.vice.Vice.Companion.storage
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.TitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.TimeUtils.ms
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.minutes

object EvanNotification {

    private val misc get() = storage.misc

    @SubscribeEvent
    fun onTitle(event: TitleEvent) {
        if(!event.title.contains("QUESTION")) return

        misc.lastEvanQuiz = System.currentTimeMillis()
        storage.markDirty()
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!event.repeatSeconds(1)) return
        if (!Vice.config.EVAN_NOTIFICATION || misc.lastEvanQuiz == -1L) return

        if (misc.lastEvanQuiz.timeDelta() >= 30.minutes.ms()) {
            misc.lastEvanQuiz = -1L
            storage.markDirty()

            Utils.sendViceMessage("Your Evan Quiz is ready to start!")
            Utils.playSound("block.note_block.pling", 2f)
        }
    }
}