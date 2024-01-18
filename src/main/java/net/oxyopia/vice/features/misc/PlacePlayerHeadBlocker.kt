package net.oxyopia.vice.features.misc

import net.minecraft.item.Items
import net.minecraft.util.ActionResult
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.BlockInteractEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object PlacePlayerHeadBlocker {
	@SubscribeEvent
	fun onBlockInteract(event: BlockInteractEvent) {
		if (!Vice.config.PREVENT_PLACING_PLAYER_HEADS) return

		if (event.itemStack.item === Items.PLAYER_HEAD) {
			event.returnValue = ActionResult.PASS
		}
	}
}