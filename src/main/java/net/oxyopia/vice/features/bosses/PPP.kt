package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ModifyBossBarEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.enums.World
import java.util.UUID
import kotlin.math.ceil

object PPP {
	private const val PHASE_1_MAX_TIME = 2 * 60
	private const val PHASE_2_MAX_TIME = 2 * 60

	private val bossbarRegex = Regex("FAKER - (.\\d*)/\\d* ♥ \\[PHASE (\\d)]")

	private var lastSpawned = 0L
	private var lastKnownUUID: UUID? = null
	private var lastKnownHealth: Int? = null

	@SubscribeEvent
	fun onBossBarModifyEvent(event: ModifyBossBarEvent) {
		if (!Vice.config.BOSS_DESPAWN_TIMERS || !World.PPP.isInWorld()) return

		bossbarRegex.find(event.original.string)?.apply {
			if (lastKnownUUID != event.instance.uuid) {
				lastSpawned = System.currentTimeMillis()
				lastKnownUUID = event.instance.uuid
				DevUtils.sendDebugChat("&&9BOSS CHANGE &&rDetected PPP change", "BOSS_DETECTION_INFO")
			}

			val diff = System.currentTimeMillis() - lastSpawned
			val style = event.original.siblings.first().style.withObfuscated(false)

			event.returnValue =
				when (groupValues[2]) {
				"1" -> event.original.copy().append(Utils.formatTimer(PHASE_1_MAX_TIME, diff)).setStyle(style)
				"2" -> event.original.copy().append(Utils.formatTimer(PHASE_2_MAX_TIME, diff)).setStyle(style)
				else -> event.original
			}

			try {
				lastKnownHealth = groupValues[1].toInt()
			} catch (e: NumberFormatException) {
				DevUtils.sendErrorMessage(e, "An error occurred converting Bossbar Health of PPP to an Int!")
			}
		}
	}
}