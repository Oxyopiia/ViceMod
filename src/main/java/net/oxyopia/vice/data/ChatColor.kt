package net.oxyopia.vice.data

import java.awt.Color

/**
 * Color values sourced from Essential mod in preparation for dropping it.
 *
 * @author EssentialGG
 */
enum class ChatColor(
	val char: Char,
	val color: Color = Color.white,
	val isFormat: Boolean = false
) {
	BLACK('0', Color(0x000000)),
	DARK_BLUE('1', Color(0x0000AA)),
	DARK_GREEN('2', Color(0x00AA00)),
	DARK_AQUA('3', Color(0x00AAAA)),
	DARK_RED('4', Color(0xAA0000)),
	DARK_PURPLE('5', Color(0xAA00AA)),
	GOLD('6', Color(0xFFAA00)),
	GRAY('7', Color(0xAAAAAA)),
	DARK_GRAY('8', Color(0x555555)),
	BLUE('9', Color(0x5555FF)),
	GREEN('a', Color(0x55FF55)),
	AQUA('b', Color(0x55FFFF)),
	RED('c', Color(0xFF5555)),
	LIGHT_PURPLE('d', Color(0xFF55FF)),
	YELLOW('e', Color(0xFFFF55)),
	WHITE('f', Color(0xFFFFFF)),
	OBFUSCATE('k', isFormat = true),
	BOLD('l', isFormat = true),
	STRIKETHROUGH('m', isFormat = true),
	UNDERLINE('n', isFormat = true),
	ITALIC('o', isFormat = true),
	RESET('r');
}