package net.oxyopia.vice.events

import net.minecraft.item.ItemStack
import net.minecraft.text.MutableText

class ItemRenameEvent(val item: ItemStack, val itemName: MutableText) : ViceEvent.Cancelable<MutableText>()