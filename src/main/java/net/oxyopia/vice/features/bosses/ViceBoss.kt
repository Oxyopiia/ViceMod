package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.data.World

object ViceBoss : Boss(
	World.Vice,
	Regex("VICE - (?<health>\\d+(?:\\.\\d+)?)/\\d+(?:\\.\\d+)? ❤"),
	phaseTimesSec = listOf(5 * 60)
) {
	override fun getPhaseTimeSec() = phaseTimesSec[0]
}