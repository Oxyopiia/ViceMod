package net.oxyopia.vice.data.gui

import gg.essential.elementa.utils.withAlpha
import gg.essential.universal.UScreen
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.oxyopia.vice.Vice
import net.oxyopia.vice.config.HudEditor
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.events.HudEditorRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawBackground
import net.oxyopia.vice.utils.NumberUtils.clamp
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.Utils.getClient
import org.lwjgl.glfw.GLFW
import java.awt.Color
import kotlin.time.Duration.Companion.seconds

abstract class
	HudElement(
		private val displayName: String,
		var position: Position,
		private val padding: Float = 2f,
		private val searchTerm: String = displayName) :
	ClickableWidget(-1, 0, 0, 0, null)
{
	private var lastClicked: Long = -1

	abstract fun storePosition(position: Position)
	abstract fun Position.drawPreview(context: DrawContext): Size

	open fun drawCondition(): Boolean = true
	abstract fun shouldDraw(): Boolean
	private fun shouldDrawInternal(): Boolean {
		return shouldDraw() && MinecraftClient.getInstance().currentScreen == HudEditor && (Vice.storage.misc.showAllHudEditorElements || drawCondition())
	}

	fun save() = storePosition(position)

	@SubscribeEvent
	fun drawHudEditor(event: HudEditorRenderEvent) {
		visible = true // Done to bypass Minecraft's behaviour. When disabled, will be falsified by setInvisible() in renderWidget() shortly after.
		render(event.context, event.mouseX, event.mouseY, event.tickDelta)
	}

	override fun renderWidget(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
		if (!shouldDrawInternal()) return setInvisible()
		syncHoverState()

		val color = if (isResetting()) Color.red else Color.gray
		val alpha = if (isHovered) 0.5f else 0.3f

		val size = position.drawPreview(context)

		position.drawBackground(size, context, padding = padding, color = color.withAlpha(alpha)).apply {
			x = minX.toInt()
			y = minY.toInt()
			width = width().toInt()
			height = height().toInt()
		}

		visible = true
	}

	override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
		if (!isDragging()) return false

		position.x += deltaX.toFloat()
		position.y += deltaY.toFloat()
		limitPosition()

		return true
	}

	override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double): Boolean {
		if (!isHovered) return false

		position.scale += verticalAmount.toFloat() / 10
		limitScale()
		save()

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

			GLFW.GLFW_KEY_TAB -> invertCentering()

			GLFW.GLFW_KEY_V -> position.y = getClient().window.scaledHeight / 2f
			GLFW.GLFW_KEY_H -> {
				position.x = getClient().window.scaledWidth / 2f
				position.centered = true
			}

			else -> return false
		}

		limitPosition()
		limitScale()
		save()
		selectedElement = this
		return true
	}

	fun onLeftClick(): Boolean {
		if (!isHovered) return false

		if (lastClicked.timeDelta() <= 0.5.seconds) {
			val configUI = Vice.config.gui() ?: return false
			val category = Vice.config.getCategoryFromSearch(searchTerm)

			MinecraftClient.getInstance().send(Runnable { UScreen.displayScreen(configUI) })
			MinecraftClient.getInstance().send(Runnable { configUI.selectCategory(category) })
		} else lastClicked = System.currentTimeMillis()

		return true
	}

	fun tryResetting() {
		if (isResetting()) {
			position = Position(0f, 0f, scale = 1f, centered = false)
			resettingElement = null
			save()
			return
		}

		resettingElement = this
	}

	fun invertCentering() {
		position.centered = !position.centered
	}

	override fun isHovered(): Boolean = visible && hoveredElement == this

	override fun isSelected(): Boolean = visible && selectedElement == this

	private fun isDragging(): Boolean = visible && draggedElement == this

	private fun isResetting(): Boolean = visible && resettingElement == this

	fun getDisplayName(): String = displayName

	private fun syncHoverState() {
		hoveredElements.removeAll { it.displayName == displayName }

		if (hovered) {
			hoveredElements.add(this)
		}
	}

	private fun setInvisible() {
		if (!visible) return

		hovered = false
		if (isHovered) syncHoverState()
		if (isSelected) selectedElement = null

		visible = false
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