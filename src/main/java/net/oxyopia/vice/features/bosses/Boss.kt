package net.oxyopia.vice.features.bosses

import net.minecraft.text.Style
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.BossBarEvents
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.TimeUtils.ms
import java.util.*
import kotlin.time.Duration.Companion.seconds

abstract class Boss (
	val world: World,
	private val bossbarRegex: Regex = Regex(""),
	val phaseTimesSec: List<Int> = listOf()
){
	var lastSpawned = 0L
	var lastBarUpdate = 0L
	var lastKnownUUID: UUID? = null
	var lastKnownHealth: Int? = null
	var lastKnownPhase: Int? = null

	open val warningPercentage = 0.15

	private var lastDespawnNotify: Long = -1

	@SubscribeEvent
	open fun onBossBarModifyEvent(event: BossBarEvents.Override) {
		if (!Vice.config.BOSS_DESPAWN_TIMERS || !world.isInWorld()) return

		bossbarRegex.find(event.original.string)?.apply {
			if (lastKnownUUID != event.instance.uuid) {
				lastSpawned = System.currentTimeMillis()
				lastKnownUUID = event.instance.uuid
				DevUtils.sendDebugChat("&&9BOSS CHANGE &&rDetected a Boss change", "BOSS_DETECTION_INFO")
			}

			val phase = groupValues[2].toIntOrNull() ?: return
			val phaseTime = getPhaseTimeSec(phase) ?: return
			val diff = lastSpawned.timeDelta()

			val timer = diff.formatTimer(phaseTime)

			val style = event.original.siblings.lastOrNull()?.style?.withObfuscated(false) ?: Style.EMPTY
			event.setReturnValue(event.original.copy().append(timer).setStyle(style))

			lastBarUpdate = System.currentTimeMillis()
			lastKnownHealth = groupValues[1].toIntOrNull()
			lastKnownPhase = groupValues[2].toIntOrNull()
		}
	}

	@SubscribeEvent
	fun onTick(event: ClientTickEvent) {
		if (!event.repeatSeconds(1) || !Vice.config.BOSS_DESPAWN_WARNING || !world.isInWorld()) return

		val phaseTime = 1000 * (getPhaseTimeSec(lastKnownPhase) ?: return)
		DevUtils.sendDebugChat("ps: $phaseTime")
		DevUtils.sendDebugChat("delta: ${lastSpawned.timeDelta()}")

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

	private fun getPhaseTimeSec(phaseId: Int?): Int? {
		if (phaseId == null || phaseId < 1) return null
		return phaseTimesSec[phaseId - 1]
	}

	open fun getPhaseTimeSec(): Int? {
		return getPhaseTimeSec(lastKnownPhase)
	}
}