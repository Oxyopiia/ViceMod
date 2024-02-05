package net.oxyopia.vice.features.cooking

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawStrings

object CurrentOrderDisplay : HudElement("Cooking Display", Vice.storage.cooking.currentOrderPos) {
	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		val mc = MinecraftClient.getInstance()
		if (mc.player == null || !World.Burger.isInWorld() || mc.player!!.y <= 100.0) return

		val displayList: MutableList<String> = mutableListOf()

		val stock = CookingAPI.stock
		val currentOrder = CookingAPI.currentOrder
		val currentItemIndex = CookingAPI.orderCurrentItemIndex
		val heldItem = CookingAPI.heldItem

		if (Vice.config.SHOW_NEXT_COOKING_ITEM && currentOrder == CookingOrder.NONE) {
			var text = "&&cNo Order"

			if (Vice.config.SIMPLIFY_COOKING_DISPLAYS && stock >= 0) {
				text += "&&7 (&&${getStockColor()}${stock}&&7)"
			}

			displayList.add(text)

		} else if (Vice.config.SHOW_NEXT_COOKING_ITEM) {
			val recipe = currentOrder.recipe

			val orderDisplayColor = if (currentOrder.isBossOrder) "&&5" else "&&a"
			displayList.add(orderDisplayColor + "&&l${currentOrder.displayName}")

			var text = "&&7Next Ingredient: &&6${recipe[currentItemIndex].displayName}"
			if (recipe[currentItemIndex] == heldItem) text = text.replace("&&6", "&&a")
			if ((recipe.size - 1) > currentItemIndex) text += "&&8 -> ${recipe[currentItemIndex + 1].displayName}"

			if (Vice.config.SIMPLIFY_COOKING_DISPLAYS) {
				text = text.removePrefix("&&7Next Ingredient: ")

				if (Vice.config.SHOW_COOKING_STOCK_INFO && stock >= 0) {
					text += "&&7 (&&${getStockColor()}${stock}&&7)"
				}
			}

			displayList.add(text)
		}

		if (Vice.config.SHOW_COOKING_STOCK_INFO && !Vice.config.SIMPLIFY_COOKING_DISPLAYS && stock >= 0) {
			displayList.add("&&7Stock: &&${getStockColor()}${stock}")
		}

		position.drawStrings(displayList, event.context)
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

	override fun updatePosition(position: Position) {
		Vice.storage.cooking.currentOrderPos = position
	}

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		val list = mutableListOf<String>()
		val simplified = Vice.config.SIMPLIFY_COOKING_DISPLAYS

		if (Vice.config.SHOW_NEXT_COOKING_ITEM) {
			list.add("&&6&&lHamburger")

			var text = "&&7Next Ingredient: &&aBread&&8 -> Cooked Burger"
			if (simplified) {
				text = text.removePrefix("&&7Next Ingredient: ")

				if (Vice.config.SHOW_COOKING_STOCK_INFO) {
					text += "&&7 (&&a25&&7)"
				}
			}

			list.add(text)
		}

		if (Vice.config.SHOW_COOKING_STOCK_INFO && !simplified) {
			list.add("&&7Stock: &&a25")
		}

		return position.drawStrings(list, context)
	}

	override fun appendClickableNarrations(builder: NarrationMessageBuilder?) {}
}