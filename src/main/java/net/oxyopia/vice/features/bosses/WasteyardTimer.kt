package net.oxyopia.vice.features.bosses

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.bosses.WasteyardTimer.startTime
import net.oxyopia.vice.utils.ChatUtils
import net.oxyopia.vice.utils.HudUtils.drawString
import net.oxyopia.vice.utils.SoundUtils
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.TimeUtils.timeDeltaWithin
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object WasteyardTimer : HudElement(
	"Wasteyard Timer",
	Vice.storage.bosses.wasteyardTimerPos,
	{ Vice.storage.bosses.wasteyardTimerPos = it },
	enabled = { Vice.config.WASTEYARD_TIMER },
	drawCondition = { startTime.timeDeltaWithin(5.minutes) }
) {
	private const val COOLDOWN_TIME_SECS = 90
	private var startTime = -1L
	private var lastNotification = -1L

	@SubscribeEvent
	fun onSound(event: ChatEvent) {
		if (!World.Wasteyard.isInWorld()) return
		if (!event.string.startsWith("Upon entering the Wasteyard")) return

		startTime = System.currentTimeMillis()
	}

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!canDraw()) return

		if (startTime.timeDeltaWithin(COOLDOWN_TIME_SECS.seconds)) {
			val text = startTime.timeDelta().formatTimer(COOLDOWN_TIME_SECS.seconds)
			position.drawString("&&4Wasteyard&&c$text", event.context)

		} else {
			position.drawString("&&4Wasteyard &&aREADY", event.context)
		}
	}

	@SubscribeEvent
	fun onTimedTick(event: ClientTickEvent) {
		if (!event.repeatSeconds(1) || !Vice.config.WASTEYARD_TIMER) return
		if (lastNotification >= startTime || startTime.timeDeltaWithin(COOLDOWN_TIME_SECS.seconds)) return

		ChatUtils.sendViceMessage("&&aYour Wasteyard cooldown has worn off.")
		SoundUtils.playSound(Vice.config.WASTEYARD_TIMER_SOUND, pitch = Vice.config.WASTEYARD_TIMER_PITCH)
		lastNotification = System.currentTimeMillis()
	}

	override fun Position.drawPreview(context: DrawContext): Size {
		return position.drawString("&&cWasteyard &&aREADY", context)
	}
}