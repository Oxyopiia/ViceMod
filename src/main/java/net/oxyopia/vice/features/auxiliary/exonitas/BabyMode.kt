package net.oxyopia.vice.features.auxiliary.exonitas

import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.EntityIsChildEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object BabyMode {
	@SubscribeEvent
	fun isBaby(event: EntityIsChildEvent) {
		if (!Vice.config.EXONITAS_BABY_MODE || !World.Exonitas.isInWorld()) return

		event.setReturnValue(true)
	}
}