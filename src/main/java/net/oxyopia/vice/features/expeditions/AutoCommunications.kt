package net.oxyopia.vice.features.expeditions

import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtString
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.features.expeditions.ExpeditionAPI.merchants
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.ItemUtils.getLore
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.convertFormatting

object AutoCommunications {
	private val merchantFindRegex: Regex by lazy {
		Regex("Villager in Room \\d+! \\(ViceExpMerchant-(\\d+)-(.+)\\)")
	}
	private val merchantBuyRegex: Regex by lazy {
		Regex("Purchased .+ in Room \\d+! \\(ViceExpMerchantBuy-(\\d+):(\\d+)\\)")
	}

	fun ChatEvent.processCommunications() {
		if (string.startsWith("Vice (Dev)")) return

		// TEST LINE:
		// Purchased Soul Scythe in Room 3! (ViceExpMerchantBuy-3:2)
		merchantBuyRegex.find(string)?.apply {
			val room = groupValues[1].toIntOrNull() ?: return
			val itemIndex = groupValues[2].toIntOrNull() ?: return

			val removed = merchants[room]?.set(itemIndex, ItemStack.EMPTY)
			DevUtils.sendDebugChat("&&aEXPEDITIONS &&fSet index &&a$itemIndex&&f of merchant &&a$room &&to &&cEMPTY.", "EXPEDITION_DEBUGGER")

			filter()
			if (!shouldParse()) return
			Utils.sendViceMessage("&&a${sender} &&fpurchased &&a${removed?.cleanName()} &&ffrom the Villager in &&aRoom $room")
			Utils.playDing()
		}

		// TEST LINE:
		// Villager in Room 3! (ViceExpMerchant-3-Bandages:Uɴᴄᴏᴍᴍᴏɴ Iᴛᴇᴍ;Raygun:Lᴇɢᴇɴᴅᴀʀʏ Wᴇᴀᴘᴏɴ;C4:Eᴘɪᴄ Wᴇᴀᴘᴏɴ)
		merchantFindRegex.find(string)?.apply {
			val room = groupValues[1].toIntOrNull() ?: return
			val data = groupValues[2]

			filter()
			if (!shouldParse()) return
			Utils.sendViceMessage("&&a${sender} &&ffound a Villager in &&aRoom $room!")
			Utils.playDing()

			if (room < 0 || room > 15 || merchants[room]?.isNotEmpty() == true) return
			DevUtils.sendDebugChat("&&aEXPEDITIONS &&fImporting shopkeeper &&a$room&&f data from ${sender}.", "EXPEDITION_DEBUGGER")

			val items = data.split(";")
			merchants[room] = mutableListOf()

			items.forEach { itemData ->
				val split = itemData.split(":")
				val name = split[0]
				val importantLore = split[1]

				val rarity = ExpeditionRarity.fromText(importantLore) ?: ExpeditionRarity.DEFAULT

				val stack = ItemStack(Items.IRON_SWORD).setCustomName(Text.of("${rarity.color}$name".convertFormatting()))
				val loreList = stack.getOrCreateSubNbt(ItemStack.DISPLAY_KEY).getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE.toInt())
				loreList.add(NbtString.of(Text.Serializer.toJson(Text.of(importantLore))))
				stack.getOrCreateSubNbt(ItemStack.DISPLAY_KEY).put(ItemStack.LORE_KEY, loreList)

				merchants[room]?.add(stack)
			}
		}
	}

	fun shareShopkeeperFound(room: Int, items: List<ItemStack>) {
		if (!shouldShare()) return

		var text = "Villager in Room $room! (ViceExpMerchant-$room-"
		items.forEach { item ->
			val lore = item.getLore()
			val importantLore = if (lore.size >= 4) lore[lore.size - 4] else lore.last()

			text += "${item.cleanName()}:$importantLore;"
		}

		text = text.removeSuffix(";").plus(")")
		tellTeammates(text)
	}

	fun shareShopkeeperBuy(room: Int, itemIndex: Int, matchedItem: ItemStack) {
		if (!shouldShare()) return
		tellTeammates("Purchased ${matchedItem.cleanName()} in Room $room! (ViceExpMerchantBuy-$room:$itemIndex)")
	}

	private fun tellTeammates(string: String) {
		DevUtils.sendDebugChat("&&aEXPEDITIONS &&fSharing data &&7$string", "EXPEDITION_DEBUGGER")
		MinecraftClient.getInstance().networkHandler?.sendChatMessage(string)
	}

	private fun ChatEvent.filter() {
		if (Vice.config.FILTER_EXPEDITION_COMMUNICATIONS) cancel()
	}

	private fun getPlayerCount(): Int = ExpeditionAPI.currentSession.players.size
	private fun shouldShare(): Boolean = Vice.config.AUTO_COMMUNICATE_EXPEDITION_INFO && getPlayerCount() > 1
	private fun shouldParse(): Boolean = Vice.config.AUTO_COMMUNICATE_EXPEDITION_INFO
}