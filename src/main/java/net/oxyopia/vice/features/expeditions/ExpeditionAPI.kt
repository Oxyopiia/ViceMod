package net.oxyopia.vice.features.expeditions

import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.oxyopia.vice.data.ChatColor
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
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.Utils
import java.awt.Color
import kotlin.time.Duration.Companion.seconds


object ExpeditionAPI {
	var currentSession: ExpeditionRun = ExpeditionRun(-1L)

	private val newItemRegex = Regex("\\+\\d+ (.*)")
	private val merchantSlots = listOf(20, 22, 24)
	internal val rooms = listOf(
		Room(0, -130.0, "Starter", RoomType.STARTER),
		Room(1, -107.0, "Large"),
		Room(2, -76.0, "Hallway"),
		Room(3, -45.0, "Lootbox", RoomType.LOOTBOX),
		Room(4, -26.0, "Large"),
		Room(5, 4.5, "Horizontal"),
		Room(6, 21.0, "Large"),
		Room(7, 52.0, "Large"),
		Room(8, 83.0, "XL"),
		Room(9, 118.0, "Boss", RoomType.BOSS),
	)

	internal data class Room(
		val id: Int,
		val minZ: Double,
		val name: String,
		val type: RoomType = RoomType.MOB
	) {
		fun hasMerchant() = merchants.containsKey(id)
	}

	internal enum class RoomType(val color: Color) {
		STARTER(ChatColor.YELLOW.color),
		MOB(Color.white),
		LOOTBOX(ChatColor.GOLD.color),
		BOSS(ChatColor.DARK_RED.color)
	}

	private var lastClickedShopItemIndex = -1
	private var lastClickedShopItemTime: Long = -1L
	val merchants = hashMapOf<Int, MutableList<ItemStack>>()

	internal fun isInExpedition(): Boolean = Utils.getDTWorld()?.type == World.WorldType.EXPEDITION

	@SubscribeEvent
	fun onChat(event: ChatEvent) {
		if (!World.Expeditions.isInWorld()) return

		when {
			event.string.startsWith("Right click on the door to begin the Expedition.") -> {
				val players = Utils.getWorld()?.players?.filterNot { it.name.string.startsWith("CIT-") || it.customName?.string?.startsWith("CIT-") == true } ?: listOf()
				currentSession = ExpeditionRun(System.currentTimeMillis() - 1000L, gameState = 1, players = players.toMutableList())
				merchants.clear()
				DevUtils.sendDebugChat("&&aEXPEDITIONS &&fStarted &&7(gameState = 1, ${players.size} size).", "EXPEDITION_DEBUGGER")
			}

			event.string.startsWith("Room complete") && !currentSession.roomIsCompleteAndWaiting() -> {
				currentSession.gameState++
				DevUtils.sendDebugChat("&&aEXPEDITIONS &&fNew game state &&7(ChatMessage): &&a${currentSession.gameState}.", "EXPEDITION_DEBUGGER")
				checkPlayerCount()
			}

			event.string.contains("ViceExp") -> event.processCommunications()

			newItemRegex.matches(event.string) -> {
				val match = newItemRegex.find(event.string) ?: return

				val itemName = match.groupValues[1]
				val room = getRoomByZ() ?: return

				if (lastClickedShopItemIndex < 0 || lastClickedShopItemTime.timeDelta() > 0.5.seconds) return
				val matchedItem = merchants[room.id]?.get(lastClickedShopItemIndex) ?: return

				if (matchedItem.cleanName() == itemName) {
					AutoCommunications.shareShopkeeperBuy(room.id, lastClickedShopItemIndex, matchedItem)
					merchants[room.id]?.set(lastClickedShopItemIndex, ItemStack.EMPTY)
				}
			}
		}
	}

	@SubscribeEvent
	fun onInventoryOpen(event: ChestRenderEvent.Slots) {
		if (!World.Expeditions.isInWorld() || !event.isActuallyFullyRendered()) return
		if (!event.chestName.contains("Expedition Merchant")) return

		val roomIndex = getRoomByZ() ?: return
		val list = mutableListOf<ItemStack>()

		merchantSlots.forEach { slotId ->
			val item = event.slots[slotId].stack
			if (item.item == Items.GRAY_STAINED_GLASS_PANE) return@forEach

			list.add(item)
		}

		if (!merchants.containsKey(roomIndex.id) && list.size > 0) {
			AutoCommunications.shareShopkeeperFound(roomIndex.id, list)
		}

		merchants[roomIndex.id] = list
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

	internal fun getRoomByZ(): Room? {
		val player = Utils.getPlayer() ?: return null
		if (!World.Expeditions.isInWorld()) return null
		val z = player.z

		rooms.forEachIndexed { index, room ->
			if (index == rooms.lastIndex || z < rooms[index + 1].minZ) {
				return room
			}
		}

		return null
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
