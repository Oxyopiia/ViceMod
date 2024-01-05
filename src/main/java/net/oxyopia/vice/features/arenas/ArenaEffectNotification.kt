package net.oxyopia.vice.features.arenas

import net.minecraft.util.Identifier
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ArenaWaveChangeEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.Utils

object ArenaEffectNotification {
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

			Utils.playSound(Identifier("minecraft", "block.note_block.pling"), 2f, 1f)
		}
	}
}