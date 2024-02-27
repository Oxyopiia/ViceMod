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
import net.oxyopia.vice.utils.ItemUtils.nameWithoutEnchants

object PlayerStats : HudElement("Player Stats", Vice.storage.misc.playerStatsPos) {
    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!Vice.config.PLAYER_STATS) return

        val defenseRegex = Regex("Defence: ([+-]?\\d+)")
        val speedRegex = Regex("Speed: ([+-]?\\d+(?:\\.\\d+)?)%")
        val fishTimeRegex = Regex("Fish Time: (\\d+)-(\\d+(\\.\\d+)?)s")
        val fishReduceTimeRegex = Regex("Fish Time: -?(\\d+(?:\\.\\d+)?)s")

        val player = MinecraftClient.getInstance().player

        var defence = 0
        var speed = 0f
        var fishReduce = 0f
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

                fishReduceTimeRegex.find(line)?.apply {
                    fishReduce -= groupValues[1].toFloat()
                }
            }
        }

        var fishTimeNum = (0 - (fishReduce * -1))

        var fishTime = "${fishTimeNum}s"

        val itemStack = player?.mainHandStack
        val itemName = itemStack?.nameWithoutEnchants()

        if (itemName != null) {
            if (itemName.contains("Basic Fishing Rod") ||
                itemName.contains("Reinforced Fishing Rod") ||
                itemName.contains("Frigid Fishing Rod") ||
                itemName.contains("Polar Rod") ||
                itemName.contains("Gilded Fishing Rod") ||
                itemName.contains("RGB Rod")
            ) {
                val lore = itemStack.getLore()

                lore.forEach { line ->
                    fishTimeRegex.find(line)?.apply {
                        fishTime = "${groupValues[1]}-${groupValues[2].toFloat() - (fishReduce * -1)}s"
                    }
                }
            } else {
                fishTime = "Hold Fishing Rod"
            }
        }

        val list: MutableList<String> = mutableListOf()

        list.add("&&b&&lPlayer Stats")
        list.add("&&fDefence: &&a\uD83D\uDEE1 $defence")
        list.add("&&fSpeed: &&e⚡ $speed% &&7(${String.format("%.2f", movementSpeed * 100).toFloat()})")
        list.add("&&fFish Time: &&b\uD83D\uDD51 $fishTime")

        position.drawStrings(list, event.context)
    }

    override fun storePosition(position: Position) {
        Vice.storage.misc.playerStatsPos = position
        Vice.storage.markDirty()
    }

    override fun shouldDraw(): Boolean = Vice.config.PLAYER_STATS

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
        val list = listOf(
            "&&b&&lPlayer Stats",
            "&&fDefence: &&a\uD83D\uDEE1 16",
            "&&fSpeed: &&e⚡ 30.0% &&7(19.5)",
            "&&fFish Time: &&b\uD83D\uDD51 5-20s"
        )

        return position.drawStrings(list, context)
    }
}
