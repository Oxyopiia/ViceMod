package net.oxyopia.vice.features.misc

import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.BlockInteractEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object PlacePlayerHeadBlocker {
	@SubscribeEvent
	fun onBlockInteract(event: BlockInteractEvent) {
		if (!Vice.config.PREVENT_PLACING_PLAYER_HEADS) return

		val stack: ItemStack = if (event.hand == Hand.MAIN_HAND) event.player.mainHandStack else event.player.offHandStack
		if (stack.item === Items.PLAYER_HEAD) {
			event.returnValue = ActionResult.PASS
		}
	}
}