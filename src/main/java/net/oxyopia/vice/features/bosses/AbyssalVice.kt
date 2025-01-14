package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.utils.HudUtils

object AbyssalVice: Boss(
	World.AbyssalVice,
	Regex("Abyssal Vice - (?<health>\\d+(?:\\.\\d+)?)/\\d+(?:\\.\\d+)? ❤ \\[PHASE (?<phase>\\d)]"),
	phaseTimesSec = listOf(5 * 60, 5 * 60)
) {
	override val warningPercentage: Double
		get() = 0.1

	@SubscribeEvent
	fun onSound(event: SoundEvent) {
		if (!Vice.config.ABYSSAL_VICE_LASER_WARNING || !world.isInWorld() || lastKnownPhase != 2) return
		if (event.soundName != "entity.wither.break_block" || event.volume != 9999f) return

		HudUtils.sendVanillaTitle("&&cLaser!", "", stayTime =  2.8f, fadeinout = 0.1f)
	}
}