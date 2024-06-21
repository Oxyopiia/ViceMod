package net.oxyopia.vice.events

import net.minecraft.client.gui.DrawContext
import net.minecraft.screen.slot.Slot

class ChestRenderSlotEvent(val slot: Slot, val context: DrawContext) : ViceEvent()