package net.oxyopia.vice.features.event.summer

import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.ChestRenderEvent
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.ItemUtils.getLore
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object SummerAPI {
	private val storage get() = Vice.storage.summer
	private val violetCycleRegex by lazy {
		Regex("(?:(?<mins>\\d+) minutes? and )?(?<seconds>\\d+(?:.\\d+)?) seconds? Until Shop Refresh")
	}

	private val ICE_CREAM_RESET = 1.hours
	private val BAR_RESET = 59.minutes.plus(45.seconds)

	private var lastMenuID = -1
	internal var lastBarStart = -1L

	@SubscribeEvent
	fun onSound(event: SoundEvent) {
		if (World.Summer.isInWorld() && event.soundName == "777" && event.pitch == 1f && event.volume == 3f) {
			lastBarStart = System.currentTimeMillis()
		}
	}

	@SubscribeEvent
	fun onChestRender(event: ChestRenderEvent) {
		if (!World.Summer.isInWorld() || !event.chestName.contains("Fish Exchange") || lastMenuID == event.id) return

		val mainShopIcon = event.components.firstOrNull { it.stack.cleanName() == "Violet's Fish Exchange" } ?: return
		lastMenuID = event.id

		val lore = mainShopIcon.stack.getLore().firstNotNullOfOrNull { violetCycleRegex.find(it) } ?: return
		val mins = lore.groups["mins"]?.value?.toDoubleOrNull() ?: 0.0
		val sec = lore.groups["seconds"]?.value?.toDoubleOrNull() ?: 0.0

		val totalSeconds = mins * 60.0 + sec
		val resetTime = System.currentTimeMillis() + totalSeconds.toLong() * 1000L

		storage.violetReset = resetTime
		Vice.storage.markDirty()
	}

	@SubscribeEvent
	fun onChatMessage(event: ChatEvent) {
		if (!World.Summer.isInWorld()) return

		when {
			event.string.startsWith("Complete the orders which appear on the whiteboard correctly and in timely fashion. You have 3 lives, so make sure they count!") -> {
				val resetTime = System.currentTimeMillis() + ICE_CREAM_RESET.inWholeMilliseconds
				storage.iceCreamReset = resetTime
				Vice.storage.markDirty()
			}

			event.string.startsWith("Hold your Drunken Revolver and Fire! Destroy all the Beer Bottles to win,") -> {
				val resetTime = System.currentTimeMillis() + BAR_RESET.inWholeMilliseconds
				storage.barReset = resetTime
				Vice.storage.markDirty()
			}
		}
	}
}
