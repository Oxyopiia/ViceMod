package net.oxyopia.vice.events

import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import net.minecraft.util.collection.DefaultedList

class ChestRenderEvent {
	class Slots(val chestName: String, val slots: DefaultedList<Slot>, val cursorStack: ItemStack) : ViceEvent()
}