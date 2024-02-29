package net.oxyopia.vice.features.misc

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.ItemUtils.getLore
import net.oxyopia.vice.utils.ItemUtils.isRod
import net.oxyopia.vice.utils.Utils

object PlayerStats : HudElement("Player Stats", Vice.storage.misc.playerStatsPos) {
	private val defenseRegex = Regex("Defence: ([+-]?\\d+)")
	private val speedRegex = Regex("Speed: ([+-]?\\d+(?:\\.\\d+)?)%")
	private val fishReduceTimeRegex = Regex("Fish Time: ([+-]?\\d+\\.\\d+)s")
	private val fishTimeRegex = Regex("Fish Time: (\\d+)-(\\d+(\\.\\d+)?)s")

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!Vice.config.PLAYER_STATS) return
        val player = Utils.getPlayer() ?: return

        var defence = 0
        var speed = 0f
        val movementSpeed = player.movementSpeed
		val fishingTime = getFishingTime()

        player.armorItems?.forEach { itemStack ->
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

        list.add("&&b&&lPlayer Stats")
        list.add("&&fDefence: &&a\uD83D\uDEE1 $defence")
        list.add("&&fSpeed: &&e⚡ $speed% &&7(${String.format("%.2f", movementSpeed * 100).toFloat()})")

		if (!fishingTime.isNone() && player.mainHandStack.isRod()) {
			list.add("&&fFish Time: &&b\uD83D\uDD51 ${fishingTime.min}-${fishingTime.max}s")
		}

        position.drawStrings(list, event.context)
    }


    private fun getFishingTime(): FishingTime {
		var min = 0.0
		var max = 0.0

		Utils.getPlayer()?.mainHandStack?.let {
		    if (it.isRod()) {
				it.getLore().forEach { line ->
					fishTimeRegex.find(line)?.apply {
						min = groupValues[1].toDoubleOrNull() ?: 0.0
						max = groupValues[2].toDoubleOrNull() ?: 0.0
					}
				}
			}
		}

        Utils.getPlayer()?.armorItems?.forEach { itemStack ->
			itemStack.getLore().forEach { line ->
			    fishReduceTimeRegex.find(line)?.apply {
				    max -= (groupValues[1].toDoubleOrNull() ?: 0.0) * -1 // groupValues[1].toDoubleOrNull() for some reason return positive instead negative
			    }
		    }
		}

        return FishingTime(min, max)
    }

    data class FishingTime(val min: Double, val max: Double) {
		fun isNone() = min == 0.0 && max == 0.0
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
