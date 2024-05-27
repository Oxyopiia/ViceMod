package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.data.World

object MinehutBoss : Boss(
	World.Minehut,
	Regex("Lifesteal Box SMP Unique - (.\\d+)/\\d+ ❤ \\[PHASE (\\d)]"),
	phaseTimesSec = listOf(2 * 60, 3 * 60, 4 * 60)
)