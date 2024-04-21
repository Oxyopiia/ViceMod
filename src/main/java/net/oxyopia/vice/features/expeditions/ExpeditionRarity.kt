package net.oxyopia.vice.features.expeditions

import net.minecraft.item.ItemStack
import net.oxyopia.vice.utils.ItemUtils.getLore

enum class ExpeditionRarity(val text: String, val cleanText: String, val rarityValue: Int, val color: String) {
	COMMON("Cᴏᴍᴍᴏɴ", "Common", 5, "&&f"),
	UNCOMMON("Uɴᴄᴏᴍᴍᴏɴ", "Uncommon", 10, "&&a"),
	RARE("Rᴀʀᴇ", "Rare", 25, "&&b"),
	EPIC("Eᴘɪᴄ", "Epic", 40, "&&d"),
	LEGENDARY("Lᴇɢᴇɴᴅᴀʀʏ", "Legendary", 50, "&&6"),
	MYTHICAL("Mʏᴛʜɪᴄᴀʟ", "Mythical", 100, "&&e"),
	DEFAULT("", "", 0, "&&f");

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

		fun fromText(string: String): ExpeditionRarity? {
			return entries.firstOrNull {
				string.startsWith(it.text) || string.startsWith(it.cleanText)
			}
		}

		fun fromOrdinal(ordinal: Int): ExpeditionRarity {
			return entries[ordinal]
		}
 	}
}