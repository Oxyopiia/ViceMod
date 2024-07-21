package net.oxyopia.vice.events

import net.minecraft.client.gui.DrawContext
import net.minecraft.screen.slot.Slot

class ContainerRenderSlotEvent(val slot: Slot, val chestName: String, val context: DrawContext) : ViceEvent()