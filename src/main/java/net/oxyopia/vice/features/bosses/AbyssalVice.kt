package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.utils.HudUtils

object AbyssalVice: Boss(
	World.AbyssalVice,
	Regex("Abyssal Vice - (.\\d*)/\\d* ♥ \\[PHASE (\\d)]")
) {
	private const val PHASE_1_MAX_TIME = 5 * 60
	private const val PHASE_2_MAX_TIME = 5 * 60

	override fun getPhaseTimeSec(phase: String): Int? {
		return when (phase) {
			"1" -> PHASE_1_MAX_TIME
			"2" -> PHASE_2_MAX_TIME
			else -> null
		}
	}

	override val warningPercentage: Double
		get() = 0.1

	@SubscribeEvent
	fun onSound(event: SoundEvent) {
		if (!Vice.config.ABYSSAL_VICE_LASER_WARNING || !world.isInWorld() || lastKnownPhase != 2) return
		if (event.soundName != "entity.wither.break_block" || event.volume != 9999f) return

		HudUtils.sendVanillaTitle("&&cLaser!", "", stayTime =  2.8f, fadeinout = 0.1f)
	}

}