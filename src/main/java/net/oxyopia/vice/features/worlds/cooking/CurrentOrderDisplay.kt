package net.oxyopia.vice.features.worlds.cooking

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice.Companion.config
import net.oxyopia.vice.Vice.Companion.storage
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.worlds.cooking.CurrentOrderDisplay.mc
import net.oxyopia.vice.utils.hud.HudUtils.drawStrings

object CurrentOrderDisplay : HudElement(
	"Cooking Display",
	storage.cooking.currentOrderPos,
	{ storage.cooking.currentOrderPos = it },
	enabled = { config.COOKING_HELPER },
	drawCondition = { World.Burger.isInWorld() && (mc.player?.y ?: 0.0) > 100.0 },
	searchTerm = "cooking"
) {
	private val mc = MinecraftClient.getInstance()

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!canDraw()) return

		val displayList: MutableList<String> = mutableListOf()

		val heldItem = CookingAPI.heldItem
		val currentOrder = CookingAPI.currentOrder

		if (currentOrder == CookingOrder.NONE) {
			displayList.add("&&cNo Order${getStockText()}")
			position.drawStrings(displayList, event.context)
			return
		}

		val recipe = currentOrder.recipe
		val currentItemIndex = CookingAPI.orderCurrentItemIndex
		val currentItem = recipe[currentItemIndex]

		val orderDisplayColor = if (currentOrder.isBossOrder) "&&5" else "&&a"
		displayList.add(orderDisplayColor + "&&l${currentOrder.displayName}")

		val currentItemColor = when {
			currentItem == heldItem -> "&&a"
			currentItem == CookingItem.COOKED_MEAT && heldItem == CookingItem.RAW_MEAT -> "&&e"
			else -> "&&6"
		}
		var text = "$currentItemColor${currentItem.displayName}"

		if (config.SHOW_SUCCEEDING_INGREDIENTS && recipe.size - 1 > currentItemIndex) {
			text += "&&8 -> ${recipe[currentItemIndex + 1].displayName}"
		}

		text += getStockText()
		displayList.add(text)

		position.drawStrings(displayList, event.context)
	}

	private fun getStockText(): String {
		val stock = CookingAPI.stock

		if (stock < 0) return ""

		@Suppress("KotlinConstantConditions")
		val color = when {
			stock >= 25 -> "a"
			stock in 11..24 -> "e"
			stock <= 10 -> "c"
			else -> "7"
		}

		return " &&7(&&$color$stock&&7)"
	}

	override fun Position.drawPreview(context: DrawContext): Size {
		val list = mutableListOf("&&6&&lHamburger")

		var text = "&&aBread"
		if (config.SHOW_SUCCEEDING_INGREDIENTS) text += "&&8 -> Cooked Burger"
		text += " &&7(&&e22&&7)"
		list.add(text)

		return position.drawStrings(list, context)
	}
}