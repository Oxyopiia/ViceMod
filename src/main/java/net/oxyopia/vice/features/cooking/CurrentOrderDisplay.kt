package net.oxyopia.vice.features.cooking

import net.minecraft.client.MinecraftClient
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Position
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawStrings

object CurrentOrderDisplay {
	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		val mc = MinecraftClient.getInstance()
		if (mc.player == null || !World.Burger.isInWorld() || mc.player!!.y <= 100.0) return

		val list: MutableList<String> = mutableListOf()

		val stock = CookingAPI.stock
		val currentOrder = CookingAPI.currentOrder
		val currentItemIndex = CookingAPI.orderCurrentItemIndex
		val heldItem = CookingAPI.heldItem

		val pos = Position(event.scaledWidth / 2f, 8f)

		if (Vice.config.DEVMODE) {
			pos.x = event.scaledWidth / 2f + Vice.devConfig.COOKING_ORDER_HUD_X_OFFSET_LOCATION
			pos.y = event.scaledHeight / 2f + Vice.devConfig.COOKING_ORDER_HUD_Y_OFFSET_LOCATION
		}

		if (Vice.config.SHOW_NEXT_COOKING_ITEM && currentOrder == CookingOrder.NONE) {
			var text = "&&cNo Order"

			if (Vice.config.SIMPLIFY_COOKING_DISPLAYS && stock >= 0) {
				text += "&&7 (&&${getStockColor()}${stock}&&7)"
			}

			list.add(text)

		} else if (Vice.config.SHOW_NEXT_COOKING_ITEM) {
			val recipe = currentOrder.recipe

			val orderDisplayColor = if (currentOrder.isBossOrder) "&&5" else "&&a"
			list.add(orderDisplayColor + "&&l${currentOrder.displayName}")

			var text = "&&7Next Ingredient: &&6${recipe[currentItemIndex].displayName}"
			if (recipe[currentItemIndex] == heldItem) text = text.replace("&&6", "&&a")
			if ((recipe.size - 1) > currentItemIndex) text += "&&8 -> ${recipe[currentItemIndex + 1].displayName}"

			if (Vice.config.SIMPLIFY_COOKING_DISPLAYS) {
				text = text.removePrefix("&&7Next Ingredient: ")

				if (Vice.config.SHOW_COOKING_STOCK_INFO && stock >= 0) {
					text += "&&7 (&&${getStockColor()}${stock}&&7)"
				}
			}

			list.add(text)
		}

		if (Vice.config.SHOW_COOKING_STOCK_INFO && !Vice.config.SIMPLIFY_COOKING_DISPLAYS && stock >= 0) {
			list.add("&&7Stock: &&${getStockColor()}${stock}")
		}

		pos.drawStrings(list, event.context)
	}

	private fun getStockColor(): String {
		val stock = CookingAPI.stock

		@Suppress("KotlinConstantConditions")
		return when {
			stock >= 25 -> "a"
			stock in 11..24 -> "e"
			stock <= 10 -> "c"
			else -> "7"
		}
	}
}