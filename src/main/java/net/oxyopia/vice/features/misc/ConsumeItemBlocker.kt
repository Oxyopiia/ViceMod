package net.oxyopia.vice.features.misc

import net.minecraft.item.Items
import net.minecraft.util.ActionResult
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.BlockInteractEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.ItemUtils.nameWithoutEnchants

object ConsumeItemBlocker {
	@SubscribeEvent
	fun onBlockInteract(event: BlockInteractEvent) {
		if (!Vice.config.PREVENT_PLACING_PLAYER_HEADS) return
		val stack = event.itemStack

		val new = when {
			stack.item == Items.PLAYER_HEAD -> ActionResult.PASS
			stack.item == Items.TRIPWIRE_HOOK && stack.nameWithoutEnchants().contains("Train Key") -> ActionResult.PASS
			else -> return
		}

		event.setReturnValue(new)
	}
}