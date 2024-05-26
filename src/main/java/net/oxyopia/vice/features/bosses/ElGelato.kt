package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.data.World

object ElGelato : Boss(
	World.Gelato,
	Regex("(?:EL|TRUE) GELATO - (.\\d+)/\\d+ ❤ \\[PHASE (\\d)]"),
	phaseTimesSec = listOf(60, 120, 120)
)