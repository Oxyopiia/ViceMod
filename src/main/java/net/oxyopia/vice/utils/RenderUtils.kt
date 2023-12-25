package net.oxyopia.vice.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.font.TextRenderer.TextLayerType
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.AffineTransformation
import net.minecraft.util.math.Box
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.Vec3d
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.WorldRenderEvent
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

		val i = textRenderer.draw(text, xPos.toFloat(), y.toFloat(), color, shadow, stack.peek().positionMatrix, vertexConsumers, TextLayerType.NORMAL, 0, 0xF000F0, textRenderer.isRightToLeft)
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

	/**
	 * Adapted from SkyHanni under the GNU Lesser General Public License v2.1.
	 *
	 * @link https://github.com/hannibal002/SkyHanni/blob/beta/src/main/java/at/hannibal2/skyhanni/utils/RenderUtils.kt#L691
	 * @link https://github.com/hannibal002/SkyHanni/blob/beta/LICENSE
	 * @author hannibal002
	 */
	fun WorldRenderEvent.draw3DLine(pos1: Vec3d, pos2: Vec3d, color: Color, lineWidth: Float = 3f, depth: Boolean = true) {
		RenderSystem.disableCull()

		val camera: Entity = Vice.client.cameraEntity ?: return
		val tessellator = Tessellator.getInstance()
		val builder = tessellator.buffer

		val realX = camera.lastRenderX + (camera.x - camera.lastRenderX) * tickDelta
		val realY = camera.lastRenderY + (camera.y - camera.lastRenderY) * tickDelta
		val realZ = camera.lastRenderZ + (camera.z - camera.lastRenderZ) * tickDelta

		matrices.push()
		matrices.translate(-realX, -realY, -realZ)

		RenderSystem.enableBlend()
		RenderSystem.blendFuncSeparate(770, 771, 1, 0)
		RenderSystem.lineWidth(lineWidth)

		if (!depth) {
			GL11.glDisable(GL11.GL_DEPTH_TEST)
			RenderSystem.depthMask(false)
		}

		val matrix4f = matrices.peek().positionMatrix

		builder.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION_COLOR)
		builder.vertex(matrix4f, pos1.x.toFloat(), pos1.y.toFloat(), pos1.z.toFloat()).color(color.rgb).next()
		builder.vertex(matrix4f, pos2.x.toFloat(), pos2.y.toFloat(), pos2.z.toFloat()).color(color.rgb).next()
		tessellator.draw()

		matrices.pop()

		if (!depth) {
			GL11.glEnable(GL11.GL_DEPTH_TEST)
			RenderSystem.depthMask(true)
		}

		RenderSystem.disableBlend()
		RenderSystem.enableDepthTest()
	}

	fun WorldRenderEvent.drawString(
			position: Vec3d,
			string: String,
			color: Color,
			size: Float = 0.25f,
			center: Boolean = true,
			offset: Float = -1f,
			visibleThroughObjects: Boolean = true
	) {
		val client = Vice.client
		val camera: Camera = client.gameRenderer.camera
		if (camera.isReady && client.entityRenderDispatcher.gameOptions != null) {
			val textRenderer: TextRenderer = client.textRenderer

			val x = camera.pos.x
			val y = camera.pos.y
			val z = camera.pos.z

			val matrixStack = RenderSystem.getModelViewStack()
			matrixStack.push()
			matrixStack.translate((position.x - x).toFloat().toDouble(), ((position.y - y).toFloat() + 0.07f).toDouble(), (position.z - z).toFloat().toDouble())
			matrixStack.scale(size, -size, size)

			if (visibleThroughObjects) {
				RenderSystem.disableDepthTest()
			} else {
				RenderSystem.enableDepthTest()
			}

			RenderSystem.depthMask(true)
			matrixStack.scale(-1.0f, 1.0f, 1.0f)
			RenderSystem.applyModelViewMatrix()

			var g = if (center) (-textRenderer.getWidth(string)).toFloat() / 2.0f else 0.0f
			g -= offset / size

			val immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().buffer)
			val textLayer = if (visibleThroughObjects) TextLayerType.SEE_THROUGH else TextLayerType.NORMAL

			textRenderer.draw(string, g, 0.0f, color.rgb, false, AffineTransformation.identity().matrix, immediate, textLayer, 0, 15728880)
			immediate.draw()

			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
			RenderSystem.enableDepthTest()
			matrixStack.pop()
			RenderSystem.applyModelViewMatrix()
		}
	}
}