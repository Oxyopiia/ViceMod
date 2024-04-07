package net.oxyopia.vice.features.hud

import com.google.gson.annotations.Expose
import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.ms
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.minutes

object ForgeTimers : HudElement("Forge Times", Vice.storage.misc.forgeTimersPos) {
	private val misc = Vice.storage.misc
	private val startRegex = Regex("\\+⌚ (\\d+ .+) \\((\\d+)m\\)")
	private val collectRegex = Regex("\\+(.+)")

	override fun shouldDraw(): Boolean = Vice.config.FORGE_TIMERS

	@SubscribeEvent
	fun onChatMessage(event: ChatEvent) {
		if (!World.MagmaHeights.isInWorld()) return
		val content = event.string

		startRegex.find(content)?.apply {
			val duration = groupValues[2].toInt()
			val offsetTime = System.currentTimeMillis() + duration.minutes.ms()

			misc.forgeList.add(ForgeItem(groupValues[1], offsetTime))
			Vice.storage.markDirty()
			return
		}

		collectRegex.find(content)?.apply {
			if (misc.forgeList.isEmpty()) return
			val firstValidIndex = misc.forgeList.indexOfFirst { groupValues[1] == it.name && it.timestamp <= System.currentTimeMillis() }

			if (firstValidIndex != -1) {
				misc.forgeList.removeAt(firstValidIndex)
				Vice.storage.markDirty()
			}
		}
	}

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!shouldDraw()) return
		if (misc.forgeList.isEmpty()) return

		val list = mutableListOf("&&b&&lForge")

		misc.forgeList.forEach { item ->
			val timeText = when {
				item.timestamp <= System.currentTimeMillis() -> "&&aREADY"
				else -> {
					val remainingMs = item.timestamp.timeDelta() * -1
					"&&e\uD83D\uDD51 ${formatDuration(remainingMs, false)}"
				}
			}

			list.add("&&7- &&f${item.name.removePrefix("1 ")}&&7: " + timeText)
		}

		position.drawStrings(list, event.context)
	}

	override fun storePosition(position: Position) {
		Vice.storage.misc.forgeTimersPos = position
		Vice.storage.markDirty()
	}

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		val list = listOf(
			"&&b&&lForge",
			"&&7- &&fSteel&&7: &&aREADY",
			"&&7- &&fSteel&&7: &&e\uD83D\uDD51 10:36",
			"&&7- &&fSteel&&7: &&e\uD83D\uDD51 12:57",
		)

		return position.drawStrings(list, context)
	}

	data class ForgeItem(
		@Expose val name: String,
		@Expose val timestamp: Long
	)
}