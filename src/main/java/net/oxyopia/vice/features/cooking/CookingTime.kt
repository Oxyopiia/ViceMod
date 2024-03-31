package net.oxyopia.vice.features.cooking

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.auxiliary.exonitas.PowerBoxTimer
import net.oxyopia.vice.utils.HudUtils.drawString
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.Utils

object CookingTime : HudElement("Cooking Timer", Vice.storage.cooking.cookingTimePos) {
    var cookingSeconds = 0.0
    var readySeconds = 3.0
    val cookingTimeRegex = Regex("You are now cooking a burger! Come back within (\\d*) seconds.")
    var lastCooking = -0L
    var lastReady = -0L
    var cookingMode = "NONE"

    @SubscribeEvent
    fun onSound(event: SoundEvent) {
        if (!World.Burger.isInWorld()) return
        if (event.soundName == "block.note_block.pling" && event.pitch == 0.5f && event.volume == 9999f) {
            lastReady = System.currentTimeMillis()
            cookingMode = "READY"
        }
    }

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        if (!World.Burger.isInWorld()) return
        val content = event.string

        cookingTimeRegex.find(content)?.apply {
            cookingSeconds = groupValues[1].toDouble()
            lastCooking = System.currentTimeMillis()
            cookingMode = "COOKING"
        }

        if(content.startsWith("Your burger burnt.")) cookingMode = "BURNED"
    }

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!shouldDraw() || !World.Burger.isInWorld()) return

        if(CookingAPI.heldItem == CookingItem.COOKED_MEAT) cookingMode = "NONE"

        val text = when {
            cookingMode == "COOKING" -> String.format("&&a%.2fs", cookingSeconds - lastCooking.timeDelta() / 1000.0)
            cookingMode == "READY" -> "&&aREADY (${String.format("&&a%.2fs", readySeconds - lastReady.timeDelta() / 1000.0)})"
            cookingMode == "BURNED" -> "&&cBURNED"
            else -> ""
        }

        position.drawString(text, event.context)
    }

    override fun shouldDraw(): Boolean {
        return Vice.config.COOKING_TIME
    }

    override fun storePosition(position: Position) {
        Vice.storage.cooking.cookingTimePos = position
        Vice.storage.markDirty()
    }

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
        return Pair(position.drawString("&&c3.25s", context) * position.scale, 7f)
    }
}