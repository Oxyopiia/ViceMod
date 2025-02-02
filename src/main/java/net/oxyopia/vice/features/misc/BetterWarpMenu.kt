package net.oxyopia.vice.features.misc

import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.events.ItemTooltipEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.ItemUtils.cleanName
import java.awt.Color

object BetterWarpMenu {
	private const val WARP_STARTER = "Left-Click to Warp"
	private val FLOOR_REGEX by lazy {
		Regex("Floor (\\d+)")
	}

	private val worlds: List<List<MutableText>> = listOf(
		listOf("Reality Peak".toText(Colors.ChatColor.LightPurple), "Deserted Dunes".toText(Colors.ChatColor.Yellow)),
		listOf("Space Escape".toText(Colors.SpaceEscape), "Fastest Food".toText(Colors.FastestFood)),
		listOf("Lost Temple".toText(Colors.LostTemple), "Bitter Battleground".toText(Color.WHITE)),
		listOf("Magma Heights".toText(Colors.MagmaHeights), "Volume".toText(Colors.Volume)),
		listOf("Glimpse".toText(Colors.Glimpse), "The Arcade".toText(Colors.ChatColor.DarkGrey)),
		listOf("Showdown".toText(Colors.Showdown), "The Journey to the Glitch HQ".toText(Colors.GlitchHQ)),
		listOf("Lost In Time".toText(Colors.LostInTime), "Soulswift Sands".toText(Colors.LostInTime), "Timeless Tastes".toText(Colors.Showdown), "Cosmic Collapse".toText(Colors.SpaceEscape)),
		listOf("Starry Streets".toText(Colors.StarryStreets), "On the Run".toText(Colors.OnTheRun)),
	)
	private val bosses: List<MutableText> = listOf(
		"Vice".toText(Colors.ViceBoss),
		"Wasteyard".toText(Colors.ChatColor.DarkRed),
		"El Gelato".toText(Colors.ChatColor.Green),
		"Percentage Player Percentage".toText(Colors.ChatColor.Red),
		"Lifesteal Box SMP".toText(Colors.ChatColor.Aqua),
		"Diox".toText(Colors.Diox),
		"Elderpork".toText(Colors.Elderpork),
		"THE VATICAN".toText(Colors.ChatColor.Aqua)
	)

	@SubscribeEvent
	fun onItemTooltip(event: ItemTooltipEvent) {
		if (!Vice.config.BETTER_WARP_MENU || !event.lines.any { it.string.startsWith(WARP_STARTER) }) return

		val floorMatch = FLOOR_REGEX.find(event.stack.cleanName()) ?: return
		val floorIndex = (floorMatch.groupValues[1].toIntOrNull() ?: 0) - 1

		if (floorIndex !in worlds.indices) return

		val worldLines = worlds[floorIndex].map {
			val bullet = if (it.string.cleanName() != "Lost In Time" && floorIndex == 6) "  ◦ " else "• "
			bullet.toText(Colors.ChatColor.DarkGrey).append(it)
		}

		val bossLine = "• ".toText(Colors.ChatColor.DarkGrey).append(
			bosses.getOrNull(floorIndex) ?: "Unknown Boss".toText(Colors.ChatColor.Red)
		)

		val newLore = mutableListOf<Text>().apply {
			add(event.stack.name)
			add("Worlds:".toText(Colors.ChatColor.Grey))
			addAll(worldLines)
			add(Text.empty())
			add("Boss:".toText(Colors.ChatColor.Grey))
			add(bossLine)

			add(Text.empty())

			if (event.stack.cleanName() == "Floor 6") {
				add("Left-Click to Warp (Anodized)".toText(Colors.ChatColor.Grey))
				add("Right-Click to Warp (Regular)".toText(Colors.ChatColor.Grey))
			} else {
				add("Left-Click to Warp".toText(Colors.ChatColor.Grey))
			}
		}

		event.lines.clear()
		event.lines.addAll(newLore)
	}
}
