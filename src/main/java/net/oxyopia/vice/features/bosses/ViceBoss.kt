package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.BossBarEvents
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.data.World
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.seconds

object ViceBoss : Boss(
	World.Vice,
	phaseTimesSec = listOf(5 * 60)
) {
	@SubscribeEvent
	override fun onBossBarModifyEvent(event: BossBarEvents.Override) {
		if (!Vice.config.BOSS_DESPAWN_TIMERS || !world.isInWorld()) return

		if (event.original.string.contains("VICE")) {
			if (lastKnownUUID != event.instance.uuid) {
				lastSpawned = System.currentTimeMillis()
				lastKnownUUID = event.instance.uuid
				DevUtils.sendDebugChat("&&9BOSS CHANGE &&rDetected Vice change", "BOSS_DETECTION_INFO")
			}

			val diff = lastSpawned.timeDelta()
			val style = event.original.siblings.first().style.withObfuscated(false)

			val timer = diff.formatTimer(getPhaseTimeSec().seconds)

			event.setReturnValue(event.original.copy().append(timer).setStyle(style))
			lastBarUpdate = System.currentTimeMillis()
		}
	}

	override fun getPhaseTimeSec() = phaseTimesSec[0]
}