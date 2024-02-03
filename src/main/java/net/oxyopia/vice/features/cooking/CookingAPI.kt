package net.oxyopia.vice.features.cooking

import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ServerChatMessageEvent
import net.oxyopia.vice.events.TitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.ms
import net.oxyopia.vice.utils.Utils.timeDelta
import kotlin.time.Duration.Companion.seconds

object CookingAPI {
	var currentOrder = CookingOrder.NONE
	var orderCurrentItemIndex = 0
	var stock: Int = -10000
	var heldItem = CookingItem.NONE

	private var lastSeenNewOrder = 0L

	private val heldItemRegex = Regex("You are currently holding (?:a |an )?(.*)")
	private val ingredientsRegex = Regex("(\\d*) Ingredients Left")
	private val stockRegex = Regex("Your current stock is (\\d*)/\\d*")

	private fun updateOrder(order: CookingOrder) {
		currentOrder = order
		orderCurrentItemIndex = 0
		DevUtils.sendDebugChat("&&6COOKING &&rUpdated current order to &&e${currentOrder.name}", "COOKING_DEBUGGER")
	}

	@SubscribeEvent
	fun onTitle(event: TitleEvent) {
		if (event.title.contains("+1 Stock")) stock += 1
	}

	@SubscribeEvent
	fun onChatMessage(event: ServerChatMessageEvent) {
		if (!World.Burger.isInWorld()) return
		val content = event.string
		val hideHandledMessages = Vice.config.HIDE_HANDLED_COOKING_MESSAGES

		if (content.contains("NEW ORDER") || content.contains("BOSS ORDER")) {
			lastSeenNewOrder = System.currentTimeMillis()
			return

		}

		// Matches a new Order if logic above has recently ran
		else if (lastSeenNewOrder.timeDelta() <= 1.seconds.ms()) {
			val order = CookingOrder.getByName(content) ?: return
			updateOrder(order)

			return
		}

		else if (content.contains("Order Complete") || content.contains("Incorrect Ingredient ")) {
			updateOrder(CookingOrder.NONE)
			if (!Vice.config.AUTO_APPLY_BREAD) heldItem = CookingItem.NONE

			return
		}

		heldItemRegex.find(content.removeSuffix("."))?.let {
			heldItem = CookingItem.getByName(it.groupValues[1]) ?: CookingItem.NONE
			DevUtils.sendDebugChat("&&6COOKING &&rUpdated held item to &&d${heldItem.name}", "COOKING_DEBUGGER")

			if (hideHandledMessages) {
				event.cancel()

				if (heldItem == CookingItem.COOKED_MEAT) {
					Utils.playSound("block.note_block.pling", pitch = 1.5f, volume = 3f)
				}
			}
			return
		}

		ingredientsRegex.find(content)?.apply {
			if (currentOrder == CookingOrder.NONE) return

			try {
				val ingredientsRemaining = groupValues[1].toInt()
				if (ingredientsRemaining > 0) {
					orderCurrentItemIndex = currentOrder.recipe.size - ingredientsRemaining
					DevUtils.sendDebugChat("&&6COOKING &&rNext Item: ${currentOrder.recipe[orderCurrentItemIndex]}", "COOKING_DEBUGGER")
				}

				if (hideHandledMessages) event.cancel()
			} catch (e: NumberFormatException) {
				DevUtils.sendErrorMessage(e, "An error occurred casting a regex group value (${groupValues[1]}) to an Int!")
			}

			return
		}

		stockRegex.find(content)?.apply {
			try {
				stock = groupValues[1].toInt()
				DevUtils.sendDebugChat("&&6COOKING &&Updated Stock to $stock", "COOKING_DEBUGGER")

				if (hideHandledMessages) event.cancel()

			} catch (e: NumberFormatException) {
				DevUtils.sendErrorMessage(e, "An error occurred casting a regex group value (${groupValues[1]}) to an Int!")
			}

			return
		}
	}
}