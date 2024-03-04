package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawString
import net.oxyopia.vice.utils.Utils.timeDelta

object WastelandTime : HudElement("Wasteland Time", Vice.storage.misc.wastelandTimePos) {
    private var wastelandSeconds = 60.0
    private var lastEnter = -0L

    @SubscribeEvent
    fun onSound(event: SoundEvent) {
        if (event.soundName == "ui.toast.challenge_complete" && event.pitch == 1f && event.volume == 9999f) {
            lastEnter = -0L
        }

        if (!World.Wasteyard.isInWorld()) return

        if (event.soundName == "block.end_portal.spawn" && event.pitch == 1f && event.volume == 9999f) {
            lastEnter = System.currentTimeMillis()
        }
    }

    @SubscribeEvent
    fun onPlayerDeath(event: EntityDeathEvent) {
        val client = MinecraftClient.getInstance()
        if (!World.Wasteyard.isInWorld()) return
        if(event.entity == client.player) lastEnter = -0L
    }

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!shouldDraw() || !World.Wasteyard.isInWorld()) return

        val text = when {
            lastEnter != -0L -> String.format("&&a%.2fs", wastelandSeconds - lastEnter.timeDelta() / 1000.0)
            else -> ""
        }

        position.drawString(text, event.context)
    }

    override fun shouldDraw(): Boolean {
        return Vice.config.WASTELAND_TIME
    }

    override fun storePosition(position: Position) {
        Vice.storage.misc.wastelandTimePos = position
        Vice.storage.markDirty()
    }

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
        return Pair(position.drawString("&&c3.25s", context) * position.scale, 7f)
    }
}