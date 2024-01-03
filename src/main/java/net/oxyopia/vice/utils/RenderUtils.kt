package net.oxyopia.vice.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.font.TextRenderer.TextLayerType
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.util.math.AffineTransformation
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.oxyopia.vice.events.WorldRenderEvent
import org.lwjgl.opengl.GL11
import java.awt.Color

object RenderUtils {
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

		val camera: Entity = MinecraftClient.getInstance().cameraEntity ?: return
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
		val client = MinecraftClient.getInstance()
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