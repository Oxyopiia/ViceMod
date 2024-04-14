package net.oxyopia.vice.features.expeditions

import net.minecraft.entity.player.PlayerEntity
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.EntitySpawnEvent
import net.oxyopia.vice.events.ExpeditionEndEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.Utils

object ExpeditionAPI {
	private var currentSession: ExpeditionRun? = null
	private var trackedPlayers = mutableListOf<String>()

	@SubscribeEvent
	fun onPlayerLoad(event: EntitySpawnEvent) {
		if (Utils.getDTWorld() != World.Expeditions) return
		if (event.entity !is PlayerEntity) return

		trackedPlayers.add(event.entity.name.string)
		DevUtils.sendDebugChat("&&aEXPEDITIONS &&fDetected player &&7${event.entity.name.string} &&fand added to list.", "EXPEDITION_DEBUGGER")
	}

	@SubscribeEvent
	fun onChat(event: ChatEvent) {
		if (event.string.contains("Right click on the door to begin the Expedition.")) {
			DevUtils.sendDebugChat("&&aEXPEDITIONS &&fStarted.", "EXPEDITION_DEBUGGER")
			currentSession = ExpeditionRun(System.currentTimeMillis() - 1000L, trackedPlayers)
		}

		// TODO - Update detection logic for completing an Expedition.
		else if (event.string == "Detected Completion") {
			currentSession?.let { session ->
				DevUtils.sendDebugChat("&&aEXPEDITIONS &&fDetected valid expedition finish.", "EXPEDITION_DEBUGGER")
				val completionTime = session.startTime.timeDelta()
				session.endTime = System.currentTimeMillis()

				val pb = Vice.storage.expeditions.easter.personalBestTime
				val isNewPB = pb == -1L || completionTime < pb

				Vice.EVENT_MANAGER.publish(ExpeditionEndEvent(session, completionTime, isNewPB))

				if (isNewPB) {
					Vice.storage.expeditions.easter.personalBestTime = completionTime
					Vice.storage.markDirty()
				}
			}

			currentSession = null
		}
	}
}
