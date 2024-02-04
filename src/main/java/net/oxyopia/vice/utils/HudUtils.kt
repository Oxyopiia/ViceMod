package net.oxyopia.vice.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.math.ColorHelper
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Position
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.Utils.convertFormatting
import java.awt.Color

object HudUtils {
	fun fillUIArea(stack: MatrixStack, layer: RenderLayer?, x1: Int, y1: Int, x2: Int, y2: Int, z: Int, color: Color) {
		fillUIArea(stack, layer, x1, y1, x2, y2, z, color.rgb)
	}

	fun fillUIArea(stack: MatrixStack, layer: RenderLayer?, x1: Int, y1: Int, x2: Int, y2: Int, z: Int, color: Int) {
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

	fun drawText(stack: MatrixStack, textRenderer: TextRenderer, text: String?, x: Int, y: Int, color: Int = Color.white.rgb, shadow: Boolean = Vice.config.HUD_TEXT_SHADOW, centered: Boolean = false): Int {
		if (text == null) {
			return 0
		}

		var xPos = x
		val vertexConsumers = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers

		if (centered) {
			val width = textRenderer.getSpecialTextWidth(text)
			xPos = x - (width / 2)
		}

		val i = textRenderer.draw(text.convertFormatting(), xPos.toFloat(), y.toFloat(), color, shadow, stack.peek().positionMatrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0, textRenderer.isRightToLeft)
		RenderSystem.disableDepthTest()
		vertexConsumers.draw()
		RenderSystem.enableDepthTest()
		return i
	}

	fun Position.drawString(text: String, context: DrawContext, offsetX: Float = 0f, offsetY: Float = 0f, defaultColor: Color = Color.white) {
		val matrices = context.matrices
		val consumers = context.vertexConsumers
		val textRenderer = MinecraftClient.getInstance().textRenderer

		val display = text.convertFormatting()

		matrices.push()

		matrices.translate(x, y, 0f)
		if (centered) {
			matrices.translate(-textRenderer.getSpecialTextWidth(text) / 2f * scale, 0f, 0f)
		}

		matrices.translate(offsetX, offsetY, 0f)
		matrices.scale(scale, scale, 1f)

		textRenderer.draw(display, 0f, 0f, defaultColor.rgb, Vice.config.HUD_TEXT_SHADOW, matrices.peek().positionMatrix, consumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0)

		matrices.pop()
	}

	fun Position.drawStrings(list: List<String>, context: DrawContext, gap: Float = 10f) {
		list.forEachIndexed { index, text ->
			drawString(text, context, offsetY = index * gap * scale)
		}
	}

	private fun TextRenderer.getSpecialTextWidth(text: String, shadow: Boolean = Vice.config.HUD_TEXT_SHADOW): Int {
		return this.getWidth(text.replace(Regex("&&[a-zA-Z0-9]"), "").replace("§", "")) + if (shadow) 1 else 0
	}

	fun sendVanillaTitle(title: String, subtitle: String, stayTime: Float = 1f, fadeinout: Float = 0.25f) {
		val client = MinecraftClient.getInstance()

		client.inGameHud.setSubtitle(Text.of(subtitle.convertFormatting()))
		client.inGameHud.setTitle(Text.of(title.convertFormatting()))
		client.inGameHud.setTitleTicks((20 * fadeinout).toInt(), (20 * stayTime).toInt(), (20 * fadeinout).toInt())
	}




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
