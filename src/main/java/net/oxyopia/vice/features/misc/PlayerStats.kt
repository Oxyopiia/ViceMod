package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.ItemUtils.getLore

object PlayerStats : HudElement("Player Stats", Vice.storage.misc.playerStatsPos) {
    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if(!Vice.config.PLAYER_STATS) return

        val defenseRegex = Regex("Defence: ([+-]?\\d+)")
        val speedRegex = Regex("Speed: ([+-]?\\d+(?:\\.\\d+)?)%")

        val player = MinecraftClient.getInstance().player

        var defence = 0
        var speed = 0f
        val movementSpeed = player?.movementSpeed ?: 1f

        player?.armorItems?.forEach { itemStack ->
            val lore = itemStack.getLore()

            lore.forEach { line ->
                defenseRegex.find(line)?.apply {
                    defence += groupValues[1].toIntOrNull() ?: 0
                }

                speedRegex.find(line)?.apply {
                    speed += groupValues[1].toFloatOrNull() ?: 0f
                }
            }
        }

        val list: MutableList<String> = mutableListOf()

        list.add("&&6&&lPlayer Stats")
        list.add("&&7Defence: &&a\uD83D\uDEE1 $defence")
        list.add("&&7Speed: &&e⚡ $speed% &&7(${String.format("%.2f", movementSpeed * 100).toFloat()})")

        position.drawStrings(list, event.context)
    }

    override fun storePosition(position: Position) {
        Vice.storage.misc.playerStatsPos = position
        Vice.storage.markDirty()
    }

    override fun shouldDraw(): Boolean = Vice.config.PLAYER_STATS

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
        val list = listOf(
            "&&6&&lPlayer Stats",
            "&&7Defence: &&a\uD83D\uDEE1 16",
            "&&7Speed: &&e⚡ 30.0% &&7(19.5)"
        )

        return position.drawStrings(list, context)
    }
}
