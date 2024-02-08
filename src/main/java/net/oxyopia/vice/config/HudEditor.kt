package net.oxyopia.vice.config

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.Utils
import org.lwjgl.glfw.GLFW
import kotlin.math.round

object HudEditor : Screen(Text.of("Vice GUI Editor")) {
	override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
		super.render(context, mouseX, mouseY, delta)

		val element = HudElement.draggedElement ?: HudElement.hoveredElement
		element?.apply {
			val previewPos = Position(context.scaledWindowWidth.toFloat() / 2, 10f)
			val previewText = listOf(
				"&&b${getDisplayName()}",
				"&&7x: &&a${position.x.toInt()}&&7, y: &&a${position.y.toInt()}&&7, scale: &&a${round(position.scale * 10) / 10.0}"
			)

			previewPos.drawStrings(previewText, context, 1000)
		}
	}

	override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
		if (keyCode == GLFW.GLFW_KEY_T || keyCode == GLFW.GLFW_KEY_SLASH) {
			Utils.sendViceMessage("Still in HUD Editor!")
			Utils.playSound("block.note_block.pling", volume = 3f)
		}

		if (HudElement.hoveredElement != null) {
			HudElement.hoveredElement?.keyPressed(keyCode, scanCode, modifiers)
		} else HudElement.selectedElement?.keyPressed(keyCode, scanCode, modifiers)

		return super.keyPressed(keyCode, scanCode, modifiers)
	}

	override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
		HudElement.hoveredElement?.mouseScrolled(mouseX, mouseY, amount)
		return super.mouseScrolled(mouseX, mouseY, amount)
	}

	override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
		HudElement.draggedElement?.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
	}

	override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
		when (button) {
			GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> HudElement.hoveredElement?.invertCentering()
			GLFW.GLFW_MOUSE_BUTTON_RIGHT -> HudElement.hoveredElement?.onRightClick()
			GLFW.GLFW_MOUSE_BUTTON_LEFT -> {
				HudElement.resettingElement = null
				HudElement.selectedElement = HudElement.hoveredElement
				HudElement.draggedElement = HudElement.hoveredElement
			}
		}

		HudElement.hoveredElement?.save()
		return super.mouseClicked(mouseX, mouseY, button)
	}

	override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
		HudElement.draggedElement?.save()
		HudElement.draggedElement = null

		return super.mouseReleased(mouseX, mouseY, button)
	}

	override fun close() {
		Vice.storage.forceSave()
		super.close()
	}
}