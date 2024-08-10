package net.oxyopia.vice.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.math.ColorHelper
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.data.gui.Quad
import net.oxyopia.vice.events.ContainerRenderSlotEvent
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.Utils.convertFormatting
import java.awt.Color

object HudUtils {
	fun String.toText(color: Color = Color.white, bold: Boolean = false, italic: Boolean = false, underline: Boolean = false, obfuscated: Boolean = false, strikethrough: Boolean = false): MutableText {
		val text = Text.literal(this)
		return text.setStyle(Style.EMPTY
			.withColor(color.rgb)
			.withBold(bold)
			.withItalic(italic)
			.withUnderline(underline)
			.withObfuscated(obfuscated)
			.withStrikethrough(strikethrough)
		)
	}

	fun fillUIArea(stack: MatrixStack, layer: RenderLayer, x1: Int, y1: Int, x2: Int, y2: Int, z: Int, color: Color) {
		fillUIArea(stack, layer, x1, y1, x2, y2, z, color.rgb)
	}

	private fun fillUIArea(stack: MatrixStack, layer: RenderLayer, x1: Float, y1: Float, x2: Float, y2: Float, color: Color, z: Float = 0f) {
		fillUIArea(stack, layer, x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt(), z.toInt(), color.rgb)
	}

	fun fillUIArea(stack: MatrixStack, layer: RenderLayer, x1: Int, y1: Int, x2: Int, y2: Int, z: Int, color: Int) {
		var ax = x1
		var ay = y1
		var bx = x2
		var by = y2
		var i: Int
		val matrix4f = stack.peek().positionMatrix

		if (ax < bx) {
			i = ax
			ax = bx
			bx = i
		}
		if (ay < by) {
			i = ay
			ay = by
			by = i
		}

		val f = ColorHelper.Argb.getAlpha(color).toFloat() / 255.0f
		val g = ColorHelper.Argb.getRed(color).toFloat() / 255.0f
		val h = ColorHelper.Argb.getGreen(color).toFloat() / 255.0f
		val j = ColorHelper.Argb.getBlue(color).toFloat() / 255.0f

		val vertexConsumers = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers
		val vertexConsumer = vertexConsumers.getBuffer(layer)
		vertexConsumer.vertex(matrix4f, ax.toFloat(), ay.toFloat(), z.toFloat()).color(g, h, j, f).next()
		vertexConsumer.vertex(matrix4f, ax.toFloat(), by.toFloat(), z.toFloat()).color(g, h, j, f).next()
		vertexConsumer.vertex(matrix4f, bx.toFloat(), by.toFloat(), z.toFloat()).color(g, h, j, f).next()
		vertexConsumer.vertex(matrix4f, bx.toFloat(), ay.toFloat(), z.toFloat()).color(g, h, j, f).next()

		RenderSystem.disableDepthTest()
		vertexConsumers.draw(layer)
		RenderSystem.enableDepthTest()
	}

	fun ContainerRenderSlotEvent.highlight(color: Color) {
		val x = slot.x
		val y = slot.y

		fillUIArea(context.matrices, RenderLayer.getGuiOverlay(), x, y, x + 16, y + 16, -500, color)
	}

	fun drawText(text: Text, x: Int, y: Int, context: DrawContext, color: Color, shadow: Boolean = Vice.config.HUD_TEXT_SHADOW, centered: Boolean = false): Int {
		val textRenderer = MinecraftClient.getInstance().textRenderer
		val vertexConsumers = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers
		var xPos = x

		if (centered) {
			val width = textRenderer.getWidth(text)
			xPos = x - (width / 2)
		}

		val width = textRenderer.draw(text, xPos.toFloat(), y.toFloat(), color.rgb, shadow, context.matrices.peek().positionMatrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0)
		RenderSystem.disableDepthTest()
		vertexConsumers.draw()
		RenderSystem.enableDepthTest()

		return width
	}

	fun drawText(text: String, x: Int, y: Int, context: DrawContext, color: Int = Color(255, 255, 255, 255).rgb, shadow: Boolean = Vice.config.HUD_TEXT_SHADOW, centered: Boolean = false): Int {
		return drawText(Text.of(text.convertFormatting()), x, y, context, Color(color), shadow, centered)
	}

	fun Position.drawBackground(size: Pair<Float, Float>, context: DrawContext, color: Color = Color.gray, padding: Float = 0f): Quad {
		if (size.first <= 0 || size.second <= 0) return Quad(0f, 0f, 0f, 0f)

		var width = size.first + padding
		val height = size.second + padding

		val pureX = x - (if (centered) (width / 2f) else 0f)
		if (centered) width /= 2f

		return Quad(pureX, y, x + width, y + height)
			.addPadding(padding)
			.apply { fillUIArea(context.matrices, RenderLayer.getGuiOverlay(), minX, minY, maxX, maxY, color) }
	}

