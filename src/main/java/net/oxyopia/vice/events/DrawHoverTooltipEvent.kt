package net.oxyopia.vice.events

import net.minecraft.text.Text

class DrawHoverTooltipEvent(val tooltip: List<Text>) : ViceEvent.Cancelable<List<Text>>()