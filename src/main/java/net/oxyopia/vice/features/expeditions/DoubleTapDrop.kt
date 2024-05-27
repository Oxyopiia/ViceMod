package net.oxyopia.vice.features.expeditions

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ItemDropEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.expeditions.ExpeditionRarity.Companion.getExpeditionRarity
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.TimeUtils.timeDeltaDuration
import net.oxyopia.vice.utils.Utils
import kotlin.time.Duration.Companion.seconds

object DoubleTapDrop {
	private var lastItemDropped = ""
	private var lastDropAttempt = -1L

	@SubscribeEvent
	fun onDrop(event: ItemDropEvent) {
		if (!Vice.config.EXPEDITION_ITEM_PROTECTION || !ExpeditionAPI.isInExpedition()) return

		val rarity = event.item.getExpeditionRarity() ?: return
		val protectionThreshold = ExpeditionRarity.fromOrdinal(Vice.config.EXPEDITION_ITEM_PROTECTION_THRESHOLD)
		if (rarity < protectionThreshold) return

		val name = event.item.cleanName()
		if (name != lastItemDropped || lastDropAttempt.timeDeltaDuration() > 3.seconds) {
			lastItemDropped = name
			lastDropAttempt = System.currentTimeMillis()
			Utils.sendViceMessage("&&cStopped you from dropping that item as it is ${protectionThreshold.cleanText} or higher! Drop it again to actually drop it.")
			Utils.playFail()
			event.cancel()
		}
	}
}