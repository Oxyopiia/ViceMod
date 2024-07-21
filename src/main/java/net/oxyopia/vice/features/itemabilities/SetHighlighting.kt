package net.oxyopia.vice.features.itemabilities

import gg.essential.elementa.utils.withAlpha
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ContainerRenderSlotEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.highlight
import net.oxyopia.vice.utils.ItemUtils.cleanName

object SetHighlighting {
	@SubscribeEvent
	fun onInventorySlotRender(event: ContainerRenderSlotEvent) {
		if (!Vice.config.INVENTORY_SET_COLORS) return

		val set = ItemAbility.getByName(event.slot.stack.cleanName())?.set ?: return
		event.highlight(set.color.withAlpha(Vice.config.INVENTORY_SET_COLORS_OPACITY))
	}
}