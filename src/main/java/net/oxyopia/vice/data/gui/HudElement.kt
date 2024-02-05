package net.oxyopia.vice.data.gui

import gg.essential.elementa.utils.withAlpha
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ClickableWidget
import net.oxyopia.vice.config.HudEditor
import net.oxyopia.vice.events.HudEditorRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawBackground
import net.oxyopia.vice.utils.HudUtils.drawStrings
import org.lwjgl.glfw.GLFW
import java.awt.Color

abstract class
	HudElement(private val displayName: String, defaultState: Position, private val padding: Float = 2f) :
	ClickableWidget(-1, 0, 0, 0, null) {
	var position: Position = defaultState

	abstract fun updatePosition(position: Position)
	abstract fun Position.drawPreview(context: DrawContext): Pair<Float, Float>

	open fun shouldDraw(): Boolean = true
	private fun shouldDrawInternal(): Boolean {
		return shouldDraw() && MinecraftClient.getInstance().currentScreen == HudEditor
	}

	@SubscribeEvent
	fun drawHudEditor(event: HudEditorRenderEvent) {
		render(event.context, event.mouseX, event.mouseY, event.tickDelta)
	}

	override fun renderButton(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
		if (!shouldDrawInternal()) return

		val previewPos = Position(context.scaledWindowWidth.toFloat() / 2, 10f)
		val previewText = listOf(
			"&&b$displayName",
			"&&7x: &&a${position.x.toInt()}&&7, y: &&a${position.y.toInt()}&&7, scale: &&a${position.scale}"
		)

		val alpha = if (isHovered) {
			previewPos.drawStrings(previewText, context)
			0.5f
		} else 0.3f

		position.drawPreview(context).run {
			position.drawBackground(this, context, padding = padding, color = Color.gray.withAlpha(alpha)).apply {
				x = minX.toInt()
				y = minY.toInt()
				width = (maxX - minX).toInt()
				height = (maxY - minY).toInt()
			}
		}
	}

	override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
		if (hovered) {
			position.x += deltaX.toFloat()
			position.y += deltaY.toFloat()
		}

		return hovered
	}

	override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
		return hovered
	}

	override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
		if (!hovered) return false

		when (keyCode) {
			GLFW.GLFW_KEY_W -> position.y += 1
			GLFW.GLFW_KEY_A -> position.x -= 1
			GLFW.GLFW_KEY_S -> position.y -= 1
			GLFW.GLFW_KEY_D -> position.x += 1

			GLFW.GLFW_KEY_UP -> position.y += 1
			GLFW.GLFW_KEY_LEFT -> position.x -= 1
			GLFW.GLFW_KEY_DOWN -> position.y -= 1
			GLFW.GLFW_KEY_RIGHT -> position.x += 1

			GLFW.GLFW_KEY_EQUAL -> position.scale += 0.1f
			GLFW.GLFW_KEY_KP_ADD -> position.scale += 0.1f
			GLFW.GLFW_KEY_MINUS -> position.scale -= 0.1f
			GLFW.GLFW_KEY_KP_SUBTRACT -> position.scale -= 0.1f

			else -> return false
		}

		return true
	}
}