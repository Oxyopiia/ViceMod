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
import net.oxyopia.vice.utils.Utils


object PlayerStats : HudElement("Player Stats", Vice.storage.misc.playerStatsPos){
    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!Utils.inDoomTowers) return
        if(!Vice.config.PLAYER_STATS) return

        val defenseRegex = Regex("Defence: ([+-]?\\d+)")
        val speedRegex = Regex("Speed: ([+-]?\\d+(?:\\.\\d+)?)%")

        val player = MinecraftClient.getInstance().player;

        var defence = 0

        if(player == null) return

        var speed = 0f

        if (player.armorItems != null) {
            for (i in 0 until player.armorItems.count()) {
                val lore = player.armorItems.elementAt(i).getLore()
                if (lore.isEmpty()) {
                    defence += 0
                } else {
                    for (i in lore.indices) {
                        defenseRegex.find(lore[i])?.apply {
                            val defenceValue = groupValues[1].toIntOrNull() ?: 0
                            defence += defenceValue
                        }

                        speedRegex.find(lore[i])?.apply {
                            val speedValue = groupValues[1].toFloatOrNull() ?: 0f
                            speed += speedValue
                        }
                    }
                }
            }
        }

        val list: MutableList<String> = mutableListOf()

        list.add("&&6&&lPlayer Stats:")
        list.add("&&aDefence: $defence")
        list.add("&&eSpeed: $speed% (${String.format("%.2f", player.movementSpeed * 100).toFloat()})")

        position.drawStrings(list, event.context)
    }

    override fun storePosition(position: Position) {
        Vice.storage.misc.playerStatsPos = position
        Vice.storage.markDirty()
    }

    override fun shouldDraw(): Boolean = Vice.config.PLAYER_STATS

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
        val list = listOf(
            "&&6&&lPlayer Stats:",
            "&&aDefence: 23",
            "&&eSpeed: 30.0% (19.5)"
        )

        return position.drawStrings(list, context)
    }
}
