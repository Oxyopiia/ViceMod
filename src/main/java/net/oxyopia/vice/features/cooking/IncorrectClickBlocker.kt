package net.oxyopia.vice.features.cooking

import net.minecraft.block.Blocks
import net.minecraft.util.ActionResult
import net.oxyopia.vice.Vice
import net.oxyopia.vice.commands.BlockClickOverride
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.BlockInteractEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.Utils.timeDelta

object IncorrectClickBlocker {
	private var lastPlateInteract: Long = 0

	@SubscribeEvent
	fun onBlockInteract(event: BlockInteractEvent) {
		if (!Vice.config.BLOCK_WRONG_COOKING_CLICKS || !World.Burger.isInWorld() || BlockClickOverride.isActive()) return

		if (event.block != Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE) return

		val order = CookingAPI.currentOrder
		if (order == CookingOrder.NONE || CookingAPI.heldItem != order.recipe[CookingAPI.orderCurrentItemIndex] || lastPlateInteract.timeDelta() <= 300) {
			event.setReturnValue(ActionResult.FAIL)
		}

		lastPlateInteract = System.currentTimeMillis()
	}
}