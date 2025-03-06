package net.oxyopia.vice.features.hud

import io.netty.util.Attribute
import net.minecraft.client.gui.DrawContext
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.ChatUtils
import net.oxyopia.vice.utils.HudUtils.drawTexts
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.ItemUtils.getLore
import net.oxyopia.vice.utils.ItemUtils.isRod
import net.oxyopia.vice.utils.Utils
import java.awt.Color

object PlayerStats : HudElement(
	"Player Stats",
	Vice.storage.misc.playerStatsPos,
	{  Vice.storage.misc.playerStatsPos = it },
	enabled = { Vice.config.PLAYER_STATS }
) {
	private val defenseRegex = Regex("Defence: ([+-]?\\d+)")
	private val speedRegex = Regex("Speed: ([+-]?\\d+(?:\\.\\d+)?)%")
	private val fishReduceTimeRegex = Regex("Fish Time: ([+-]?\\d+\\.\\d+)s")
	private val fishTimeRegex = Regex("Fish Time: (\\d+)(?:-(\\d+(\\.\\d+)?))?s")
	private val extraHealthRegex = Regex("Max Health: ([+-])?(\\d+\\.\\d+)")

	private var extraHealth = 0.0
	private var dodgeChance = 0.0
	private var chainedHeal = 0.0
	private var enforcerChance = 0.0

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!canDraw()) return
        val player = Utils.getPlayer() ?: return

        var defence = 0
        var speed = 0f
        val movementSpeed = player.movementSpeed
		val fishingTime = getFishingTime()
		extraHealth = 0.0
		dodgeChance = 0.0
		chainedHeal = 0.0
		enforcerChance = 0.0

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

        val list: MutableList<Text> = mutableListOf()

        list.add("Player Stats".toText(Vice.PRIMARY, bold = true))
        list.add("§fDefence: §a\uD83D\uDEE1 $defence".toText())
        list.add("§fSpeed: §e⚡ $speed% §7(${String.format("%.2f", movementSpeed * 100).toFloat()})".toText())

		if (!fishingTime.isNone()) {
			if (fishingTime.min != 0.0) {
				list.add("§fFish Time: §b\uD83D\uDD51 ${fishingTime.min}-${fishingTime.max}s".toText())
			} else {
				list.add("§fFish Time: §b\uD83D\uDD51 ${fishingTime.max}s".toText())
			}
		}

		val breakingPower = getBreakingPower()
		if (breakingPower > 0) {
			list.add("§fBreaking Power: §b⛏ $breakingPower".toText())
			list.add("§fBreaking Speed: §a\uD83D\uDD51 ${getMiningSpeed()}x".toText())
		}

		Utils.getPlayer()?.armorItems?.forEach { itemStack ->
			getExtraHealth(itemStack)
			getOtherStats(itemStack)
		}

		if (extraHealth != 0.0) {
			val sign = if (extraHealth > 0) "+" else ""
			list.add("§fExtra Health: §c❤ $sign$extraHealth".toText())
		}

		if (dodgeChance > 0) {
			list.add("Dodge Chance: ".toText().append("☀ ${(dodgeChance * 100).toInt()}%".toText(Color(255,219,112))))
		}

		if (chainedHeal > 0) {
			list.add("Chained Heal: ".toText().append("❣ +$chainedHeal".toText(Color(219,255,247))))
		}

		if(enforcerChance > 0) {
			list.add("Enforcer Chance: ".toText().append("⚠ ${(enforcerChance * 100).toInt()}%".toText(Color(46, 60, 112))))
		}

        position.drawTexts(list, event.context)
    }


    fun getFishingTime(): FishingTime {
		var min = 0.0
		var max = 0.0

		Utils.getPlayer()?.mainHandStack?.let { item ->
			if (!item.isRod()) return FishingTime.NONE

			item.getLore().forEach { line ->
				fishTimeRegex.find(line)?.apply {
					min = groupValues[1].toDoubleOrNull() ?: 0.0
					max = groupValues[2].toDoubleOrNull() ?: 0.0

					if(max == 0.0 && min != 0.0) {
						max = min
						min = 0.0
					}
				}
			}
		}

        Utils.getPlayer()?.armorItems?.forEach { itemStack ->
			itemStack.getLore().forEach { line ->
			    fishReduceTimeRegex.find(line)?.apply {
				    max += groupValues[1].toDoubleOrNull() ?: 0.0
			    }
		    }
		}

        return FishingTime(min, max)
    }

    data class FishingTime(val min: Double, val max: Double) {
		fun isNone() = this == NONE

		companion object {
			val NONE by lazy { FishingTime(0.0, 0.0) }
		}
	}

	private fun getMiningSpeed(): Float {
		Utils.getPlayer()?.mainHandStack?.apply {
			val data = get(DataComponentTypes.CUSTOM_DATA)?.copyNbt() ?: return 0f

			if (data.contains("custom", NbtElement.COMPOUND_TYPE.toInt())) {
				val nbtCompound = data.getCompound("custom")

				if (nbtCompound.contains("miningspeed", NbtElement.FLOAT_TYPE.toInt())) {
					return nbtCompound.getFloat("miningspeed")
				}
			}
		}

		return 0f
	}

	private fun getBreakingPower(): Int {
		Utils.getPlayer()?.mainHandStack?.apply {
			val data = get(DataComponentTypes.CUSTOM_DATA)?.copyNbt() ?: return 0

			if (data.contains("custom", NbtElement.COMPOUND_TYPE.toInt())) {
				val nbtCompound = data.getCompound("custom")

				if (nbtCompound.contains("breakingpower", NbtElement.INT_TYPE.toInt())) {
					return nbtCompound.getInt("breakingpower")
				}
			}
		}

		return 0
	}

	private fun getExtraHealth(item: ItemStack) {
		val lore = item.getLore()

		lore.forEach { line ->
			extraHealthRegex.find(line)?.apply {
				when (groupValues[1]) {
					"+" -> extraHealth += groupValues[2].toFloat()
					"-" -> extraHealth -= groupValues[2].toFloat()
				}
			}
		}
	}

	private fun getOtherStats(item: ItemStack) {
		when {
			item.cleanName().contains("Enforcer") -> enforcerChance += 0.1
			item.cleanName().contains("Smile Bandit") -> dodgeChance += 0.05
			item.cleanName().contains("Omniclocked") -> chainedHeal += 0.25
		}
	}

    override fun Position.drawPreview(context: DrawContext): Size {
        val list = listOf(
            "Player Stats".toText(Vice.PRIMARY, bold = true),
            "§fDefence: §a\uD83D\uDEE1 16".toText(),
            "§fSpeed: §e⚡ 30.0% §7(19.5)".toText(),
			"§fExtra Health: §c❤ -0.25".toText(),
			"Dodge Chance: ".toText().append("☀ 5%".toText(Color(255,219,112))),
			"Chained Heal: ".toText().append("❣ +0.25".toText(Color(219,255,247))),
			"Enforcer Chance: ".toText().append("⚠ 10%".toText(Color(46,60,112))),
            "§fFish Time: §b\uD83D\uDD51 5-20s".toText(),
			"§fBreaking Power: §b⛏ 2".toText(),
			"§fBreaking Speed: §a1.2x".toText()
        )

        return position.drawTexts(list, context)
    }
}
