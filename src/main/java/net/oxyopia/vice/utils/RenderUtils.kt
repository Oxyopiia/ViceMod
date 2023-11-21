package net.oxyopia.vice.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Box
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d
import net.oxyopia.vice.Vice
import org.lwjgl.opengl.GL11
import java.awt.Color

object RenderUtils {
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

	fun renderBox(stack: MatrixStack?, buffer: VertexConsumer?, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, color: Color) {
		val aabb = Box(x1, y1, z1, x2, y2, z2)
		WorldRenderer.drawBox(stack, buffer, aabb, color.red.toFloat() / 255, color.green.toFloat() / 255, color.blue.toFloat() / 255, color.alpha.toFloat() / 100)
		DevUtils.sendDebugChat("renderBox x,y,z 1: $x1 $y1 $z1 to $x2 $y2 $z2", "GAME_RENDERER_DEBUGGER")
	}

	fun draw3DLine(stack: MatrixStack, x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float, color: Color, lineWidth: Float) {
		val pos1 = Vec3d(x1.toDouble(), y1.toDouble(), z1.toDouble())
		val pos2 = Vec3d(x2.toDouble(), y2.toDouble(), z2.toDouble())
		draw3DLine(stack, pos1, pos2, color, lineWidth)
	}

	/**
	 * Draw a line between two points.
	 * Adapted from Skyblocker mod under the GNU LESSER GENERAL PUBLIC LICENSE
	 *
	 * does not work yet :(
	 *
	 * @author hysky
	 */
	fun draw3DLine(stack: MatrixStack, point1: Vec3d, point2: Vec3d, color: Color, lineWidth: Float) {
		if (Vice.client.cameraEntity == null) return
		val cameraPos = Vice.client.cameraEntity!!.pos

//		stack.push();
//		stack.translate(-cameraPos.x, cameraPos.y, -cameraPos.z);
		val tessellator = Tessellator.getInstance()
		val buffer = tessellator.buffer
		val matrix3f = stack.peek().normalMatrix
		val matrix4f = stack.peek().positionMatrix
		GL11.glEnable(GL11.GL_LINE_SMOOTH)
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST)
		RenderSystem.setShader { GameRenderer.getRenderTypeLinesProgram() }
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
		RenderSystem.lineWidth(lineWidth)
		RenderSystem.enableBlend()
		RenderSystem.defaultBlendFunc()
		RenderSystem.disableCull()
		RenderSystem.enableDepthTest()
		buffer.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.LINES)
		val normal = point2.subtract(point1).normalize()
		buffer.vertex(matrix4f, point1.x.toFloat(), point1.y.toFloat(), point1.z.toFloat()).color(color.rgb).normal(matrix3f, normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat()).next()
		buffer.vertex(matrix4f, point2.x.toFloat(), point2.y.toFloat(), point2.z.toFloat()).color(color.rgb).normal(matrix3f, normal.x.toFloat(), normal.y.toFloat(), normal.z.toFloat()).next()
		tessellator.draw()
		//		stack.pop();
		GL11.glDisable(GL11.GL_LINE_SMOOTH)
		RenderSystem.lineWidth(1f)
		RenderSystem.enableCull()
	}
}