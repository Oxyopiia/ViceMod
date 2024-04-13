package net.oxyopia.vice.features.expeditions

import net.minecraft.item.ItemStack
import net.oxyopia.vice.utils.ItemUtils.getLore

enum class ExpeditionRarity(val text: String, val cleanText: String) {
	COMMON("Cᴏᴍᴍᴏɴ", "Common"),
	UNCOMMON("Uɴᴄᴏᴍᴍᴏɴ", "Uncommon"),
	RARE("Rᴀʀᴇ", "Rare"),
	EPIC("Eᴘɪᴄ", "Epic"),
	LEGENDARY("Lᴇɢᴇɴᴅᴀʀʏ", "Legendary"),
	MYTHICAL("Mʏᴛʜɪᴄᴀʟ", "Mythical");

	companion object {
		fun ItemStack.getExpeditionRarity(): ExpeditionRarity? {
			return entries.firstOrNull { getLore().last() == it.text }
		}
 	}
}