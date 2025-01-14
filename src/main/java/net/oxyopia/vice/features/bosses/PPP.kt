package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.data.World

object PPP : Boss(
	World.PPP,
	Regex("FAKER - (?<health>\\d+(?:\\.\\d+)?)/\\d+(?:\\.\\d+)? ❤ \\[PHASE (?<phase>\\d)]"),
	phaseTimesSec = listOf(3 * 60, 2 * 60)
)