package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.data.World

object ShadowGelato : Boss(
	World.ShadowGelato,
	Regex("(?:TRUE )?SHADOW GELATO - (.\\d+)/\\d+ ❤ \\[PHASE (\\d)]"),
	phaseTimesSec = listOf(2 * 60, 3 * 60, 2 * 60)
)