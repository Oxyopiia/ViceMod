package net.oxyopia.vice.utils.hud

enum class HorizontalAlignment {
	LEFT,
	CENTER,
	RIGHT;

	companion object {
		fun HorizontalAlignment.getNext() = entries[(ordinal + 1) % entries.size]
	}
}