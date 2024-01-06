package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.RenderInGameHudEvent
import net.oxyopia.vice.events.ServerChatMessageEvent
import net.oxyopia.vice.events.TitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.enums.World
import java.awt.Color

object World4Features {
	private var currentOrder = CookingOrder.NONE
	private var orderCurrentItemIndex = 0
	private var stock: Int = -10000
	private var heldItem = CookingItem.NONE
	private var lastSeenNewOrder = 0L

	private val heldItemRegex = Regex("You are currently holding (?:a |an )?(.*)")
	private val ingredientsRegex = Regex("(\\d*) Ingredients Left")
	private val stockRegex = Regex("Your current stock is (\\d*)/\\d*")

	enum class CookingItem(val displayName: String) {
		BREAD("Bread"),
		CHEESE("Cheese"),
		LETTUCE("Lettuce"),
		TOMATO("Tomato"),
		RAW_MEAT("Raw Meat"),
		COOKED_MEAT("Cooked Burger"),
		MYSTIC_BACON("Mystic Bacon"),
		EMPTY_CUP("Empty Cup"),
		SODA("Soda"),
		NONE("");

		companion object {
			fun getByName(displayName: String): CookingItem? {
				return entries.firstOrNull { it.displayName == displayName }
			}
		}
	}

	enum class CookingOrder(val displayName: String, val recipe: List<CookingItem>) {
		HAMBURGER("Hamburger", listOf(CookingItem.BREAD, CookingItem.COOKED_MEAT, CookingItem.BREAD)),
		CHEESEBURGER(
			"Cheeseburger",
			listOf(CookingItem.BREAD, CookingItem.COOKED_MEAT, CookingItem.CHEESE, CookingItem.BREAD)
		),
		DOUBLE_CHEESEBURGER(
			"Double Cheeseburger",
			listOf(
				CookingItem.BREAD,
				CookingItem.COOKED_MEAT,
				CookingItem.CHEESE,
				CookingItem.BREAD,
				CookingItem.COOKED_MEAT,
				CookingItem.CHEESE,
				CookingItem.BREAD
			)
		),
		ULTIMEATIUM(
			"Ultimeatium",
			listOf(
				CookingItem.BREAD,
				CookingItem.COOKED_MEAT,
				CookingItem.CHEESE,
				CookingItem.LETTUCE,
				CookingItem.TOMATO,
				CookingItem.BREAD
			)
		),
		OBESE_VICE_BURGER(
			"Obese Vice's Triple Patty Bacon Bomb",
			listOf(
				CookingItem.BREAD,
				CookingItem.COOKED_MEAT,
				CookingItem.CHEESE,
				CookingItem.BREAD,
				CookingItem.COOKED_MEAT,
				CookingItem.MYSTIC_BACON,
				CookingItem.CHEESE,
				CookingItem.BREAD,
				CookingItem.COOKED_MEAT,
				CookingItem.CHEESE,
				CookingItem.BREAD
			)
		),
		NONE("", emptyList());

		companion object {
			fun getByName(displayName: String): CookingOrder? {
				return entries.firstOrNull { it.displayName == displayName }
			}
		}
	}

	private fun updateOrder(order: CookingOrder) {
		currentOrder = order
		orderCurrentItemIndex = 0
		DevUtils.sendDebugChat("&&6COOKING &&rUpdated current order to &&e${currentOrder.name}", "COOKING_DEBUGGER")
	}

