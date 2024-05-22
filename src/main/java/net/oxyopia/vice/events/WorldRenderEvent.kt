package net.oxyopia.vice.events

import net.minecraft.client.render.Camera
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import org.joml.Matrix4f

// matrices, tickDelta, camera, gameRenderer, projectionMatrix)
open class WorldRenderEvent(val matrices: MatrixStack, val tickDelta: Float, val camera: Camera, val gameRenderer: GameRenderer, val projectionMatrix: Matrix4f) : ViceEvent() {
	class AfterBlocks(matrices: MatrixStack, tickDelta: Float, camera: Camera, gameRenderer: GameRenderer, projectionMatrix: Matrix4f) : WorldRenderEvent(matrices, tickDelta, camera, gameRenderer, projectionMatrix)
	class Last(matrices: MatrixStack, tickDelta: Float, camera: Camera, gameRenderer: GameRenderer, projectionMatrix: Matrix4f) : WorldRenderEvent(matrices, tickDelta, camera, gameRenderer, projectionMatrix)
}