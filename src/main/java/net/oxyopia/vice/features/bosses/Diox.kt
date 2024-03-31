package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.data.World

object Diox : Boss(
    Regex("(?:TRUE )?Diox\\s*-\\s*(\\d+(?:\\.\\d+)?/\\d+(?:\\.\\d+)?|∞)\\s*♥\\s*\\[PHASE\\s*(\\d+)]\$")
){
    private const val PHASE_1_MAX_TIME = 5 * 60
    private const val PHASE_2_MAX_TIME = 3 * 60
    private const val PHASE_3_MAX_TIME = 1 * 60
    private const val PHASE_4_MAX_TIME = 5 * 60
    private const val PHASE_5_MAX_TIME = 85

    override fun getPhaseTimeSec(phase: String): Int? {
        return when (phase) {
            "2" -> PHASE_2_MAX_TIME
            "4" -> PHASE_4_MAX_TIME
            "5" -> PHASE_5_MAX_TIME
            else -> null
        }
    }

    override fun isInWorld(): Boolean = World.Diox.isInWorld()
}