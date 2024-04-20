package net.oxyopia.vice.features.expeditions

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.RenderItemSlotEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.ItemUtils.getLore
import java.awt.Color

object DefibCounter {
	private val usesRegex = Regex("Uses: (\\d+)/(\\d+)")

	@SubscribeEvent
	fun onRenderItemSlot(event: RenderItemSlotEvent) {
		if (!Vice.config.DEFIB_COUNTER) return
		if (!event.itemStack.cleanName().contains("Defibrillator")) return

		val matrices = event.context.matrices

		matrices.push()
		matrices.translate(0.0f, 0.0f, 200.0f)

		val firstLine = event.itemStack.getLore().firstOrNull() ?: return

		usesRegex.find(firstLine)?.apply {
			val timesUsed = groupValues[1].toIntOrNull() ?: return
			val max = groupValues[2].toIntOrNull() ?: return

			val remaining = max - timesUsed

			val color = when {
				remaining >= 6 -> Color(0x55FF55)
				remaining >= 3 -> Color(0xFFFF55)
				else -> Color(0xFF5555)
			}

			HudUtils.drawText(remaining.toString(), event.x, event.y + 9, event.context, color = color.rgb, shadow = true)
		}

		matrices.pop()
	}
}