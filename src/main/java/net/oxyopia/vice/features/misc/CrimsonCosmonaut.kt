package net.oxyopia.vice.features.misc

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.config.features.shatteredsector.CrimsonCosmonautStorage
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

object CrimsonCosmonaut : HudElement(
    "Crimson Cosmonaut Timer",
    Vice.storage.shatteredSector.crimsonCosmonaut.fightTimePos,
    { Vice.storage.shatteredSector.crimsonCosmonaut.fightTimePos = it },
    enabled = { Vice.config.CRIMSON_COSMONAUT_TIMER },
) {
    private val SPAWN_COOLDOWN = 48.hours
    private val SPAWN_MESSAGE = Regex("x\\d+ Bluesteel \\(\\+\\d+ From Difficulty\\)")
    private val crimsonCosmonaut: CrimsonCosmonautStorage = Vice.storage.shatteredSector.crimsonCosmonaut

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!canDraw()) return

        val list: MutableList<String> = mutableListOf()

        val diff = crimsonCosmonaut.lastKnownFightTime.timeDelta()
        val remainingTime = SPAWN_COOLDOWN.inWholeSeconds - diff.inWholeSeconds
        val formatted = remainingTime.seconds.formatDuration()

        if (remainingTime > 0) {
            list.add("§cNext Crimson Cosmonaut in: §a${formatted}")
        } else {
            list.add("§cCrimson Cosmonaut is ready to fight!")
        }

        position.drawStrings(list, event.context)
    }

    @SubscribeEvent
    fun onChatEvent(event: ChatEvent) {
        SPAWN_MESSAGE.find(event.string)?.apply {
            crimsonCosmonaut.lastKnownFightTime = System.currentTimeMillis()
            Vice.storage.markDirty()
        }
    }

    override fun Position.drawPreview(context: DrawContext): Size {
        val list = listOf(
            "§cNext Crimson Cosmonaut in: §a11:13:56"
        )
        return position.drawStrings(list, context)
    }
}