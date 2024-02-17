package net.oxyopia.vice.features.arenas

import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ArenaWaveChangeEvent
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.ms
import net.oxyopia.vice.utils.Utils.timeDelta
import kotlin.time.Duration.Companion.minutes

object ArenaNotifications {
	val arenas get() = Vice.storage.arenas

	@SubscribeEvent
	fun onWaveUpdate(event: ArenaWaveChangeEvent) {
		if (Vice.config.ARENAS_MOB_EFFECT_NOTIFICATION) {
			when (event.waveNumber) {
				10 -> Utils.sendViceMessage("Mobs now have &&bSpeed I")
				15 -> Utils.sendViceMessage("Mobs now have &&bSpeed I&&r & &&aResistance I")
				20 -> Utils.sendViceMessage("Mobs now have &&bSpeed I&&r & &&aResistance II")
				25 -> Utils.sendViceMessage("Mobs now have &&bSpeed I&&r & &&aResistance III")
				30 -> Utils.sendViceMessage("Mobs now have &&bSpeed II&&r & &&aResistance III")
				40 -> Utils.sendViceMessage("Mobs now have &&bSpeed II&&r & &&aResistance III&&r & &&4Strength I")
				50 -> Utils.sendViceMessage("Mobs now have &&bSpeed II&&r & &&aResistance IV&&r & &&4Strength II")
				75 -> Utils.sendViceMessage("Mobs now have &&bSpeed II&&r & &&aResistance IV&&r & &&4Strength III")
				else -> return
			}

			Utils.playSound("block.note_block.pling", 2f)
		}
	}

	@SubscribeEvent
	fun onClientTick(event: ClientTickEvent) {
		if (!event.repeatSeconds(2)) return
		if (!Vice.config.ARENAS_COOLDOWN_NOTIFIER) return

		arenas.startTimes
			.filterNot { (world, ts) -> arenas.notifiedInstances.getOrDefault(world, 0) == ts }
			.mapKeys { World.getById(it.key) ?: return }
			.forEach { (world, ts) ->
				if (ts.timeDelta() >= 30.minutes.ms()) {
					Utils.sendViceMessage("Your Cooldown for the &&b${world.displayName}&&r Arena has passed.")
					Utils.playSound("block.note_block.pling", 1.4f)
					arenas.notifiedInstances[world.id] = ts
					Vice.storage.markDirty()
				}
			}
	}
}