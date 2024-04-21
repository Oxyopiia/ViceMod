package net.oxyopia.vice.events

import net.minecraft.item.ItemStack

class ItemDropEvent(val item: ItemStack) : ViceEvent.Cancelable<Boolean>() {
	override fun cancel() {
		setReturnValue(true)
	}
}