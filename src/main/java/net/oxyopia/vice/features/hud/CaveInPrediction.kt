package net.oxyopia.vice.features.hud

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.BossBarEvents
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.Utils
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object CaveInPrediction : HudElement("Cave-In Prediction", Vice.storage.lostInTime.caveInEstimatePos) {
	private val bossbarRegex = Regex("(\\d+)/(\\d+) BLOCKS MINED UNTIL A CAVE-IN")

	private var currentCount = -1
	private var currentThreshold = -1
	private var tracking: TrackingPoint? = null
	private var lastUpdated = -1L

	private data class TrackingPoint(val timestamp: Long, val count: Int)

	override fun shouldDraw(): Boolean = Vice.config.LOST_IN_TIME_CAVE_PREDICTION
	override fun drawCondition(): Boolean = World.SoulswiftSands.isInWorld() && (Utils.getPlayer()?.y ?: 100.0) <= 35.0

	@SubscribeEvent
	fun onBossbar(event: BossBarEvents.Read) {
		bossbarRegex.find(event.name.string)?.apply {
			val count = groupValues[1].toIntOrNull() ?: -1
			val threshold = groupValues[2].toIntOrNull() ?: -1

			if (lastUpdated.timeDelta() >= 15.seconds) tracking = null
			if (count != currentCount) lastUpdated = System.currentTimeMillis()

			if (count < currentCount || currentCount == -1 || tracking == null) {
				tracking = TrackingPoint(System.currentTimeMillis(), count)
			}

			currentCount = count
			currentThreshold = threshold
		}
	}

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!shouldDraw() || !drawCondition()) return

		val tracking = tracking ?: return

		val elapsed = tracking.timestamp.timeDelta()
		val blocksMined = currentCount - tracking.count

		if (blocksMined == 0) return
		val millisPerBlock = (elapsed.inWholeMilliseconds / blocksMined).stripLast2Sigfigs()

		val blocksUntilThreshold = currentThreshold - currentCount
		val msUntilThreshold = millisPerBlock.milliseconds * blocksUntilThreshold

		val timer = msUntilThreshold.formatDuration()

		val list = listOf(
			"Average blocks/second: &&a${String.format("%.2f", 1000.0 / millisPerBlock)}",
			"Blocks to Collapse: &&a$blocksUntilThreshold",
			"Estimated Time to Collapse: &&e$timer"
		)

		position.drawStrings(list, event.context)
	}

	private fun Long.stripLast2Sigfigs(): Long {
		val truncated = this / 100
		return truncated * 100
	}

	override fun storePosition(position: Position) {
		Vice.storage.lostInTime.caveInEstimatePos = position
		Vice.storage.markDirty()
	}

	override fun Position.drawPreview(context: DrawContext): Size {
		val list = listOf(
			"Average blocks/second: &&a0.20",
			"Blocks until Collapse: &&a20",
			"Estimated Time until Collapse: &&e01:40"
		)

		return position.drawStrings(list, context)
	}
}