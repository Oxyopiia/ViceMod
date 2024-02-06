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
}