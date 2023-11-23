package net.oxyopia.vice.events

import net.minecraft.client.gui.DrawContext

class RenderInGameHudEvent(val context: DrawContext, val scaledWidth: Int, val scaledHeight: Int) : BaseEvent()