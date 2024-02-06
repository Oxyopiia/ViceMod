package net.oxyopia.vice.config

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.utils.Utils
import org.lwjgl.glfw.GLFW

object HudEditor : Screen(Text.of("Vice GUI Editor")) {
	override fun render(context: DrawContext?, mouseX: Int, mouseY: Int, delta: Float) {
		super.render(context, mouseX, mouseY, delta)
	}

	override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
		if (keyCode == GLFW.GLFW_KEY_T) Utils.sendViceMessage("Still in HUD Editor!")

		HudElement.hoveredElement?.keyPressed(keyCode, scanCode, modifiers)
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
		if (HudElement.draggedElement == null) {
			HudElement.draggedElement = HudElement.hoveredElement
		}

		return super.mouseClicked(mouseX, mouseY, button)
	}

	override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
		HudElement.draggedElement = null

		return super.mouseReleased(mouseX, mouseY, button)
	}
}