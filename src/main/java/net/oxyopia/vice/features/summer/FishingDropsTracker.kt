package net.oxyopia.vice.features.summer

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils.drawTexts
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.Utils

object FishingDropsTracker : HudElement("Summer Fishing Drops Tracker", Vice.storage.summer.fishingDropsPos) {
	override fun shouldDraw(): Boolean = Vice.config.SUMMER_FISHING_TRACKER
	override fun drawCondition(): Boolean = World.Summer.isInWorld() || Vice.config.SHOW_SUMMER_FISHING_TRACKER_GLOBALLY

	private val localDropRegex by lazy {
		Regex("You Fished up (.+)! \\(\\d+(?:.\\d+)?%\\)")
	}
	private val tidalVanguardRegex by lazy {
		Regex("(.+) Fished up a Tidal Vanguard! \\(\\d+(?:.\\d+)?%\\)")
	}
	private val pufferfishRegex by lazy {
		Regex("\\+1 (.+ Pufferfish)")
	}

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!shouldDraw() || !drawCondition()) return
		draw(event.context)
	}

	private fun draw(context: DrawContext): Pair<Float, Float> {
		val list: MutableList<Text> = mutableListOf("Summer Fishing Drops".toText(Vice.PRIMARY, bold = true))

		FishingDrops.entries.forEach {
			val count = Vice.storage.summer.fishups.getOrDefault(it.name, 0)
			val text = it.customDisplayName.toText(it.color).append(" $count".toText())
			list.add(text)
		}

		if (list.size == 1) {
			list.add("No fish-ups yet!".toText(Colors.ChatColor.Red))
		}

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

	override fun storePosition(position: Position) {
		Vice.storage.summer.fishingDropsPos = position
		Vice.storage.markDirty()
	}

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		return draw(context)
	}
}