	@SubscribeEvent
	fun onChatMessage(event: ServerChatMessageEvent) {
		if (!World.Burger.isInWorld()) return
		val content = event.content.string

		heldItemRegex.find(content.removeSuffix("."))?.let {
			heldItem = CookingItem.getByName(it.groupValues[1]) ?: CookingItem.NONE
			DevUtils.sendDebugChat("&&6COOKING &&rUpdated held item to &&d${heldItem.name}", "COOKING_DEBUGGER")

			return
		}

		if (content.contains("NEW ORDER")) {
			lastSeenNewOrder = System.currentTimeMillis()
		} else if (System.currentTimeMillis() - lastSeenNewOrder <= 1 * 1000) {
			val order = CookingOrder.getByName(content) ?: return
			updateOrder(order)
		}

		if (content.contains("Order Complete") || content.contains("Incorrect Ingredient ")) updateOrder(CookingOrder.NONE)

		ingredientsRegex.find(content)?.apply {
			try {
				val ingredientsRemaining = groupValues[1].toInt()
				if (ingredientsRemaining > 0) {
					orderCurrentItemIndex = currentOrder.recipe.size - ingredientsRemaining
					DevUtils.sendDebugChat("&&6COOKING &&rNext Item: ${currentOrder.recipe[orderCurrentItemIndex]}", "COOKING_DEBUGGER")
				}
			} catch (e: NumberFormatException) {
				DevUtils.sendErrorMessage(e, "An error occurred casting a regex group value (${groupValues[1]}) to an Int!")
			}

			return
		}

		stockRegex.find(content)?.apply {
			try {
				stock = groupValues[1].toInt()
				DevUtils.sendDebugChat("&&6COOKING &&Updated Stock to ${stock}", "COOKING_DEBUGGER")
			} catch (e: NumberFormatException) {
				DevUtils.sendErrorMessage(e, "An error occurred casting a regex group value (${groupValues[1]}) to an Int!")
			}
		}
	}

	@SubscribeEvent
	fun onTitle(event: TitleEvent) {
		if (event.title.contains("+1 Stock")) stock += 1
	}

	@SubscribeEvent
	fun onHudRender(event: RenderInGameHudEvent) {
		if (MinecraftClient.getInstance().player == null || !World.Burger.isInWorld()) return
		if (MinecraftClient.getInstance().player?.y!! <= 100.0) return

		val xPos = event.scaledWidth / 2
		var yPos = 8

		if (Vice.config.SHOW_NEXT_COOKING_ITEM && currentOrder == CookingOrder.NONE) {
			var text = "&&cNo Order"
			if (Vice.config.SIMPLIFY_COOKING_DISPLAYS && stock >= 0) text += "&&7 (&&${getStockColor()}${stock}&&7)"

			HudUtils.drawText(event.context.matrices, MinecraftClient.getInstance().textRenderer, text, xPos, yPos, Color(0, 0, 0, 255).rgb, centered = true)
			yPos += 10
		} else if (Vice.config.SHOW_NEXT_COOKING_ITEM) {
			val recipe = currentOrder.recipe

			HudUtils.drawText(event.context.matrices, MinecraftClient.getInstance().textRenderer, "&&a&&l${currentOrder.displayName}", xPos, yPos, Color(0, 0, 0, 255).rgb, centered = true)

			var text = "&&7Next Ingredient: &&6${recipe[orderCurrentItemIndex].displayName}"
			if ((recipe.size - 1) > orderCurrentItemIndex) text += "&&8 -> ${recipe[orderCurrentItemIndex + 1].displayName}"

			if (Vice.config.SIMPLIFY_COOKING_DISPLAYS) {
				text = text.removePrefix("&&7Next Ingredient: ")
				if (stock >= 0) text += "&&7 (&&${getStockColor()}${stock}&&7)"
			}

			HudUtils.drawText(
				event.context.matrices,
				MinecraftClient.getInstance().textRenderer,
				text,
				xPos,
				yPos + 10,
				Color(0, 0, 0, 255).rgb,
				centered = true
			)

			yPos += 20
		}

		if (Vice.config.SHOW_COOKING_STOCK_INFO && !Vice.config.SIMPLIFY_COOKING_DISPLAYS && stock >= 0) {
			HudUtils.drawText(event.context.matrices, MinecraftClient.getInstance().textRenderer, "&&7Stock: &&${getStockColor()}${stock}", xPos, yPos, Color(0, 0, 0, 255).rgb, centered = true)
		}
	}

	private fun getStockColor(): String {
		return when {
			stock >= 25 -> "a"
			stock in 6..24 -> "e"
			stock <= 5 -> "c"
			else -> "7"
		}
	}
}