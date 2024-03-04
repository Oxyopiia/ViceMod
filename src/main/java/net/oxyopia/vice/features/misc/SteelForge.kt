package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.config.features.MiscStorage
import net.oxyopia.vice.config.features.worlds.OysterStorage
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.bosses.ViceBoss
import net.oxyopia.vice.features.cooking.CookingTime
import net.oxyopia.vice.utils.HudUtils.drawString
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.formatTimer
import net.oxyopia.vice.utils.Utils.timeDelta

object SteelForge : HudElement("Steel Forge Time", Vice.storage.misc.steelForgeTimePos) {

    private val misc: MiscStorage get() = Vice.storage.misc

    private var SteelSeconds = 900
    private var lastStart = misc.steelForgeList

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        if (!World.MagmaHeights.isInWorld()) return
        val content = event.string

        if (content == "+⌚ 1 Steel (15m)") {
            for (i in 0..2) {
                if (lastStart[i] == -0L) {
                    lastStart[i] = System.currentTimeMillis()
                    Vice.storage.misc.steelForgeList = lastStart
                    Vice.storage.markDirty()

                    break
                }
            }
        }

        if (content == "+1 Steel") {
            Utils.sendViceMessage("found steel")
            for (i in 0..2) {
                val diff = System.currentTimeMillis() - lastStart[i]
                if (diff >= SteelSeconds * 1000 && lastStart[i] != -0L) {
                    lastStart[i] = -0L
                    Vice.storage.misc.steelForgeList = lastStart
                    Vice.storage.markDirty()

                    break
                }
            }
        }
    }

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!shouldDraw()) return

        val list = mutableListOf<String>()

        for(i in 0..2) {
            val diff = System.currentTimeMillis() - lastStart[i]
            if (lastStart[i] != -0L) {
                val formattedDiff = diff.formatTimer(SteelSeconds)
                if (diff >= SteelSeconds * 1000 && lastStart[i] != -0L) {
                    list.add("&&8Steel ${i+1}: &&aREADY")
                } else {
                    list.add("&&8Steel ${i+1}: $formattedDiff")
                }
            }
        }

        position.drawStrings(list, event.context)
    }

    override fun shouldDraw(): Boolean {
        return Vice.config.STEEL_FORGE_TIME
    }

    override fun storePosition(position: Position) {
        Vice.storage.misc.steelForgeTimePos = position
        Vice.storage.markDirty()
    }

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
        return Pair(position.drawString("&&8Steel 1: \uD83D\uDD51 08:34", context) * position.scale, 7f)
    }
}