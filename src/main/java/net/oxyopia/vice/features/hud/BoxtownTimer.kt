package net.oxyopia.vice.features.hud

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.SoundUtils
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

object BoxtownTimer : HudElement(
    "Boxtown Timer",
    Vice.storage.misc.boxtownTimerPos,
    { Vice.storage.misc.boxtownTimerPos = it },
    enabled = { Vice.config.BOXTOWN_TIMER },
) {
    private val BOXTOWN_COOLDOWN = 2.hours
    private val SPAWN_MESSAGE = Regex("Mine as many correct boxes as possible before the time runs out!")

    private var bellUsed = false

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!canDraw()) return

        val list: MutableList<String> = mutableListOf()

        val lastKnownBoxtown = Vice.storage.misc.lastKnownBoxtown
        val diff = lastKnownBoxtown.timeDelta()
        val remainingTime = BOXTOWN_COOLDOWN.inWholeSeconds - diff.inWholeSeconds
        val formatted = remainingTime.seconds.formatDuration()

        if (remainingTime > 0) {
            list.add("§6Next Boxtown in: §a${formatted}")
        } else {
            list.add("§6Boxtown is ready to play!")
            if(!bellUsed) {
                SoundUtils.playSound("block.bell.use", volume = 1.0f)
                bellUsed = true
            }
        }

        position.drawStrings(list, event.context)
    }

    @SubscribeEvent
    fun onChatEvent(event: ChatEvent) {
        SPAWN_MESSAGE.find(event.string)?.apply {
            bellUsed = false
            Vice.storage.misc.lastKnownBoxtown = System.currentTimeMillis()
            Vice.storage.markDirty()
        }
    }

    override fun Position.drawPreview(context: DrawContext): Size {
        val list = listOf(
            "§6Next Boxtown in: §a13:56"
        )
        return position.drawStrings(list, context)
    }
}