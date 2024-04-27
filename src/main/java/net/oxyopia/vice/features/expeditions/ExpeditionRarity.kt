package net.oxyopia.vice.features.expeditions

import net.minecraft.item.ItemStack
import net.oxyopia.vice.utils.ItemUtils.getLore

enum class ExpeditionRarity(val text: String, val cleanText: String, val rarityValue: Double, val color: String, val shorthand: String) {
	DEFAULT("", "", 0.0, "&&f", "N"),
	COMMON("Cᴏᴍᴍᴏɴ", "Common", 5.0, "&&f", "C"),
	UNCOMMON("Uɴᴄᴏᴍᴍᴏɴ", "Uncommon", 10.0, "&&a", "U"),
	RARE("Rᴀʀᴇ", "Rare", 25.0, "&&b", "R"),
	EPIC("Eᴘɪᴄ", "Epic", 40.0, "&&d", "E"),
	LEGENDARY("Lᴇɢᴇɴᴅᴀʀʏ", "Legendary", 50.0, "&&6", "L"),
	MYTHICAL("Mʏᴛʜɪᴄᴀʟ", "Mythical", 100.0, "&&e", "M");

	companion object {
		fun ItemStack.getExpeditionRarity(): ExpeditionRarity? {
			return entries.firstOrNull {
				getLore().let { lore ->
					if (lore.isEmpty()) return@firstOrNull false

					val last = lore.last()
					if (last.contains("Credits") && lore.size >= 4) {
						return@firstOrNull lore[lore.size - 4].startsWith(it.text)
					}

					return@firstOrNull lore.last().startsWith(it.text)
				}
			}
		}

		fun fromShorthand(string: String): ExpeditionRarity? {
			return entries.firstOrNull {
				string.startsWith(it.shorthand)
			}
		}

		fun fromOrdinal(ordinal: Int): ExpeditionRarity {
			return entries[ordinal]
		}
 	}
}