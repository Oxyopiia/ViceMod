package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.data.World

object PPP : Boss(
	World.PPP,
	Regex("FAKER - (\\d+)/\\d+ ❤ \\[PHASE (\\d)]"),
	phaseTimesSec = listOf(2 * 60, 2 * 60)
)