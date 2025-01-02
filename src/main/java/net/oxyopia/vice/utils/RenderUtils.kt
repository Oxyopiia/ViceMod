package net.oxyopia.vice.utils

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer.TextLayerType
import net.minecraft.client.render.*
import net.minecraft.client.util.BufferAllocator
import net.minecraft.util.math.Vec3d
import net.oxyopia.vice.events.WorldRenderEvent
import net.oxyopia.vice.utils.hud.HudUtils.getSpecialTextWidth
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import java.awt.Color


object RenderUtils {
	val allocator = BufferAllocator(1536)

	/**
	 * Adapted from Skyblocker under the GNU Lesser General Public License v3.0.
	 *
	 * @link https://github.com/SkyblockerMod/Skyblocker/blob/master/src/main/java/de/hysky/skyblocker/utils/render/RenderHelper.java#L150
	 * @link https://github.com/SkyblockerMod/Skyblocker/blob/master/LICENSE
	 * @author Skyblocker Mod
	 * @author Oxyopiia
	 */
	fun WorldRenderEvent.draw3DLine(pos1: Vec3d, pos2: Vec3d, color: Color, lineWidth: Float = 3f, visibleThroughObjects: Boolean = true) {
		val tessellator = Tessellator.getInstance()

		matrices.push()
		matrices.translate(-camera.pos.x, -camera.pos.y, -camera.pos.z)
		matrices.peek().positionMatrix.mul(RenderSystem.getModelViewMatrix())
		matrices.multiply(camera.rotation)

		GL11.glEnable(GL11.GL_LINE_SMOOTH)
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST)

		RenderSystem.setShader { GameRenderer.getRenderTypeLinesProgram() }
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
		RenderSystem.lineWidth(lineWidth)
		RenderSystem.enableBlend()
		RenderSystem.defaultBlendFunc()
		RenderSystem.disableCull()
		RenderSystem.enableDepthTest()
		RenderSystem.depthFunc(if (visibleThroughObjects) GL11.GL_ALWAYS else GL11.GL_LEQUAL)

		val matrix3f = matrices.peek().normalMatrix
		val normalVec = Vector3f(pos2.x.toFloat(), pos2.y.toFloat(), pos2.z.toFloat())
			.sub(pos1.x.toFloat(), pos1.y.toFloat(), pos1.z.toFloat())
			.normalize()
			.mul(matrix3f)

		val matrix4f = matrices.peek().positionMatrix

		val builder = tessellator.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.POSITION_COLOR)
		builder
			.vertex(matrix4f, pos1.x.toFloat(), pos1.y.toFloat(), pos1.z.toFloat())
			.color(color.rgb)
			.normal(normalVec.x, normalVec.y, normalVec.z)

		builder
			.vertex(matrix4f, pos2.x.toFloat(), pos2.y.toFloat(), pos2.z.toFloat())
			.color(color.rgb)
			.normal(normalVec.x, normalVec.y, normalVec.z)

		BufferRenderer.drawWithGlobalProgram(builder.end())

		matrices.pop()
		GL11.glDisable(GL11.GL_LINE_SMOOTH)
		RenderSystem.lineWidth(1f)
		RenderSystem.enableCull()
		RenderSystem.depthFunc(GL11.GL_LEQUAL)
	}

	/**
	 * Adapted from Skyblocker under the GNU Lesser General Public License v3.0.
	 *
	 * @link https://github.com/SkyblockerMod/Skyblocker/blob/master/src/main/java/de/hysky/skyblocker/utils/render/RenderHelper.java#L292
	 * @link https://github.com/SkyblockerMod/Skyblocker/blob/master/LICENSE
	 * @author Skyblocker Mod
	 * @author Oxyopiia
	 */
	fun WorldRenderEvent.drawString(
		position: Vec3d,
		string: String,
		color: Color = Color.white,
		size: Float = 1f,
		yOffset: Float = 0f,
		center: Boolean = true,
		shadow: Boolean = false,
		visibleThroughObjects: Boolean = true
	) {
		val textRenderer = MinecraftClient.getInstance().textRenderer
		val positionMatrix = Matrix4f()
		val scale = size * 0.025f

		positionMatrix
			.translate(
				(position.x - camera.pos.x).toFloat(),
				(position.y - camera.pos.y).toFloat(),
				(position.z - camera.pos.z).toFloat()
			)
			.rotate(camera.rotation)
			.scale(scale, -scale, scale)

		val consumer = VertexConsumerProvider.immediate(allocator)
		val xOffset: Float = if (center) -textRenderer.getSpecialTextWidth(string) / 2f else 0f

		RenderSystem.depthFunc(if (visibleThroughObjects) GL11.GL_ALWAYS else GL11.GL_LEQUAL)

		textRenderer.draw(
			string,
			xOffset,
			yOffset,
			color.rgb,
			shadow,
			positionMatrix,
			consumer,
			TextLayerType.SEE_THROUGH,
			0,
			LightmapTextureManager.MAX_LIGHT_COORDINATE
		)
		consumer.draw()

		RenderSystem.depthFunc(GL11.GL_LEQUAL)
	}
}