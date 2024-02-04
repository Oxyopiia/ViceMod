package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ModifyBossBarEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.data.World
import net.oxyopia.vice.utils.Utils.formatTimer

object ViceBoss : Boss() {
	private const val PHASE_1_MAX_TIME = 5 * 60

	@SubscribeEvent
	override fun onBossBarModifyEvent(event: ModifyBossBarEvent) {
		if (!Vice.config.BOSS_DESPAWN_TIMERS || !isInWorld()) return

		if (event.original.string.contains("VICE")) {
			if (lastKnownUUID != event.instance.uuid) {
				lastSpawned = System.currentTimeMillis()
				lastKnownUUID = event.instance.uuid
				DevUtils.sendDebugChat("&&9BOSS CHANGE &&rDetected Vice change", "BOSS_DETECTION_INFO")
			}

			val diff = System.currentTimeMillis() - lastSpawned
			val style = event.original.siblings.first().style.withObfuscated(false)
			val timer = diff.formatTimer(getPhaseTimeSec())

			event.setReturnValue(event.original.copy().append(timer).setStyle(style))
			lastBarUpdate = System.currentTimeMillis()
		}
	}

	override fun getPhaseTimeSec(phase: String): Int {
		return PHASE_1_MAX_TIME
	}

	private fun getPhaseTimeSec(): Int {
		return getPhaseTimeSec("")
	}

	override fun isInWorld(): Boolean = World.Vice.isInWorld()
}