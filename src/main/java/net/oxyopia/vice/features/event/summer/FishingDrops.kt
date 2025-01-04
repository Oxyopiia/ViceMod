package net.oxyopia.vice.features.event.summer

import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.utils.tracker.TrackerFormatting
import java.awt.Color

enum class FishingDrops(val displayName: String, val color: Color, val chatString: String = displayName) {
	MARLIN("Marlin", Color(148, 138, 255)),
	SALMON("Salmon", Color(247, 129, 129)),
	TUNA("Tuna", Color(189, 104, 76)),
	MAHI_MAHI("Mahi-Mahi", Color(189, 255, 81)),
	THE_NAUTICAL("The Nautical", Color(189, 104, 76)),
	CONCH_PIECE("Conch Piece", Color(229, 223, 215), chatString = "Conch Armour Piece"),
	PUFFERFISH_BARREL("Pufferfish Barrel", Colors.ChatColor.Yellow),
	TIDAL_VANGUARD("Tidal Vanguard", Colors.ChatColor.Blue);

	val formatting = TrackerFormatting(displayName, color)

	companion object {
		fun fromText(text: String): FishingDrops? {
			return entries.firstOrNull { text.lowercase() == it.chatString.lowercase() }
		}
	}
}