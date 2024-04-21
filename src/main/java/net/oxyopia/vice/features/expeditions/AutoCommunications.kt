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
import net.oxyopia.vice.features.expeditions.ExpeditionItemType.Companion.getExpeditionItemType
import net.oxyopia.vice.features.expeditions.ExpeditionRarity.Companion.getExpeditionRarity
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.ItemUtils.cleanName
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
			filter()
			if (!shouldParse() || !playerIsInside(sender)) return

			val room = groupValues[1].toIntOrNull() ?: return
			val itemIndex = groupValues[2].toIntOrNull() ?: return

			val original = merchants[room]?.get(itemIndex)
			Utils.sendViceMessage("&&a${sender} &&fpurchased &&a${original?.cleanName()} &&ffrom the Villager in &&aRoom $room")
			Utils.playDing()

			merchants[room]?.set(itemIndex, ItemStack.EMPTY)
			DevUtils.sendDebugChat("&&aEXPEDITIONS &&fSet index &&a$itemIndex&&f of merchant &&a$room &&fto &&cEMPTY.", "EXPEDITION_DEBUGGER")
		}

		// TEST LINE:
		// Villager in Room 3! (ViceExpMerchant-3-Bandages:UI;Raygun:LW;C4:EW)
		merchantFindRegex.find(string)?.apply {
			filter()
			if (!shouldParse() || !playerIsInside(sender)) return

			val room = groupValues[1].toIntOrNull() ?: return
			val data = groupValues[2]

			Utils.sendViceMessage("&&a${sender} &&ffound a Villager in &&aRoom $room&&f!")
			Utils.playDing()

			if (room < 0 || room > 15 || merchants[room]?.isNotEmpty() == true) return
			DevUtils.sendDebugChat("&&aEXPEDITIONS &&fImporting shopkeeper &&a$room&&f data from ${sender}.", "EXPEDITION_DEBUGGER")

			val items = data.split(";")
			merchants[room] = mutableListOf()

			items.forEach { itemData ->
				val split = itemData.split(":")
				val name = split[0]
				val importantLore = split[1]

				val rarity = ExpeditionRarity.fromShorthand(importantLore) ?: ExpeditionRarity.DEFAULT
				val type = ExpeditionItemType.fromShorthand(importantLore) ?: ExpeditionItemType.ITEM

				val stack = ItemStack(Items.IRON_SWORD).setCustomName(Text.of("${rarity.color}$name".convertFormatting()))
				val loreList = stack.getOrCreateSubNbt(ItemStack.DISPLAY_KEY).getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE.toInt())
				loreList.add(NbtString.of(Text.Serializer.toJson(Text.of("${rarity.text} ${type.text}"))))
				stack.getOrCreateSubNbt(ItemStack.DISPLAY_KEY).put(ItemStack.LORE_KEY, loreList)

				merchants[room]?.add(stack)
			}
		}
	}

	fun shareShopkeeperFound(room: Int, items: List<ItemStack>) {
		if (!shouldShare()) return

		var text = "Villager in Room $room! (ViceExpMerchant-$room-"
		items.forEach { item ->
			val typeText = item.getExpeditionItemType() ?: return
			val rarity = item.getExpeditionRarity() ?: return
			val condensed = rarity.shorthand + typeText.shorthand

			text += "${item.cleanName()}:$condensed;"
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

	private fun playerIsInside(username: String): Boolean {
		return ExpeditionAPI.currentSession.players.map { it.name.string }.contains(username)
	}

	private fun getPlayerCount(): Int = ExpeditionAPI.currentSession.players.size
	private fun shouldShare(): Boolean = Vice.config.AUTO_COMMUNICATE_EXPEDITION_INFO && getPlayerCount() > 1
	private fun shouldParse(): Boolean = Vice.config.AUTO_COMMUNICATE_EXPEDITION_INFO
}