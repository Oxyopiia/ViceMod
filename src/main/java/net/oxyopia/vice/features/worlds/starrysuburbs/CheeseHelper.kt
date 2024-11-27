package net.oxyopia.vice.features.worlds.starrysuburbs

import gg.essential.elementa.utils.withAlpha
import net.minecraft.item.ItemStack
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ContainerRenderSlotEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.highlight
import net.oxyopia.vice.utils.ItemUtils.getLore
import java.awt.Color

object CheeseHelper {
	private fun ItemStack.isCheeseSalable(): Boolean {
		val first = getLore().firstOrNull() ?: return false
		return first.startsWith("Sell Value: ") && first.endsWith(" Cheese")
	}

	@SubscribeEvent
	fun onInventoryRender(event: ContainerRenderSlotEvent) {
		if (!Vice.config.STARRY_STREETS_CHEESE_HIGHLIGHT || !World.StarryStreets.isInWorld() || !event.chestName.contains("Cheesy Deals")) return

		if (event.slot.stack.isCheeseSalable()) {
			event.highlight(Color(255, 190, 0).withAlpha(0.8f))
		}
	}
}