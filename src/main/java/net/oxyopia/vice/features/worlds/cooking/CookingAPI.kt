package net.oxyopia.vice.features.worlds.cooking

import net.oxyopia.vice.Vice
import net.oxyopia.vice.config.features.worlds.CookingStorage
import net.oxyopia.vice.data.Debugger
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.TitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.SoundUtils
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.seconds

object CookingAPI {
	private val cooking: CookingStorage get() = Vice.storage.cooking
	val currentOrder: CookingOrder get() = CookingOrder.getByName(cooking.currentOrder) ?: CookingOrder.NONE
	val orderCurrentItemIndex get() = cooking.currentOrderProgress
	val stock: Int get() = cooking.stock

	var heldItem = CookingItem.NONE

	private var lastSeenNewOrder = 0L

	private val heldItemRegex by lazy { Regex("You are currently holding (?:a |an )?(.*)") }
	private val ingredientsRegex by lazy { Regex("(\\d+) Ingredients Left") }
	private val stockRegex by lazy { Regex("Your current stock is (\\d+)/\\d+") }

	private fun updateOrder(order: CookingOrder) {
		cooking.currentOrder = order.displayName
		cooking.currentOrderProgress = 0
		if (order != CookingOrder.NONE) {
			cooking.totalBurgerRequests[order.name] = cooking.totalBurgerRequests.getOrDefault(order.name, 0) + 1
		}

		Vice.storage.markDirty()
		Debugger.COOKING.debug("Updated current order to §e${currentOrder.name}")
	}

	@SubscribeEvent
	fun onTitle(event: TitleEvent) {
		if (event.title.contains("+1 Stock")) {
			cooking.stock += 1
			Vice.storage.markDirty()
		}
	}

	@SubscribeEvent
	fun onChatMessage(event: ChatEvent) {
		if (!World.Burger.isInWorld()) return

		val content = event.string
		val hideHandledMessages = Vice.config.HIDE_HANDLED_COOKING_MESSAGES

		when {
			content.contains("NEW ORDER") || content.contains("BOSS ORDER") -> {
				lastSeenNewOrder = System.currentTimeMillis()
			}

			lastSeenNewOrder.timeDelta() <= 1.seconds -> {
				val order = CookingOrder.getByName(content) ?: return
				updateOrder(order)
			}

			content.contains("Order Complete") || content.contains("Incorrect Ingredient ") -> {
				if (content.contains("Order Complete")) {
					cooking.totalBurgersComplete[currentOrder.name] = cooking.totalBurgersComplete.getOrDefault(currentOrder.name, 0) + 1
				}

				updateOrder(CookingOrder.NONE)
				if (!Vice.config.AUTO_APPLY_BREAD) heldItem = CookingItem.NONE
			}

			heldItemRegex.find(content.removeSuffix(".")) != null -> {
				val match = heldItemRegex.find(content.removeSuffix("."))?.groupValues?.get(1)
				heldItem = CookingItem.getByName(match.toString()) ?: CookingItem.NONE

				Debugger.COOKING.debug("Updated held item to §d${heldItem.name}")

				if (hideHandledMessages) {
					event.cancel()
					if (heldItem == CookingItem.COOKED_MEAT) {
						SoundUtils.playSound("block.note_block.pling", pitch = 1.5f, volume = 3f)
						BurgerTimer.cooking = null
					}
				}
			}

			ingredientsRegex.find(content) != null -> {
				val ingredientsRemaining = ingredientsRegex.find(content)?.groupValues?.get(1)?.toIntOrNull() ?: return

				if (currentOrder != CookingOrder.NONE && ingredientsRemaining > 0) {
					cooking.currentOrderProgress = currentOrder.recipe.size - ingredientsRemaining
					Vice.storage.markDirty()
					Debugger.COOKING.debug("Next Item: ${currentOrder.recipe[orderCurrentItemIndex]}")
				}

				if (hideHandledMessages) event.cancel()
			}

			stockRegex.find(content) != null -> {
				val stockValue = stockRegex.find(content)?.groupValues?.get(1)?.toIntOrNull() ?: return

				cooking.stock = stockValue
				Vice.storage.markDirty()
				Debugger.COOKING.debug("Updated Stock to $stock")

				if (hideHandledMessages) event.cancel()
			}
		}
	}
}