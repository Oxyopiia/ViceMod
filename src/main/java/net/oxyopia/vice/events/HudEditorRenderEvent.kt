package net.oxyopia.vice.events

import net.minecraft.client.gui.DrawContext

class HudEditorRenderEvent(
	val context: DrawContext,
	val mouseX: Int,
	val mouseY: Int,
	val tickDelta: Float
) : ViceEvent()