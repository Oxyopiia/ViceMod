package net.oxyopia.vice.features.arenas

import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Debugger
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ArenaStartEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object ArenaAPI {
	fun getUniqueDropName(world: World): String {
		return when (world) {
			World.Floor2Arena -> "Galactic Hand Cannon"
			World.Floor3Arena -> "Arctic Scroll"
			World.Floor4Arena -> "Poseidon's Fury"
			else -> "Unique Item"
		}
	}

	@SubscribeEvent
	fun onArenaStart(event: ArenaStartEvent) {
		Vice.storage.arenas.startTimes[event.world.id] = event.timestamp
		Vice.storage.markDirty()

		Debugger.ARENA.debug("Setting timestamp for ${event.world} to ${event.timestamp}")
	}
}