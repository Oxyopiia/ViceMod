package net.oxyopia.vice.features.worlds.glitchhq

import net.minecraft.entity.boss.BossBar
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.BossBarEvents
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.TimeUtils.timeDeltaWithin
import kotlin.time.Duration.Companion.seconds

object DeliveryTimer {
	private val VAN_START_TIME = 20.seconds
	private val deliverySecondsRegex by lazy {
		Regex("• After (\\d*) seconds, Glitch Security")
	}

	private var deliveryDuration = 0.seconds
	private var lastEnteredDelivery = -1L
	private var lastVanStarted = -1L

	@SubscribeEvent
	fun onChatMessage(event: ChatEvent) {
		if (!Vice.config.GLITCH_HQ_DELIVERY_TIMER) return
		if (!World.GlitchHQ.isInWorld() && !World.Warehouse.isInWorld()) return

		if (event.string.contains("Delivery Dispatches in T-20 Seconds")) {
			lastVanStarted = System.currentTimeMillis()
		}

		deliverySecondsRegex.find(event.string)?.apply {
			deliveryDuration = groupValues[1].toIntOrNull()?.seconds ?: return
			lastEnteredDelivery = System.currentTimeMillis()
		}
	}

	@SubscribeEvent
	fun onBossbarAfter(event: BossBarEvents.Insert) {
		if (!Vice.config.GLITCH_HQ_DELIVERY_TIMER) return
		if (!World.GlitchHQ.isInWorld() && !World.Warehouse.isInWorld()) return

		if (World.GlitchHQ.isInWorld() && lastVanStarted.timeDeltaWithin(VAN_START_TIME)) {
			event.drawVanStartBossbar()
			return
		}

		if (lastEnteredDelivery.timeDeltaWithin(deliveryDuration)) {
			event.drawDeliveryTimerBossbar()
		}
	}

	private fun BossBarEvents.Insert.drawVanStartBossbar() {
		val elapsedTime = lastVanStarted.timeDelta()
		val percentageComplete = elapsedTime / VAN_START_TIME

		add(
			Text.translatable("Delivery Start ${elapsedTime.formatTimer(VAN_START_TIME)}").formatted(Formatting.RED),
			1f - percentageComplete.toFloat(),
			BossBar.Color.RED,
			BossBar.Style.NOTCHED_10
		)
	}

	private fun BossBarEvents.Insert.drawDeliveryTimerBossbar() {
		val elapsedTime = lastEnteredDelivery.timeDelta()
		val percentageComplete = elapsedTime / deliveryDuration

		add(
			Text.translatable("Delivery ${elapsedTime.formatTimer(deliveryDuration)}").formatted(Formatting.YELLOW),
			1f - percentageComplete.toFloat(),
			BossBar.Color.YELLOW,
			BossBar.Style.NOTCHED_10
		)
	}
}