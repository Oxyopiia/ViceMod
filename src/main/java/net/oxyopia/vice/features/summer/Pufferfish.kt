package net.oxyopia.vice.features.summer

import net.oxyopia.vice.data.Colors
import java.awt.Color

enum class Pufferfish(val displayName: String, val color: Color = Color.white) {
	RED_PUFFERFISH("Red Pufferfish", Colors.ChatColor.Red),
	GREEN_PUFFERFISH("Green Pufferfish", Colors.ChatColor.Green),
	BLUE_PUFFERFISH("Blue Pufferfish", Colors.ChatColor.Blue),
	ORANGE_PUFFERFISH("Orange Pufferfish", Colors.ChatColor.Gold),
	PURPLE_PUFFERFISH("Purple Pufferfish", Colors.ChatColor.DarkPurple),
	YELLOW_PUFFERFISH("Yellow Pufferfish", Colors.ChatColor.Yellow);

	companion object {
		fun fromText(text: String): Pufferfish? {
			return entries.firstOrNull { text.lowercase() == it.displayName.lowercase() }
		}
	}
}