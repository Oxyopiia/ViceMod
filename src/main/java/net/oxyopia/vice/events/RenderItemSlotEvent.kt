package net.oxyopia.vice.events

import net.minecraft.client.font.TextRenderer
import net.minecraft.item.ItemStack

class RenderItemSlotEvent(val textRenderer: TextRenderer, val stack: ItemStack, val x: Int, val y: Int) : BaseEvent()