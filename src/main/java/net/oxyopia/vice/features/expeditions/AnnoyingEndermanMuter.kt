package net.oxyopia.vice.features.expeditions

import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ModifySoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object AnnoyingEndermanMuter {
	@SubscribeEvent
	fun onSound(event: ModifySoundEvent) {
		if (!World.Expeditions.isInWorld() || event.soundName != "entity.enderman.scream" || event.pitch != 1.8f) return
		event.cancel()
	}
}