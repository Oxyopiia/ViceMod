package net.oxyopia.vice.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.math.ColorHelper
import net.oxyopia.vice.Vice
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

		val vertexConsumers = Vice.client.bufferBuilders.entityVertexConsumers
		val vertexConsumer = vertexConsumers.getBuffer(layer)
		vertexConsumer.vertex(matrix4f, ax.toFloat(), ay.toFloat(), z.toFloat()).color(g, h, j, f).next()
		vertexConsumer.vertex(matrix4f, ax.toFloat(), by.toFloat(), z.toFloat()).color(g, h, j, f).next()
		vertexConsumer.vertex(matrix4f, bx.toFloat(), by.toFloat(), z.toFloat()).color(g, h, j, f).next()
		vertexConsumer.vertex(matrix4f, bx.toFloat(), ay.toFloat(), z.toFloat()).color(g, h, j, f).next()

		RenderSystem.disableDepthTest()
		vertexConsumers.draw(layer)
		RenderSystem.enableDepthTest()
	}

	fun drawText(stack: MatrixStack, textRenderer: TextRenderer, text: String?, x: Int, y: Int, color: Int, shadow: Boolean, centered: Boolean): Int {
		if (text == null) {
			return 0
		}

		var xPos = x
		val vertexConsumers = Vice.client.bufferBuilders.entityVertexConsumers

		if (centered) {
			val width = textRenderer.getWidth(text) + if (shadow) 1 else 0
			xPos -= (width / 2).toDouble().toInt()
		}

		val i = textRenderer.draw(text, xPos.toFloat(), y.toFloat(), color, shadow, stack.peek().positionMatrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0, textRenderer.isRightToLeft)
		RenderSystem.disableDepthTest()
		vertexConsumers.draw()
		RenderSystem.enableDepthTest()
		return i
	}

	fun sendVanillaTitle(title: String, subtitle: String, stayTime: Float = 1f, fadeinout: Float = 0.25f) {
		Vice.client.inGameHud.setSubtitle(Text.of(subtitle.replace("&&", "§")))
		Vice.client.inGameHud.setTitle(Text.of(title.replace("&&", "§")))
		Vice.client.inGameHud.setTitleTicks((20 * fadeinout).toInt(), (20 * stayTime).toInt(), (20 * fadeinout).toInt())
	}
}