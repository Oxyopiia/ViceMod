package net.oxyopia.vice.features.worlds.glitchhq

import net.oxyopia.vice.Vice
import net.oxyopia.vice.Vice.Companion.storage
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.TitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.ChatUtils
import net.oxyopia.vice.utils.SoundUtils
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

        if (misc.lastEvanQuiz.timeDelta() >= 30.minutes) {
            misc.lastEvanQuiz = -1L
            storage.markDirty()

            ChatUtils.sendViceMessage("Your Evan Quiz is ready to start!")
            SoundUtils.playSound("block.note_block.pling", 2f)
        }
    }
}