package net.oxyopia.vice.features.bosses

import net.minecraft.text.Style
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Debugger
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.BossBarEvents
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.SoundUtils
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

abstract class Boss (
	val world: World,
	private val bossbarRegex: Regex = Regex(""),
	val phaseTimesSec: List<Int> = listOf()
){
	var lastSpawned = 0L
	var lastBarUpdate = 0L
	var lastKnownUUID: UUID? = null
	var lastKnownHealth: Float? = null
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
				Debugger.BOSS.debug("Detected a Boss change")
			}

			val health = groups["health"]?.value?.toFloatOrNull()

			val phase = try {
				groups["phase"]?.value?.toIntOrNull() ?: 1
			} catch (e: IllegalArgumentException) {
				1
			}

			val phaseTime = getPhaseTimeSec(phase) ?: return
			if (phaseTime <= 0) return
			val diff = lastSpawned.timeDelta()

			val timer = diff.formatTimer(phaseTime.seconds)

			val style = event.original.siblings.lastOrNull()?.style?.withObfuscated(false) ?: Style.EMPTY
			event.setReturnValue(event.original.copy().append(timer).setStyle(style))

			lastBarUpdate = System.currentTimeMillis()
			lastKnownHealth = health
			lastKnownPhase = phase
		}
	}

	@SubscribeEvent
	fun onTick(event: ClientTickEvent) {
		if (!event.repeatSeconds(1) || !Vice.config.BOSS_DESPAWN_WARNING || !world.isInWorld()) return

		val phaseTime = 1000 * (getPhaseTimeSec(lastKnownPhase) ?: return)

		if (
			!isLikelyAlive() ||
			phaseTime <= 0 ||
			lastSpawned.timeDelta() <= phaseTime.milliseconds * (1 - warningPercentage) ||
			(lastDespawnNotify > 0 && lastDespawnNotify.timeDelta() <= phaseTime.milliseconds * warningPercentage)
		) return

		SoundUtils.playSound("block.note_block.pling", pitch = 0.8f, volume = 3f)
		HudUtils.sendViceTitle("&&cLow boss time!", 2f)

		lastDespawnNotify = System.currentTimeMillis()
	}

	private fun isLikelyAlive() = lastBarUpdate.timeDelta() <= 0.5.seconds

	private fun getPhaseTimeSec(phaseId: Int?): Int? {
		if (phaseId == null || phaseId < 1) return null
		return phaseTimesSec[phaseId - 1]
	}

	open fun getPhaseTimeSec(): Int? {
		return getPhaseTimeSec(lastKnownPhase)
	}
}