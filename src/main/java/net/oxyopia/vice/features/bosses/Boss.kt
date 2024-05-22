package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.BossBarEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.TimeUtils.ms
import net.oxyopia.vice.utils.TimeUtils.timeDeltaDuration
import java.util.*
import kotlin.time.Duration.Companion.seconds

abstract class Boss (
	val world: World,
	private val bossbarRegex: Regex = Regex("")
){
	var lastSpawned = 0L
	var lastBarUpdate = 0L
	var lastKnownUUID: UUID? = null
	var lastKnownHealth: Int? = null
	var lastKnownPhase: Int? = null

	open val warningPercentage = 0.15

	private var lastDespawnNotify: Long = -1

	@SubscribeEvent
	open fun onBossBarModifyEvent(event: BossBarEvent.Override) {
		if (!Vice.config.BOSS_DESPAWN_TIMERS || !world.isInWorld()) return

		bossbarRegex.find(event.original.string)?.apply {
			if (lastKnownUUID != event.instance.uuid) {
				lastSpawned = System.currentTimeMillis()
				lastKnownUUID = event.instance.uuid
				DevUtils.sendDebugChat("&&9BOSS CHANGE &&rDetected a Boss change", "BOSS_DETECTION_INFO")
			}

			val diff = lastSpawned.timeDeltaDuration()
			val style = event.original.siblings.first().style.withObfuscated(false)

			val phaseTime = getPhaseTimeSec(groupValues[2]) ?: return
			val timer = diff.formatTimer(phaseTime.seconds)

			event.setReturnValue(event.original.copy().append(timer).setStyle(style))
			lastBarUpdate = System.currentTimeMillis()

			try {
				lastKnownHealth = groupValues[1].toInt()
				lastKnownPhase = groupValues[2].toInt()

			} catch (e: NumberFormatException) {
				DevUtils.sendErrorMessage(e, "An error occurred converting Bossbar Health of a Boss to an Int!")
			}
		}
	}

	@SubscribeEvent
	fun onTick(event: ClientTickEvent) {
		if (!event.repeatSeconds(1) || !Vice.config.BOSS_DESPAWN_WARNING || !world.isInWorld()) return

		val phaseTime = getPhaseTimeSec(lastKnownPhase.toString())?.times(1000) ?: return

		if (
			!isLikelyAlive() ||
			phaseTime <= 0 ||
			lastSpawned.timeDelta() <= (1 - warningPercentage) * phaseTime ||
			(lastDespawnNotify > 0 && lastDespawnNotify.timeDelta() <= phaseTime * warningPercentage)
		) return

		Utils.playSound("block.note_block.pling", pitch = 0.8f, volume = 3f)
		HudUtils.sendViceTitle("&&cLow boss time!", 2f)

		lastDespawnNotify = System.currentTimeMillis()
	}

	private fun isLikelyAlive() = lastBarUpdate.timeDelta() <= 0.5.seconds.ms()

	abstract fun getPhaseTimeSec(phase: String): Int?
}