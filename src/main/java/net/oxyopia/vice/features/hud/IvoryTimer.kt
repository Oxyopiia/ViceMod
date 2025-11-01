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

object IvoryTimer : HudElement(
    "Boxtown Timer",
    Vice.storage.misc.ivoryTimerPos,
    { Vice.storage.misc.ivoryTimerPos = it },
    enabled = { Vice.config.IVORY_TIMER },
) {
    private val IVORY_COOLDOWN = 1.hours
    private val SPAWN_MESSAGE = Regex("is preparing to fight Ivory The Vampiric!")

    private var reminderUsed = false

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!canDraw()) return

        val list: MutableList<String> = mutableListOf()

        val lastKnownIvory = Vice.storage.misc.lastKnownIvory
        val diff = lastKnownIvory.timeDelta()
        val remainingTime = IVORY_COOLDOWN.inWholeSeconds - diff.inWholeSeconds
        val formatted = remainingTime.seconds.formatDuration()

        if (remainingTime > 0) {
            list.add("§4Next Ivory The Vampiric in: §c${formatted}")
        } else {
            list.add("§4Ivory The Vampiric is ready to fight!")
            if(!reminderUsed) {
                SoundUtils.playSound("block.end_portal.spawn", volume = 0.8f)
                reminderUsed = true
            }
        }

        position.drawStrings(list, event.context)
    }

    @SubscribeEvent
    fun onChatEvent(event: ChatEvent) {
        SPAWN_MESSAGE.find(event.string)?.apply {
            reminderUsed = false
            Vice.storage.misc.lastKnownIvory = System.currentTimeMillis()
            Vice.storage.markDirty()
        }
    }

    override fun Position.drawPreview(context: DrawContext): Size {
        val list = listOf(
            "§4Next Ivory The Vampiric in: §c13:56"
        )
        return position.drawStrings(list, context)
    }
}