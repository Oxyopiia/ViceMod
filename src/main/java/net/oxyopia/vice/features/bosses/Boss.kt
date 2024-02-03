package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ModifyBossBarEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.Utils.formatTimer
import java.util.*

abstract class Boss constructor(
	val bossbarRegex: Regex
){
	var lastSpawned = 0L
	var lastKnownUUID: UUID? = null
	var lastKnownHealth: Int? = null
	var lastKnownPhase: Int? = null

	@SubscribeEvent
	fun onBossBarModifyEvent(event: ModifyBossBarEvent) {
		if (!Vice.config.BOSS_DESPAWN_TIMERS || !isInWorld()) return

		bossbarRegex.find(event.original.string)?.apply {
			if (lastKnownUUID != event.instance.uuid) {
				lastSpawned = System.currentTimeMillis()
				lastKnownUUID = event.instance.uuid
				DevUtils.sendDebugChat("&&9BOSS CHANGE &&rDetected a Boss change", "BOSS_DETECTION_INFO")
			}

			val diff = System.currentTimeMillis() - lastSpawned
			val style = event.original.siblings.first().style.withObfuscated(false)

			val timer = diff.formatTimer(getPhaseTime(groupValues[2]))

			event.setReturnValue(event.original.copy().append(timer).setStyle(style))

			try {
				lastKnownHealth = groupValues[1].toInt()
				lastKnownPhase = groupValues[2].toInt()

			} catch (e: NumberFormatException) {
				DevUtils.sendErrorMessage(e, "An error occurred converting Bossbar Health of a Boss to an Int!")
			}
		}
	}

	abstract fun getPhaseTime(phase: String): Int

	abstract fun isInWorld(): Boolean
}