	fun Position.drawText(text: Text, context: DrawContext, offsetX: Float = 0f, offsetY: Float =0f, defaultColor: Color = Color.white, z: Int = 0): Int {
		val matrices = context.matrices
		val consumers = context.vertexConsumers
		val textRenderer = MinecraftClient.getInstance().textRenderer

		matrices.push()
		matrices.translate(x, y, 0f)

		if (centered) {
			matrices.translate(-textRenderer.getWidth(text) / 2f * scale, 0f, 0f)
		}

		matrices.translate(offsetX, offsetY, z.toFloat())
		matrices.scale(scale, scale, 1f)

		val width = textRenderer.draw(text, 0f, 0f, defaultColor.rgb, Vice.config.HUD_TEXT_SHADOW, matrices.peek().positionMatrix, consumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0)

		matrices.pop()

		return width
	}

	fun Position.drawString(text: String, context: DrawContext, offsetX: Float = 0f, offsetY: Float = 0f, defaultColor: Color = Color.white, z: Int = 0): Int {
		return drawText(Text.of(text.convertFormatting()), context, offsetX, offsetY, defaultColor, z)
	}

	fun Position.drawTexts(list: List<Text>, context: DrawContext, z: Int = 0, gap: Float = 10f): Pair<Float, Float> {
		var maxWidth = 0

		list.forEachIndexed { index, text ->
			maxWidth = drawText(text, context, offsetY = index * gap * scale).coerceAtLeast(maxWidth)
		}

		// Subtract 3 for final line not having a gap to account for
		return Pair(maxWidth * scale, list.size * gap * scale - 3)
	}

	/**
	 * @return Width and Height as an Int Pair respectively
	 */
	fun Position.drawStrings(list: List<String>, context: DrawContext, z: Int = 0, gap: Float = 10f): Pair<Float, Float> {
		val textList = list.map { str -> Text.of(str.convertFormatting()) }
		return drawTexts(textList, context, z, gap)
	}

	fun Position.getMultilineSize(list: List<String>, gap: Int = 10): Pair<Float, Float> {
		val textRenderer = MinecraftClient.getInstance().textRenderer
		var maxWidth = 0

		list.forEach { text ->
			maxWidth = textRenderer.getSpecialTextWidth(text.convertFormatting()).coerceAtLeast(maxWidth)
		}

		return Pair(maxWidth.toFloat(), list.size * gap * scale - 3)
	}

	fun Position.getMultilineHeight(rows: Int, gap: Int = 10): Float {
		return rows * gap * scale - 3
	}

	fun TextRenderer.getSpecialTextWidth(text: String, shadow: Boolean = Vice.config.HUD_TEXT_SHADOW): Int {
		return this.getWidth(text) + if (shadow) 1 else 0
	}

	fun sendVanillaTitle(title: String, subtitle: String, stayTime: Float = 1f, fadeinout: Float = 0.25f) {
		val client = MinecraftClient.getInstance()

		client.inGameHud.setSubtitle(Text.of(subtitle.convertFormatting()))
		client.inGameHud.setTitle(Text.of(title.convertFormatting()))
		client.inGameHud.setTitleTicks((20 * fadeinout).toInt(), (20 * stayTime).toInt(), (20 * fadeinout).toInt())
	}

	fun sendVanillaTitle(title: String, subtitle: String) {
		val client = MinecraftClient.getInstance()

		client.inGameHud.setTitle(Text.of(title.convertFormatting()))
		client.inGameHud.setSubtitle(Text.of(subtitle.convertFormatting()))
	}

	fun sendVanillaActionBar(message: Text) = MinecraftClient.getInstance().inGameHud.setOverlayMessage(message, false)

	private var title = MutableText.EMPTY
	private var titleStayTicks = 0

	fun sendViceTitle(title: String, stayTime: Float = 1f) {
		this.title = Text.of(title.convertFormatting())
		this.titleStayTicks = (20 * stayTime).toInt()
	}

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (titleStayTicks <= 0) return

		val pos = Position(event.scaledWidth / 2f, event.scaledHeight / 2f, scale = 3f)
		pos.drawString(title.string, event.context, offsetY = -20f)
	}

	@SubscribeEvent
	fun tick(event: ClientTickEvent) {
		if (titleStayTicks > 0) {
			titleStayTicks--
		}
	}
}
