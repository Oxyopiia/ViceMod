package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ModifyBossBarEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.enums.World
import java.util.UUID

object ViceBoss {
	private const val PHASE_1_MAX_TIME = 5 * 60

	private var lastSpawned = 0L
	private var lastKnownUUID: UUID? = null

	@SubscribeEvent
	fun onBossBarModifyEvent(event: ModifyBossBarEvent) {
		if (!Vice.config.BOSS_DESPAWN_TIMERS || !World.Vice.isInWorld()) return

		if (event.original.string.contains("VICE")) {
			if (lastKnownUUID != event.instance.uuid) {
				lastSpawned = System.currentTimeMillis()
				lastKnownUUID = event.instance.uuid
				DevUtils.sendDebugChat("&&9BOSS CHANGE &&rDetected Vice change", "BOSS_DETECTION_INFO")
			}

			val diff = System.currentTimeMillis() - lastSpawned
			val style = event.original.siblings.first().style.withObfuscated(false)

			event.returnValue = event.original.copy().append(Utils.formatTimer(PHASE_1_MAX_TIME, diff)).setStyle(style)
		}
	}
}