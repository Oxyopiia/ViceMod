package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.cooking.CookingTime
import net.oxyopia.vice.utils.HudUtils.drawString
import net.oxyopia.vice.utils.Utils.timeDelta

object ScrapRace : HudElement("Scrap Race Time", Vice.storage.misc.raceTimePos) {
    private var RaceSeconds = 45.0
    private var lastStart = -0L

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        if (!World.MagmaHeights.isInWorld()) return
        val content = event.string

        if(content == "Countdown: 45") lastStart = System.currentTimeMillis()
        if(content.startsWith("Scrap: Got a bit of speed in ya? Pretty cool stuff.")) lastStart = -0L
        if(content == "Countdown: 1") lastStart = -0L
    }

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!shouldDraw() || !World.MagmaHeights.isInWorld()) return

        val text = when {
            lastStart != -0L -> String.format("&&a%.2fs", RaceSeconds - lastStart.timeDelta() / 1000.0)
            else -> ""
        }

        position.drawString(text, event.context)
    }

    override fun shouldDraw(): Boolean {
        return Vice.config.RACE_TIME
    }

    override fun storePosition(position: Position) {
        Vice.storage.misc.raceTimePos = position
        Vice.storage.markDirty()
    }

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
        return Pair(position.drawString("&&c3.25s", context) * position.scale, 7f)
    }
}