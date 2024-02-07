package net.oxyopia.vice.features.hud

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats
import net.minecraft.util.Identifier
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import org.joml.Matrix4f

object GamingMode {
	data class ImageData(val path: String, val width: Int, val height: Int, val scale: Float = 0.05f)
	private val Z_LAYER: Float = 1000f

	@SubscribeEvent
	fun onRenderInGameHud(event: HudRenderEvent) {
		val data: ImageData = when (Vice.config.DEV_GAMING_MODE) {
			1 -> ImageData("icon.png", 500, 500, 0.12f)
			2 -> ImageData("doomtowersisbloodybrilliant.png", 800, 600, 0.15f)
			3 -> ImageData("msmdude.png", 1125, 1433, 0.08f)
			4 -> ImageData("digiheart.png", 246, 85, 0.7f)
			5 -> ImageData("trumpet.png", 828, 817, 0.07f)
			6 -> ImageData("clive.png", 311, 528, 0.15f)
			7 -> ImageData("blackman.png", 581, 690, 0.09f)
			else -> return
		}

		val positionMatrix: Matrix4f = event.context.matrices.peek().positionMatrix
		val tessellator = Tessellator.getInstance()
		val buffer = tessellator.buffer

		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE)
		buffer.vertex(positionMatrix, 0f, 0f, Z_LAYER).color(1f, 1f, 1f, 1f).texture(0f, 0f).next()
		buffer.vertex(positionMatrix, 0f, data.height * data.scale, Z_LAYER).color(1f, 1f, 1f, 1f).texture(0f, 1f).next()
		buffer.vertex(positionMatrix, data.width * data.scale, data.height * data.scale, Z_LAYER).color(1f, 1f, 1f, 1f).texture(1f, 1f).next()
		buffer.vertex(positionMatrix, data.width * data.scale, 0f, Z_LAYER).color(1f, 1f, 1f, 1f).texture(1f, 0f).next()

		RenderSystem.setShader { GameRenderer.getPositionColorTexProgram() }

		RenderSystem.setShaderTexture(0, Identifier("vice", data.path))

		RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
		tessellator.draw()
	}
}

