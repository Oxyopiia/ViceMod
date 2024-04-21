package net.oxyopia.vice.features.expeditions

import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.expeditions.ExpeditionItemType.Companion.getExpeditionItemType
import net.oxyopia.vice.features.expeditions.ExpeditionRarity.Companion.getExpeditionRarity
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.math.roundToInt

object MerchantOverlay : HudElement("Merchant Overlay", Vice.storage.expeditions.merchantOverlayPos) {
	override fun shouldDraw(): Boolean = Vice.config.EXPEDITION_MERCHANT_OVERLAY
	override fun drawCondition(): Boolean = World.Expeditions.isInWorld()

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!shouldDraw() || !drawCondition()) return
		if (ExpeditionAPI.merchants.isEmpty() && !DevUtils.hasDevMode(Vice.devConfig.EXPEDITION_DEBUGGER)) return

		val state = ExpeditionAPI.currentSession.gameState
		val list = mutableListOf("&&a&&lMerchants")
		val merchants = ExpeditionAPI.merchants

		if (DevUtils.hasDevMode(Vice.devConfig.EXPEDITION_DEBUGGER)) {
			list.add("&&7gamestate: &&a$state")
			list.add("&&7elapsed: &&a${formatDuration(ExpeditionAPI.currentSession.startTime.timeDelta(), false)}")
			list.add("&&7room: &&a${ExpeditionAPI.getRoomByZ()}")

			list.add("")
			list.add("&&7stored players: &&a${ExpeditionAPI.currentSession.players.size}")
			ExpeditionAPI.currentSession.players.forEach { list.add("&&7- ${it.name.string}") }
		}

		merchants.forEach { merchant ->
			val roomId = merchant.key
			val items = merchant.value

			if (list.size > 1) list.add("")
			list.add("&&aRoom $roomId")

			items.forEachIndexed { _, item ->
				val cost = item.calcCost(state).roundToInt()
				val rarity = item.getExpeditionRarity() ?: return@forEachIndexed
				
				val itemColor = rarity.color
				var text = "&&6$cost"

				if (ExpeditionAPI.currentSession.roomIsCompleteAndWaiting()) {
					val nextCost = item.calcCost(state + 1).roundToInt()
					text += "&&8->$nextCost"
				}

				list.add("$itemColor${item.name.string}&&7: &&6$text &&6Credits")
			}
		}

		position.drawStrings(list, event.context)
	}

	private fun ItemStack.calcCost(gameState: Int = 0): Double {
		/**
		 * Item Value = round(rarity value * type value * (1 + (room number - 0.5) / 8))
		 * where type value is 1 when the item is a weapon, otherwise 0.5
		 * where rarity value is 5, 10, 25, 40, or 50 based on the ascending rarity
		 * where room number is the index of the current opened room
		 */

		val rarityValue = getExpeditionRarity()?.rarityValue ?: return -1.0
		val typeValue = getExpeditionItemType()?.typeValue ?: return -2.0

		return (rarityValue * typeValue * (1.0 + gameState.toDouble() / 16.0))
	}


	override fun storePosition(position: Position) {
		Vice.storage.expeditions.merchantOverlayPos = position
		Vice.storage.markDirty()
	}

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		val list = mutableListOf(
			"&&a&&lMerchants",
			"&&aRoom 1",
			"&&dC4&&7: &&622 Credits",
			"&&bCrystal Cleaver&&7: &&611 Credits",
			"&&bElectrostatic Hammer&&7: &&611 Credits",
			"",
			"&&aRoom 2",
			"&&dC4&&7: &&622 Credits",
			"&&6Raygun&&7: &&636 Credits",
			"&&aLeather Leggings&&7: &&68 Credits",
		)

		return position.drawStrings(list, context)
	}
}