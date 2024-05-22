package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.data.World

object MinehutBoss : Boss(
	World.Minehut,
	Regex("Lifesteal Box SMP Unique - (.\\d*)/\\d* ♥ \\[PHASE (\\d)]")
) {
	private const val PHASE_1_MAX_TIME = 2 * 60
	private const val PHASE_2_MAX_TIME = 3 * 60
	private const val PHASE_3_MAX_TIME = 4 * 60

	override fun getPhaseTimeSec(phase: String): Int? {
		return when (phase) {
			"1" -> PHASE_1_MAX_TIME
			"2" -> PHASE_2_MAX_TIME
			"3" -> PHASE_3_MAX_TIME
			else -> null
		}
	}
}