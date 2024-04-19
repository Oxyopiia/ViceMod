package net.oxyopia.vice.features.expeditions

import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.BlockUpdateEvent
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.ChestRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.Utils

object ExpeditionAPI {
	var currentSession: ExpeditionRun = ExpeditionRun(-1L)
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
	private val shopkeeperPriceRegex = Regex("(\\d+) Credits")

	val merchants = hashMapOf<Int, MutableList<ItemStack>>()

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

	@SubscribeEvent
	fun onChat(event: ChatEvent) {
		if (event.string.contains("Right click on the door to begin the Expedition.")) {
			DevUtils.sendDebugChat("&&aEXPEDITIONS &&fStarted.", "EXPEDITION_DEBUGGER")
			currentSession = ExpeditionRun(System.currentTimeMillis() - 1000L, gameState = 1)
			merchants.clear()
		}

		else if (event.string.contains("Room complete")) {
			currentSession.gameState++
			DevUtils.sendDebugChat("&&aEXPEDITIONS &&fNew game state &&7(ChatMessage): &&a${currentSession.gameState}.", "EXPEDITION_DEBUGGER")
		}
	}

	private val sellerSlots = listOf(20, 22, 24)

	@SubscribeEvent
	fun onInventoryOpen(event: ChestRenderEvent.Slots) {
		if (!World.Expeditions.isInWorld() || !event.isFullyRendererd()) return
		if (!event.chestName.contains("Expedition Merchant")) return

		val roomIndex = getRoomByZ()
		val list = mutableListOf<ItemStack>()

		sellerSlots.forEach { slotId ->
			val item = event.slots[slotId].stack
			if (item.item == Items.BARRIER || item.item == Items.GRAY_STAINED_GLASS_PANE) return@forEach

			list.add(item)
		}

		merchants[roomIndex] = list
	}

	@SubscribeEvent
	fun onBlockUpdate(event: BlockUpdateEvent) {
		if (!World.Expeditions.isInWorld() || !currentSession.roomIsCompleteAndWaiting()) return
		val original = Utils.getWorld()?.getBlockState(event.pos) ?: return

		DevUtils.sendDebugChat(original.block.name.string)
		if (original.block == Blocks.COAL_BLOCK) {
			currentSession.gameState++
			if (currentSession.gameState == 6) currentSession.gameState++

			DevUtils.sendDebugChat("&&aEXPEDITIONS &&fNew game state &&7(BlockUpdate): &&a${currentSession.gameState}.", "EXPEDITION_DEBUGGER")
		}
	}
}
