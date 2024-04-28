package net.oxyopia.vice.features.hud

import com.google.gson.annotations.Expose
import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.ChestRenderEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.ms
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.TimeUtils.timeDeltaWithin
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

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
					"&&e\uD83D\uDD51 ${remainingMs.milliseconds.formatDuration(false)}"
				}
			}

			list.add("&&7- &&f${item.name.removePrefix("1 ")}&&7: " + timeText)
		}

		position.drawStrings(list, event.context)
	}

	private var lastHighCountError: Long = -1

	@SubscribeEvent
	fun onChestRender(event: ChestRenderEvent.Slots) {
		if (!World.MagmaHeights.isInWorld()) return
		if (!event.chestName.contains("Ember")) return

		val chestContents = event.slots.filter { it.inventory.size() != 41 }
		if (chestContents.last().stack.isEmpty) return // Inventory is still loading

		val filteredSlots = event.slots.filter { it.stack.name.string.contains("(Forging)") }
		val inventoryCount = filteredSlots.size + if (event.cursorStack.name.string.contains("(Forging)")) 1 else 0
		val storedCount = misc.forgeList.size

		if (storedCount > inventoryCount) {
			val error = storedCount - inventoryCount
			DevUtils.sendWarningMessage("Stored $error more Forge items than expected! &&7(this is likely due to a server rollback!) &&eRemoving the first $error items.", event.getErrorIntervalDump())
			misc.forgeList = misc.forgeList.drop(error).toMutableList()
			Vice.storage.markDirty()

		} else if (storedCount != inventoryCount && !lastHighCountError.timeDeltaWithin(30.seconds)) {
			val error = abs(inventoryCount - storedCount)

			DevUtils.sendWarningMessage("Found $error more Forge items than expected! &&7(likely due to another session)", event.getErrorIntervalDump())
			lastHighCountError = System.currentTimeMillis()
		}
	}

	private fun ChestRenderEvent.Slots.getErrorIntervalDump(): String {
		val filteredSlots = slots.filter { it.stack.name.string.contains("(Forging)") }
		val inventoryCount = filteredSlots.size + if (cursorStack.name.string.contains("(Forging)")) 1 else 0
		val storedCount = misc.forgeList.size

		return """
			Stored Forge Data (storedCount $storedCount):
				${misc.forgeList}
				
			Filtered Slots (inventoryCount $inventoryCount):
				$filteredSlots
			
			Slot Dumps:
				$slots
		""".trimIndent()
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