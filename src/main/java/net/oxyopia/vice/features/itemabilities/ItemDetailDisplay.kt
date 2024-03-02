package net.oxyopia.vice.features.itemabilities

import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.DrawHoverTooltipEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.ItemUtils.nameWithoutEnchants
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.convertFormatting
import net.oxyopia.vice.utils.Utils.getEquippedSets

object ItemDetailDisplay {
	@SubscribeEvent
	fun onItemTooltip(event: DrawHoverTooltipEvent) {
		if (!Vice.config.ITEM_DETAIL_DISPLAY) return

		val tooltip = event.tooltip.toMutableList()
		val itemName = tooltip.first().string.nameWithoutEnchants()

		val ability = ItemAbility.getByName(itemName) ?: return

		val cooldownLineIndex = tooltip.indexOfFirst {
			it.string.startsWith("⚔ Damage: ") || it.string.startsWith("♦ Set: ")
		}.coerceAtLeast(0) + 1
		tooltip.add(cooldownLineIndex, Text.of("&&b\uD83D\uDD51 Cooldown&&7: ${ability.cooldown}s".convertFormatting()))

		val setLineIndex = tooltip.indexOfFirst { it.string.contains("♦ Set: ") }
		if (setLineIndex >= 0 && ability.setAmount > 0) {
			val appliedSetAmount = Utils.getPlayer()?.getEquippedSets()?.getOrDefault(ability.set, 0) ?: 0

			tooltip[setLineIndex] = tooltip[setLineIndex].copy().append(" &&8(${appliedSetAmount}/${ability.setAmount})".convertFormatting())
		}

		event.setReturnValue(tooltip)
	}
}