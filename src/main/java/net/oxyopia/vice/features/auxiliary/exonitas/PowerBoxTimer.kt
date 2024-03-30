package net.oxyopia.vice.features.auxiliary.exonitas

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.events.TitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils.drawString
import net.oxyopia.vice.utils.TimeUtils.ms
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.seconds

object PowerBoxTimer : HudElement("Power Box Timer", Vice.storage.auxiliary.city.powerBoxTimerPos) {
	private const val POWER_BOX_SECONDS = 1.75
	private val levelRegex = Regex("LEVEL (\\d*)")

	private var lastPowerBoxActivation = -1L
	private var lastKnownLevel = -1

	@SubscribeEvent
	fun onTitle(event: TitleEvent) {
		if (!World.Exonitas.isInWorld()) return

		if (event.title.contains("GAME OVER")) {
			lastKnownLevel = -1
			return
		}

		levelRegex.find(event.title)?.apply {
			try {
				lastKnownLevel = groupValues[1].toInt()
			} catch (e: Exception) {
				DevUtils.sendErrorMessage(e, "An error occurred parsing Exonitas Level as an Int!")
			}
		}
	}

	@SubscribeEvent
	fun onSound(event: SoundEvent) {
		if (!World.Exonitas.isInWorld()) return
		if (event.soundName != "entity.zombie_villager.converted" || event.pitch != 2f || event.volume != 3f) return

		lastPowerBoxActivation = System.currentTimeMillis()
	}

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!shouldDraw() || !World.Exonitas.isInWorld() || lastKnownLevel < 4) return

		val text = when {
			lastPowerBoxActivation.timeDelta() <= POWER_BOX_SECONDS.seconds.ms() -> String.format("&&a%.2fs", POWER_BOX_SECONDS - lastPowerBoxActivation.timeDelta() / 1000.0)
			else -> "&&cPower Active!"
		}

		position.drawString(text, event.context)
	}

	override fun shouldDraw(): Boolean {
		return Vice.config.EXONITAS_POWER_BOX_TIMER
	}

	override fun storePosition(position: Position) {
		Vice.storage.auxiliary.city.powerBoxTimerPos = position
		Vice.storage.markDirty()
	}

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		return Pair(position.drawString("&&c1.75s", context) * position.scale, 7f)
	}
}