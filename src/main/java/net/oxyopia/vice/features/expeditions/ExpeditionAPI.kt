package net.oxyopia.vice.features.expeditions

import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.BlockUpdateEvent
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.ChestRenderEvent
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.SlotClickEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.expeditions.AutoCommunications.processCommunications
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.TimeUtils.ms
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.Utils
import kotlin.time.Duration.Companion.seconds


object ExpeditionAPI {
	var currentSession: ExpeditionRun = ExpeditionRun(-1L)

	private val newItemRegex = Regex("\\+\\d+ (.*)")
	private val merchantSlots = listOf(20, 22, 24)
	private val roomZBounds = listOf(
		-107.0,
		-76.0,
		-45.0,
		-26.0,
		4.5,
		21.0,
		52.0,
		83.0,
		118.0
	)

	private var lastClickedShopItemIndex = -1
	private var lastClickedShopItemTime: Long = -1L
	val merchants = hashMapOf<Int, MutableList<ItemStack>>()

	@SubscribeEvent
	fun onChat(event: ChatEvent) {
		when {
			event.string.contains("Right click on the door to begin the Expedition.") -> {
				val players = Utils.getWorld()?.players?.filterNot { it.name.string.startsWith("CIT-") || it.customName?.string?.startsWith("CIT-") == true } ?: listOf()
				currentSession = ExpeditionRun(System.currentTimeMillis() - 1000L, gameState = 1, players = players.toMutableList())
				merchants.clear()
				DevUtils.sendDebugChat("&&aEXPEDITIONS &&fStarted &&7(gameState = 1, ${players.size} size).", "EXPEDITION_DEBUGGER")
			}

			event.string.contains("Room complete") && !currentSession.roomIsCompleteAndWaiting() -> {
				currentSession.gameState++
				DevUtils.sendDebugChat("&&aEXPEDITIONS &&fNew game state &&7(ChatMessage): &&a${currentSession.gameState}.", "EXPEDITION_DEBUGGER")
				checkPlayerCount()
			}

			event.string.contains("ViceExp") -> event.processCommunications()

			newItemRegex.matches(event.string) -> {
				val match = newItemRegex.find(event.string) ?: return

				val itemName = match.groupValues[1]
				val room = getRoomByZ()

				if (lastClickedShopItemIndex < 0 || lastClickedShopItemTime.timeDelta() > 0.5.seconds.ms()) return
				val matchedItem = merchants[room]?.get(lastClickedShopItemIndex) ?: return

				if (matchedItem.cleanName() == itemName) {
					merchants[room]?.set(lastClickedShopItemIndex, ItemStack.EMPTY)
					AutoCommunications.shareShopkeeperBuy(room, lastClickedShopItemIndex, matchedItem)
				}
			}
		}
	}

	@SubscribeEvent
	fun onInventoryOpen(event: ChestRenderEvent.Slots) {
		if (!World.Expeditions.isInWorld() || !event.isActuallyFullyRendered()) return
		if (!event.chestName.contains("Expedition Merchant")) return

		val roomIndex = getRoomByZ()
		val list = mutableListOf<ItemStack>()

		merchantSlots.forEach { slotId ->
			val item = event.slots[slotId].stack
			if (item.item == Items.BARRIER || item.item == Items.GRAY_STAINED_GLASS_PANE) return@forEach

			list.add(item)
		}

		if (!merchants.containsKey(roomIndex) && list.size > 0) {
			AutoCommunications.shareShopkeeperFound(roomIndex, list)
		}

		merchants[roomIndex] = list
	}

	@SubscribeEvent
	fun onSlotClick(event: SlotClickEvent) {
		if (!World.Expeditions.isInWorld() || !event.chestName.contains("Expedition Merchant")) return
		if (!merchantSlots.contains(event.slotId)) return

		lastClickedShopItemIndex = merchantSlots.indexOf(event.slotId)
		lastClickedShopItemTime = System.currentTimeMillis()
	}

	@SubscribeEvent
	fun onBlockUpdate(event: BlockUpdateEvent) {
		if (!World.Expeditions.isInWorld() || !currentSession.roomIsCompleteAndWaiting()) return
		val original = Utils.getWorld()?.getBlockState(event.pos) ?: return

		if (original.block == Blocks.COAL_BLOCK && event.new.block == Blocks.AIR) {
			currentSession.gameState++
			DevUtils.sendDebugChat("&&aEXPEDITIONS &&fNew game state &&7(BlockUpdate): &&a${currentSession.gameState}.", "EXPEDITION_DEBUGGER")

			if (currentSession.gameState == 6) {
				currentSession.gameState++
				DevUtils.sendDebugChat("&&aEXPEDITIONS &&fNew game state &&7(CooldownRoom): &&a${currentSession.gameState}.", "EXPEDITION_DEBUGGER")
			}

			checkPlayerCount()
		}
	}

	@SubscribeEvent
	fun onPlayerEntity(event: EntityDeathEvent) {
		if (!World.Expeditions.isInWorld()) return
		currentSession.players.remove(event.entity)
	}

	fun getRoomByZ(): Int {
		val player = Utils.getPlayer() ?: return -1
		if (!World.Expeditions.isInWorld()) return -2

		val z = player.z

		for ((index, minZ) in roomZBounds.withIndex()) {
			if (z < minZ) {
				return index
			}
		}

		return roomZBounds.size
	}

	private fun checkPlayerCount(sendWarning: Boolean = true) {
		val players = Utils.getWorld()?.players?.filterNot { it.name.string.startsWith("CIT-") || it.customName?.string?.startsWith("CIT-") == true } ?: listOf()
		val currentSize = currentSession.players.size

		if (players.size > currentSize) {
			if (sendWarning) DevUtils.sendWarningMessage("Player Count &&7(${players.size})&&f is higher than expected &&7($currentSize)&&f! Overwriting data.")
			currentSession.players = players.toMutableList()
		}
	}
}
