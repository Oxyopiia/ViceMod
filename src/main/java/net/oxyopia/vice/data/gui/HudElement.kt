package net.oxyopia.vice.data.gui

import gg.essential.elementa.utils.withAlpha
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.oxyopia.vice.config.HudEditor
import net.oxyopia.vice.events.HudEditorRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawBackground
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.Utils.clamp
import net.oxyopia.vice.utils.Utils.getClient
import org.lwjgl.glfw.GLFW
import java.awt.Color
import kotlin.math.round

abstract class
	HudElement(private val displayName: String, defaultState: Position, private val padding: Float = 2f) :
	ClickableWidget(-1, 0, 0, 0, null) {

	var position: Position = defaultState

	abstract fun updatePosition(position: Position)
	abstract fun Position.drawPreview(context: DrawContext): Pair<Float, Float>?

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
		syncHoverState()

		val color = if (isResetting()) Color.red else Color.gray
		val alpha = if (isHovered) 0.5f else 0.3f

		position.drawPreview(context)?.run {
			position.drawBackground(this, context, padding = padding, color = color.withAlpha(alpha)).apply {
				x = minX.toInt()
				y = minY.toInt()
				width = width().toInt()
				height = height().toInt()
			}

			position.drawInfo(context)
			visible = true
		}
	}

	private fun Position.drawInfo(context: DrawContext) {
		val previewPos = Position(context.scaledWindowWidth.toFloat() / 2, 10f)
		val previewText = listOf(
			"&&b$displayName",
			"&&7x: &&a${x.toInt()}&&7, y: &&a${y.toInt()}&&7, scale: &&a${round(scale * 10) / 10.0}"
		)

		if (isHovered) {
			previewPos.drawStrings(previewText, context, 1000)
		}
	}

	override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
		if (!isDragging()) return false

		position.x += deltaX.toFloat()
		position.y += deltaY.toFloat()
		limitPosition()

		return true
	}

	override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
		if (!isHovered) return false

		position.scale += amount.toFloat() / 10
		limitScale()

		return true
	}

	override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
		if (!isHovered && !isSelected) return false

		when (keyCode) {
			GLFW.GLFW_KEY_W -> position.y -= 1
			GLFW.GLFW_KEY_A -> position.x -= 1
			GLFW.GLFW_KEY_S -> position.y += 1
			GLFW.GLFW_KEY_D -> position.x += 1

			GLFW.GLFW_KEY_UP -> position.y -= 1
			GLFW.GLFW_KEY_LEFT -> position.x -= 1
			GLFW.GLFW_KEY_DOWN -> position.y += 1
			GLFW.GLFW_KEY_RIGHT -> position.x += 1

			GLFW.GLFW_KEY_EQUAL -> position.scale += 0.1f
			GLFW.GLFW_KEY_KP_ADD -> position.scale += 0.1f
			GLFW.GLFW_KEY_MINUS -> position.scale -= 0.1f
			GLFW.GLFW_KEY_KP_SUBTRACT -> position.scale -= 0.1f

			GLFW.GLFW_KEY_TAB -> position.centered = !position.centered

			GLFW.GLFW_KEY_V -> position.y = getClient().window.scaledHeight / 2f
			GLFW.GLFW_KEY_H -> {
				position.x = (getClient().window.scaledWidth / 2f)
				position.centered = true
			}

			else -> return false
		}

		limitPosition()
		limitScale()
		selectedElement = this
		return true
	}

	fun onRightClick() {
		if (isResetting()) {
			position = Position(0f, 0f, scale = 1f, centered = false)
			resettingElement = null
			return
		}

		resettingElement = this
	}

	fun invertCentering() {
		position.centered = !position.centered
	}

	override fun isHovered(): Boolean = (visible && hoveredElement == this)

	override fun isSelected(): Boolean = (visible && selectedElement == this)

	private fun isDragging(): Boolean = (visible && draggedElement == this)

	private fun isResetting(): Boolean = (visible && resettingElement == this)

	private fun syncHoverState() {
		hoveredElements.removeAll { it.displayName == this.displayName }

		if (hovered) {
			hoveredElements.add(this)
		}
	}

	private fun limitScale() {
		position.scale = position.scale.clamp(0.5f, 3f)
	}

	private fun limitPosition() {
		position.x = position.x.clamp(0f, getClient().window.scaledWidth - padding - 1f)
		position.y = position.y.clamp(0f, getClient().window.scaledHeight - padding - 1f)
	}

	companion object {
		var selectedElement: HudElement? = null
		var draggedElement: HudElement? = null
		var resettingElement: HudElement? = null

		val hoveredElements = mutableListOf<HudElement>()
		val hoveredElement: HudElement?
			get() = hoveredElements.sortedBy { it.displayName }.getOrNull(0)
	}

	override fun appendClickableNarrations(builder: NarrationMessageBuilder?) {}
}