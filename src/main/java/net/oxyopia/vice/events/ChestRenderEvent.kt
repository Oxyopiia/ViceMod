package net.oxyopia.vice.events

import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.util.collection.DefaultedList

class ChestRenderEvent {
	class Slots(val chestName: String, val slots: DefaultedList<Slot>, val cursorStack: ItemStack, val isFirstOpened: Boolean) : ViceEvent() {
		/**
		 * Checks if the last element is not air.
		 */
		fun isFullyRendererd(): Boolean {
			val chestContents = slots.filter { it.inventory.size() != 41 }
			return !chestContents.last().stack.isEmpty
		}

		/**
		 * Checks if all elements are not air.
		 */
		fun isActuallyFullyRendered(): Boolean {
			val emptyItems = slots.filter { it.inventory.size() != 41 && it.stack.isEmpty }
			return emptyItems.isEmpty()
		}
	}
}