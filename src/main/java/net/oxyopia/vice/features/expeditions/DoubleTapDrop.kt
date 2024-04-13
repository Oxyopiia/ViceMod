package net.oxyopia.vice.features.expeditions

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ItemDropEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.expeditions.ExpeditionRarity.Companion.getExpeditionRarity
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.TimeUtils.ms
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.Utils
import kotlin.time.Duration.Companion.seconds

object DoubleTapDrop {
	private var lastItemDropped = ""
	private var lastDropAttempt = -1L

	@SubscribeEvent
	fun onDrop(event: ItemDropEvent) {
		if (!Vice.config.EPIC_EXPEDITION_ITEM_PROTECTION) return

		val rarity = event.item.getExpeditionRarity() ?: return
		if (rarity < ExpeditionRarity.EPIC) return

		val name = event.item.cleanName()
		if (name != lastItemDropped || lastDropAttempt.timeDelta() > 3.seconds.ms()) {
			lastItemDropped = name
			lastDropAttempt = System.currentTimeMillis()
			Utils.sendViceMessage("&&cStopped you from dropping that item! Drop it again to actually drop it.")
			event.cancel()
		}
	}
}