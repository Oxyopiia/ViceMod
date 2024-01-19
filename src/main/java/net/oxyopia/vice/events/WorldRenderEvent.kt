package net.oxyopia.vice.events

import net.minecraft.client.util.math.MatrixStack

class WorldRenderEvent(val tickDelta: Float, val limitTime: Long, val matrices: MatrixStack) : ViceEvent()