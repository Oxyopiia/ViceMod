package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.data.World

object ElGelato : Boss(
	World.Gelato,
	Regex("(?:EL|TRUE) GELATO - (\\d+)/\\d+ ❤ \\[PHASE (\\d)]"),
	phaseTimesSec = listOf(180, 180, 180)
)