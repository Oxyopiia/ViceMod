package net.oxyopia.vice.events

import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.slot.Slot
import net.minecraft.util.collection.DefaultedList

class ChestRenderEvent(
	val chestName: String,
	val slots: DefaultedList<Slot>,
	val cursorStack: ItemStack,
	val id: Int,
	val context: DrawContext
) : ViceEvent() {
	val components by lazy {
		slots.filter { it.stack.item != Items.GRAY_STAINED_GLASS_PANE }
	}

	/**
	 * Checks if the last element is not air.
	 */
	fun isFullyRendererd(): Boolean {
		val chestContents = slots.filter { it.inventory.size() != 41 }
		return !chestContents.last().stack.isEmpty
	}

	/**
	 * * Checks if all elements are not air.
	 */
	fun isActuallyFullyRendered(): Boolean {
		val emptyItems = slots.filter { it.inventory.size() != 41 && it.stack.isEmpty }
		return emptyItems.isEmpty()
	}
}