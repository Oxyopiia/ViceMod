package net.oxyopia.vice.events

import net.minecraft.screen.slot.SlotActionType

class SlotClickEvent(
	val chestName: String,
	val syncId: Int,
	val slotId: Int,
	val button: Int,
	val actionType: SlotActionType
) : ViceEvent.Cancelable<Boolean>()