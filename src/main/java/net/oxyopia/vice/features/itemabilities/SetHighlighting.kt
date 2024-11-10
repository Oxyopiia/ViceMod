package net.oxyopia.vice.features.itemabilities

import gg.essential.elementa.utils.withAlpha
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Set
import net.oxyopia.vice.events.ContainerRenderSlotEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.highlight
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.ItemUtils.getAttributeModifierInAnySlot
import net.oxyopia.vice.utils.ItemUtils.getLore
import net.oxyopia.vice.utils.ItemUtils.isPlayerHeadWithArmor

object SetHighlighting {
	private val cache = hashMapOf<String, Set?>()
	private val loreRegex by lazy {
		Regex("♦ Set: (.+)")
	}

	@SubscribeEvent
	fun onInventorySlotRender(event: ContainerRenderSlotEvent) {
		if (!Vice.config.INVENTORY_SET_COLORS) return
		val item = event.slot.stack

		if (!Vice.config.INCLUDE_ARMOR_IN_SET_COLORS &&
			(item.getAttributeModifierInAnySlot(EntityAttributes.GENERIC_ARMOR) > 0 || item.isPlayerHeadWithArmor())
		) return

		val set = event.slot.stack.getSet(event) ?: return
		event.highlight(set.color.withAlpha(Vice.config.INVENTORY_SET_COLORS_OPACITY))
	}

	private fun ItemStack.getSet(event: ContainerRenderSlotEvent): Set? {
		val name = cleanName()

		return cache[name] ?: run {
			val lore = getLore()
			if (lore.isEmpty()) return null

			lore.forEach {
				loreRegex.find(it)?.apply {
					val matchedSet = Set.getByName(groupValues[1])

					cache[name] = matchedSet
					return@run matchedSet
				}
			}

			if (event.shouldCacheEmpty()) cache[name] = null
			return null
		}
	}

	private fun ContainerRenderSlotEvent.shouldCacheEmpty(): Boolean {
		return chestName.contains("Crafting") || chestName.contains("Backpack")
	}
}