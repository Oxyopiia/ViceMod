package net.oxyopia.vice.features.event.summer

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.utils.tracker.EnumTracker
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.hud.HudUtils.drawTexts
import net.oxyopia.vice.utils.hud.HudUtils.toText
import net.oxyopia.vice.utils.Utils

object FishingDropsTracker : EnumTracker<FishingDrops>(
	"Summer Fishing Drops Tracker",
	FishingDrops.entries,
	{ Vice.storage.summer.fishups },
	getEnumFormatting = { it?.formatting },
	position = Vice.storage.summer.fishingDropsPos,
	storePosition = { Vice.storage.summer.fishingDropsPos = it },
	enabled = { Vice.config.SUMMER_FISHING_TRACKER },
	drawCondition = {  World.Summer.isInWorld() || Vice.config.SHOW_SUMMER_FISHING_TRACKER_GLOBALLY }
) {
	private val localDropRegex by lazy {
		Regex("You Fished up (.+)! \\(\\d+(?:.\\d+)?%\\)")
	}
	private val tidalVanguardRegex by lazy {
		Regex("(.+) Fished up a Tidal Vanguard! \\(\\d+(?:.\\d+)?%\\)")
	}
	private val pufferfishRegex by lazy {
		Regex("\\+1 (.+ Pufferfish)")
	}

	override fun draw(context: DrawContext): Size {
		val list = mutableListOf(title.toText(Colors.Wave, bold = true))
		list.addAll(getCounts().writeDefault())

		val counts = Pufferfish.entries.associateWith { Vice.storage.summer.pufferfishOpened[it.name] ?: 0 }
		val text = counts
			.map { (fish, count) -> count.toString().toText(fish.color) }
			.reduceIndexed { index, acc, text ->
				if (index == 0) acc.append(text) else acc.append("-".toText(Colors.ChatColor.Grey)).append(text)
			}
		list.add(Text.empty())
		list.add("Pufferfish ".toText(Colors.ChatColor.Yellow).append(text))

		return position.drawTexts(list, context)
	}

	@SubscribeEvent
	fun onChatMessage(event: ChatEvent) {
		if (event.string.endsWith(" Pufferfish")) {
			pufferfishRegex.matchEntire(event.string)?.apply {
				val type = Pufferfish.fromText(groupValues[1])?.name ?: return
				val stats = Vice.storage.summer.pufferfishOpened

				stats[type] = stats.getOrDefault(type, 0) + 1
				Vice.storage.markDirty()
			}
		}

		if (!World.Summer.isInWorld() || !event.string.contains("Fished up")) return
		val fishups = Vice.storage.summer.fishups

		localDropRegex.matchEntire(event.string)?.apply {
			val id = FishingDrops.fromText(groupValues[1].removePrefix("a "))?.name ?: run {
				DevUtils.sendWarningMessage("Unable to match §b${groupValues[1]} §eto a Fishing Item! Still tracked, but will not display.", groupValues[1])
				groupValues[1]
			}
			fishups[id] = fishups.getOrDefault(id, 0) + 1
			Vice.storage.markDirty()
		}

		tidalVanguardRegex.matchEntire(event.string)?.apply {
			val ign = Utils.getPlayer()?.name?.string ?: return
			if (ign != groupValues[1]) return

			fishups[FishingDrops.TIDAL_VANGUARD.name] = fishups.getOrDefault(FishingDrops.TIDAL_VANGUARD.name, 0) + 1
			Vice.storage.markDirty()
		}	
	}

	override fun Position.drawPreview(context: DrawContext): Size {
		return draw(context)
	}
}