package net.oxyopia.vice.events

import net.minecraft.client.render.Camera
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.util.math.MatrixStack

open class WorldRenderEvent(
	val matrices: MatrixStack,
	val tickCounter: RenderTickCounter,
	val camera: Camera,
	val gameRenderer: GameRenderer,
	val vertexConsumers: VertexConsumerProvider.Immediate
) : ViceEvent()