package net.oxyopia.vice.features.bosses

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ModifyBossBarEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.Utils.formatTimer
import java.util.UUID

object AbyssalVice {
	private const val PHASE_1_MAX_TIME = 5 * 60
	private const val PHASE_2_MAX_TIME = 5 * 60

	private val bossbarRegex = Regex("Abyssal Vice - (.\\d*)/\\d* ♥ \\[PHASE (\\d)]")

	private var lastSpawned = 0L
	private var lastKnownUUID: UUID? = null
	private var lastKnownHealth: Int? = null
	private var lastKnownPhase: Int? = null

	@SubscribeEvent
	fun onBossBarModifyEvent(event: ModifyBossBarEvent) {
		if (!Vice.config.BOSS_DESPAWN_TIMERS || !World.DarkVice.isInWorld()) return

		bossbarRegex.find(event.original.string)?.apply {
			if (lastKnownUUID != event.instance.uuid) {
				lastSpawned = System.currentTimeMillis()
				lastKnownUUID = event.instance.uuid
				DevUtils.sendDebugChat("&&9BOSS CHANGE &&rDetected Dark Vice change", "BOSS_DETECTION_INFO")
			}

			val diff = System.currentTimeMillis() - lastSpawned
			val style = event.original.siblings.first().style.withObfuscated(false)

			val new = when (groupValues[2]) {
				"1" -> event.original.copy().append(diff.formatTimer(PHASE_1_MAX_TIME)).setStyle(style)
				"2" -> event.original.copy().append(diff.formatTimer(PHASE_2_MAX_TIME)).setStyle(style)
				else -> return
			}

			event.setReturnValue(new)

			try {
				lastKnownHealth = groupValues[1].toInt()
				lastKnownPhase = groupValues[2].toInt()

			} catch (e: NumberFormatException) {
				DevUtils.sendErrorMessage(e, "An error occurred converting Bossbar Health of Dark Vice to an Int!")
			}
		}
	}

	@SubscribeEvent
	fun onSound(event: SoundEvent) {
		if (!Vice.config.ABYSSAL_VICE_LASER_WARNING || !World.DarkVice.isInWorld() || lastKnownPhase != 2) return
		if (event.soundName != "entity.wither.break_block" || event.volume != 9999f) return

		HudUtils.sendVanillaTitle("&&cLaser!", "", stayTime =  2.8f, fadeinout = 0.1f)
	}
}