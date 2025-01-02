package net.oxyopia.vice.features.hud

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Brew
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.*
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.hud.FishingBrewTimer.activeBrew
import net.oxyopia.vice.features.hud.FishingBrewTimer.lastBrewActivation
import net.oxyopia.vice.utils.hud.HudUtils.drawString
import net.oxyopia.vice.utils.hud.HudUtils.drawText
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.TimeUtils.timeDeltaWithin
import kotlin.time.Duration.Companion.minutes

object FishingBrewTimer : HudElement(
	"Fishing Brew Timer",
	Vice.storage.misc.fishingBrewTimerPos,
	{ Vice.storage.misc.fishingBrewTimerPos = it },
	enabled = { Vice.config.FISHING_BREW_TIMER },
	drawCondition = { lastBrewActivation.timeDeltaWithin(activeBrew?.duration ?: 0.minutes) }
) {
	private var activeBrew: Brew? = null
	private var lastBrewActivation = -1L

	@SubscribeEvent
	fun onChatMessage(event: ChatEvent) {
		activeBrew = when {
			event.string.startsWith("Fishy Brew Activated") -> Brew.FISHY_BREW
			event.string.startsWith("Fetid Flask Activated") -> Brew.FETID_FLASK
			else -> return
		}

		lastBrewActivation = System.currentTimeMillis()
	}

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!enabled() || !drawCondition()) return

		val brew = activeBrew ?: return
		if (!lastBrewActivation.timeDeltaWithin(brew.duration)) return

		val timeRemaining = brew.duration - lastBrewActivation.timeDelta()
		val formatted = timeRemaining.formatDuration()
		position.drawText(brew.displayName.copy().append(" §a$formatted"), event.context)
	}

	override fun Position.drawPreview(context: DrawContext): Size {
		return position.drawString("§bFishy Brew §a03:43", context)
	}
}