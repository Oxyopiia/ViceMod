package net.oxyopia.vice.features.arenas

import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ArenaStartEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils

object ArenaAPI {
	var storedArenaTimestamps = HashMap<World, Long>()
		private set

	@SubscribeEvent
	fun onArenaStart(event: ArenaStartEvent) {
		storedArenaTimestamps[event.world] = event.timestamp
		DevUtils.sendDebugChat("&&dARENAS (API) &&rSetting timestamp for ${event.world} to ${event.timestamp}", "ARENAS_DEBUGGER")
	}
}