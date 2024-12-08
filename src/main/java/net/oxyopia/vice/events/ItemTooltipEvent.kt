package net.oxyopia.vice.events

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.tooltip.TooltipType
import net.minecraft.text.Text

class ItemTooltipEvent(
	val stack: ItemStack,
	val context: Item.TooltipContext,
	val type: TooltipType,
	val lines: MutableList<Text>,
) : ViceEvent()