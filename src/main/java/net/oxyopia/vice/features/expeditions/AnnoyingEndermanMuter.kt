package net.oxyopia.vice.features.expeditions

import net.oxyopia.vice.events.ModifySoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object AnnoyingEndermanMuter {
	@SubscribeEvent
	fun onSound(event: ModifySoundEvent) {
		event.cancel()
	}
}