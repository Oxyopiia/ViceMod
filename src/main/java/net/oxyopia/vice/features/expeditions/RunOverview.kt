package net.oxyopia.vice.features.expeditions

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ExpeditionEndEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.TimeUtils
import net.oxyopia.vice.utils.Utils

object RunOverview {
	@SubscribeEvent
	fun onExpeditionEnd(event: ExpeditionEndEvent) {
		if (!Vice.config.EXPEDITION_RUN_OVERVIEW) return
		val session = event.session

		val formattedCompTime = TimeUtils.formatDuration(event.completionTime, showMs = true)
		var timeMessage = "&&7Expedition took &&e$formattedCompTime&&7. "

		if (event.isNewPB) {
			timeMessage += "&&d&&lPERSONAL BEST!"
		} else {
			val pbFormatted = TimeUtils.formatDuration(Vice.storage.expeditions.easter.personalBestTime, true)
			timeMessage += "&&7(best: $pbFormatted)"
		}

		Utils.sendViceMessage(timeMessage)
		Utils.sendViceMessage("&&7Credits Earned: &&6◎ ${session.totalCredits}")
		Utils.sendViceMessage("&&7Barrels Opened: &&a⚁ ${session.barrelsCollected}")
	}
}