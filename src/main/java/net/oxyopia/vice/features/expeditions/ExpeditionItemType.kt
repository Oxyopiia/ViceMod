package net.oxyopia.vice.features.expeditions

import net.minecraft.item.ItemStack
import net.oxyopia.vice.utils.ItemUtils.getLore

enum class ExpeditionItemType(val text: String, val cleanText: String, val typeValue: Double, val shorthand: String) {
	WEAPON("Wᴇᴀᴘᴏɴ", "Weapon", 1.0, "W"),
	ITEM("Iᴛᴇᴍ", "Item", 0.5, "I"),
	DEFAULT("", "", 0.5, "N");

	companion object {
		fun ItemStack.getExpeditionItemType(): ExpeditionItemType? {
			return entries.firstOrNull {
				getLore().let { lore ->
					if (lore.isEmpty()) return@firstOrNull false

					val last = lore.last()
					if (last.contains("Credits") && lore.size >= 4) {
						return@firstOrNull lore[lore.size - 4].endsWith(it.text)
					}

					return@firstOrNull lore.last().endsWith(it.text)
				}
			}
		}

		fun fromShorthand(string: String): ExpeditionItemType? {
			return entries.firstOrNull {
				string.endsWith(it.shorthand)
			}
		}
	}
}