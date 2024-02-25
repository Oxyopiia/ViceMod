package net.oxyopia.vice.features.misc

import net.oxyopia.vice.Vice
import net.oxyopia.vice.Vice.Companion.storage
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.ms
import net.oxyopia.vice.utils.Utils.timeDelta
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

object DailyRewardsNotification {
	private val playerName by lazy {
		Utils.getPlayer()?.name
	}
	private var hasReminded = -1L

	@SubscribeEvent
	fun onChat(event: ChatEvent) {
		if (!event.string.contains("Daily Rewards") || storage.misc.lastDailyReward.timeDelta() < 24.hours.ms()) return
		if (!event.string.contains(playerName?.string ?: return)) return

		storage.misc.lastDailyReward = System.currentTimeMillis()
		storage.markDirty()
	}

	@SubscribeEvent
	fun onTick(event: ClientTickEvent) {
		if (!event.repeatSeconds(1)) return
		if (!Vice.config.DAILY_REWARDS_NOTIFICATION || storage.misc.lastDailyReward == -1L) return

		if (hasReminded.timeDelta() >= 30.minutes.ms() && storage.misc.lastDailyReward.timeDelta() >= 24.hours.ms()) {
			hasReminded = System.currentTimeMillis()

			Utils.sendViceMessage("Your Daily Rewards are ready to claim!")
			Utils.playSound("block.note_block.pling", 2f)
		}
	}
}