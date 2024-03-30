package net.oxyopia.vice.features.itemabilities

import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.DrawHoverTooltipEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.ItemUtils.nameWithoutEnchants
import net.oxyopia.vice.utils.ItemUtils.getEquippedSets
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.convertFormatting

/**
 * This feature is currently disabled, due to an error that I can't really seem to reproduce.
 * There is an issue with implementing the Mixin, MixinHandledScreen, function onDrawMouseoverTooltip().
 *
 * If you can help fix this, that would be awesome!
 */
object ItemDetailDisplay {
	@SubscribeEvent
	fun onItemTooltip(event: DrawHoverTooltipEvent) {
		if (!Vice.config.ITEM_DETAIL_DISPLAY) return

		val tooltip = event.tooltip.toMutableList()
		val itemName = tooltip.first().string.nameWithoutEnchants()
		val ability = ItemAbility.getByName(itemName) ?: return

		tooltip.indexOfFirst { it.string.startsWith("⚔ Damage: ") || it.string.startsWith("♦ Set: ") }.coerceAtLeast(0).also {
			tooltip.add(it + 1, Text.of("&&b\uD83D\uDD51 Cooldown&&7: ${ability.cooldown}s".convertFormatting()))
		}

		tooltip.indexOfFirst { it.string.contains("♦ Set: ") }.also {
			if (it >= 0 && ability.setAmount > 0) {
				val appliedSetAmount = Utils.getPlayer()?.getEquippedSets()?.getOrDefault(ability.set, 0) ?: 0

				tooltip[it] = tooltip[it].copy().append(" &&8(${appliedSetAmount}/${ability.setAmount})".convertFormatting())
			}
		}

		event.setReturnValue(tooltip)
	}
}