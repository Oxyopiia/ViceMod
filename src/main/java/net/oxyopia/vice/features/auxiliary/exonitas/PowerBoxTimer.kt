package net.oxyopia.vice.features.auxiliary.exonitas

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.events.TitleEvent
import net.oxyopia.vice.events.WorldChangeEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils.drawString
import net.oxyopia.vice.utils.TimeUtils.formatShortDuration
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.seconds

object PowerBoxTimer : HudElement("Power Box Timer", Vice.storage.auxiliary.city.powerBoxTimerPos) {
	private val POWER_BOX_TIMER = 1.75.seconds
	private val levelRegex = Regex("LEVEL (\\d*)")

	private var lastPowerBoxActivation = -1L
	private var lastKnownLevel = -1

	override fun shouldDraw(): Boolean = Vice.config.EXONITAS_POWER_BOX_TIMER
	override fun drawCondition(): Boolean = World.Exonitas.isInWorld()

	@SubscribeEvent
	fun onTitle(event: TitleEvent) {
		if (!drawCondition()) return

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
	fun onWorldChange(event: WorldChangeEvent) {
		if (lastKnownLevel >= 4) lastKnownLevel = -1
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
			lastPowerBoxActivation.timeDelta() <= POWER_BOX_TIMER -> (POWER_BOX_TIMER - lastPowerBoxActivation.timeDelta()).formatShortDuration()
			else -> "&&cPower Active!"
	}

		position.drawString(text, event.context)
	}

	override fun storePosition(position: Position) {
		Vice.storage.auxiliary.city.powerBoxTimerPos = position
		Vice.storage.markDirty()
	}

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		return Pair(position.drawString("&&a1.5s", context) * position.scale, 7f)
	}
}