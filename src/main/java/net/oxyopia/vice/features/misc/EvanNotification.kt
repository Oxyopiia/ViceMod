package net.oxyopia.vice.features.misc

import net.oxyopia.vice.Vice
import net.oxyopia.vice.Vice.Companion.storage
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.TitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.ms
import net.oxyopia.vice.utils.Utils.timeDelta
import kotlin.time.Duration.Companion.minutes

object EvanNotification {

    private val misc get() = storage.misc
    private val lastEvanQuiz get() = misc.lastEvanQuiz

    @SubscribeEvent
    fun onTitle(event: TitleEvent) {
        if(!event.title.contains("QUESTION")) return

        misc.lastEvanQuiz = System.currentTimeMillis()
        storage.markDirty()
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (!Vice.config.EVAN_NOTIFICATION) return
        if(misc.lastEvanQuiz == -1L) return

        if (lastEvanQuiz.timeDelta() >= 30.minutes.ms()) {
            misc.lastEvanQuiz = -1L
            storage.markDirty()

            Utils.sendViceMessage("Evan Quiz are ready to enter!")
            Utils.playSound("block.note_block.pling", 2f)
        }
    }
}