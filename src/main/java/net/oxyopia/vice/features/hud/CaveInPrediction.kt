package net.oxyopia.vice.features.hud

import net.minecraft.client.gui.DrawContext
import net.minecraft.util.math.Box
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.BossBarEvents
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.hud.CaveInPrediction.getMiningRegion
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.LocationUtils.isInBounds
import net.oxyopia.vice.utils.NumberUtils.clamp
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.Utils
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object CaveInPrediction : HudElement(
	"Cave-In Prediction",
	Vice.storage.lostInTime.caveInEstimatePos,
	{ Vice.storage.lostInTime.caveInEstimatePos = it },
	enabled = { Vice.config.CAVE_IN_PREDICTION },
	drawCondition = { getMiningRegion() != null }
) {
	private val CAVE_IN_DURATION = 30.seconds
	private val bossbarRegex = Regex("(\\d+)/(\\d+) .+ MINED UNTIL A CAVE-IN")

	private val regionalTracking = hashMapOf<String, TrackingData>()
	private val regions = listOf(
		"STARRY_QUARRY" to { Box(-63.0, 1.0, 29.0, 24.0, 17.0, 51.0).isInBounds(World.StarryStreets) },
		"CHARLIE_TRASH_PILE" to { Box(-75.0, 1.0, -14.0, -57.0, 32.0, 4.0).isInBounds(World.StarryStreets) },
		"SOULSWIFT_SANDS" to { World.SoulswiftSands.isInWorld() && (Utils.getPlayer()?.y ?: 100.0) <= 35.0 }
	)

	private data class TrackingData(var refPoint: TrackingPoint?, var threshold: Int, var currentCount: Int, var lastUpdated: Long, var caveInTime: Long = -1)
	private data class TrackingPoint(val timestamp: Long, val count: Int)

	private fun getMiningRegion(): String? {
		regions.forEach { if (it.second()) return it.first }
		return null
	}

	@SubscribeEvent
	fun onBossbar(event: BossBarEvents.Override) {
		bossbarRegex.find(event.original.string)?.apply {
			val region = getMiningRegion() ?: return
			val count = groupValues[1].toIntOrNull() ?: -1
			val threshold = groupValues[2].toIntOrNull() ?: -1

			val data = regionalTracking[region] ?: TrackingData(null, threshold, count, System.currentTimeMillis())

			if (data.lastUpdated.timeDelta() >= 15.seconds) {
				regionalTracking[region]?.refPoint = null
			}

			if (count != data.currentCount) {
				regionalTracking[region]?.lastUpdated = System.currentTimeMillis()
				regionalTracking[region]?.currentCount = count
			}

			if (count < data.currentCount || data.currentCount == -1 || data.refPoint == null) {
				val point = TrackingPoint(System.currentTimeMillis(), count)
				regionalTracking[region] = TrackingData(point, threshold, count, System.currentTimeMillis(), data.caveInTime)
			}

			event.adjustMiningBossbar(data)
		}
	}

	private fun BossBarEvents.Override.adjustMiningBossbar(data: TrackingData) {
		if (!Vice.config.ADJUST_DYNAMIC_MINING_BOSSBAR) return

		if (data.caveInTime.timeDelta() <= 30.seconds) {
			val timeUntil = data.caveInTime.timeDelta()
			val percentageComplete = (timeUntil / CAVE_IN_DURATION).clamp(0.0, 1.0)
			instance.percent = percentageComplete.toFloat()
		} else {
			val percentComplete = (data.currentCount.toFloat() / data.threshold).coerceIn(0f, 1f)
			instance.percent = percentComplete
		}
	}

	@SubscribeEvent
	fun onChat(event: ChatEvent) {
		when {
			event.string.startsWith("Charlie: The Trash Pile is Caving In") -> {
				regionalTracking["CHARLIE_TRASH_PILE"]?.caveInTime = System.currentTimeMillis()
			}
			event.string.startsWith("The Starry Quarry is caving in 30 seconds!") -> {
				regionalTracking["STARRY_QUARRY"]?.caveInTime = System.currentTimeMillis()
			}
			event.string.startsWith("THE SOULSWIFT SANDS ARE CAVING IN! All blocks and structures will be reset in 30 seconds") -> {
				regionalTracking["SOULSWIFT_SANDS"]?.caveInTime = System.currentTimeMillis()
			}
		}
	}

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!canDraw()) return

		val region = getMiningRegion() ?: return
		val data = regionalTracking[region] ?: return
		val point = data.refPoint ?: return

		val elapsed = point.timestamp.timeDelta()
		val blocksMined = data.currentCount - point.count
		if (blocksMined == 0) return

		val millisPerBlock = (elapsed.inWholeMilliseconds / blocksMined).stripLast2Sigfigs()
		val blocksUntilThreshold = data.threshold - data.currentCount
		val msUntilThreshold = millisPerBlock.milliseconds * blocksUntilThreshold

		val timer = msUntilThreshold.formatDuration()

		val list = listOf(
			"§7Average blocks/second: §a${String.format("%.2f", 1000.0 / millisPerBlock)}",
			"§7Blocks to Collapse: §a$blocksUntilThreshold",
			"§7Estimated Time to Collapse: §e$timer"
		)

		position.drawStrings(list, event.context)
	}

	private fun Long.stripLast2Sigfigs(): Long {
		val truncated = this / 100
		return truncated * 100
	}

	override fun Position.drawPreview(context: DrawContext): Size {
		val list = listOf(
			"§7Average blocks/second: §a0.20",
			"§7Blocks until Collapse: §a20",
			"§7Estimated Time until Collapse: §e01:40"
		)

		return position.drawStrings(list, context)
	}
}