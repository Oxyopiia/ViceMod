package net.oxyopia.vice.events

import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack

class RenderItemSlotEvent(val context: DrawContext, val itemStack: ItemStack, val x: Int, val y: Int) : ViceEvent()