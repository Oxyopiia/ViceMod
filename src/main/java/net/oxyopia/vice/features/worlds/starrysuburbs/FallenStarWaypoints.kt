package net.oxyopia.vice.features.worlds.starrysuburbs

import net.minecraft.util.math.Vec3d
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.WorldRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.RenderUtils.drawString
import net.oxyopia.vice.utils.SoundUtils
import net.oxyopia.vice.utils.TimeUtils.formatShortDuration
import net.oxyopia.vice.utils.TimeUtils.timeDeltaUntil
import net.oxyopia.vice.utils.TimeUtils.timeDeltaWithin
import kotlin.time.Duration.Companion.seconds

object FallenStarWaypoints {
	private val STAR_DURATION = 60.seconds

	private val starRegex by lazy{
		Regex("A star (?:will fall|has fallen) at (-?\\d+.?\\d+?), (-?\\d+.?\\d+?)!(?: \\(Priority Pick\\))?")
	}
	private val stars = mutableListOf<FallenStar>()

	private data class FallenStar(val x: Double, val z: Double, val timestamp: Long, val isPriorityPick: Boolean)

	@SubscribeEvent
	fun onChat(event: ChatEvent) {
		if (!Vice.config.STAR_WAYPOINTS || !World.StarryStreets.isInWorld() || !event.hasNoSender) return

		val pos = starRegex.find(event.string) ?: return
		val x = pos.groupValues[1].toDoubleOrNull() ?: return
		val z = pos.groupValues[2].toDoubleOrNull() ?: return

		if (stars.any { star -> x == star.x && z == star.z && star.isPriorityPick }) return // Avoid duplicates when Priority Pick is triggered

		val isPriorityPick = event.string.endsWith("(Priority Pick)")

		val star = FallenStar(x, z, System.currentTimeMillis(), isPriorityPick)
		stars.add(star)

		val title = if (isPriorityPick) "§bPriority Pick!" else "§6Star!"
		HudUtils.sendViceTitle(title, 3f)
		SoundUtils.playDing()
	}

	@SubscribeEvent
	fun onWorldRender(event: WorldRenderEvent) {
		if (!Vice.config.STAR_WAYPOINTS || !World.StarryStreets.isInWorld()) return
		val toRemove = mutableListOf<FallenStar>()

		stars.forEachIndexed { index, star ->
			if (!star.timestamp.timeDeltaWithin(STAR_DURATION)) {
				toRemove.add(star)
				return@forEachIndexed
			}
			val remaining = star.timestamp.plus(STAR_DURATION.inWholeMilliseconds).timeDeltaUntil()

			val color = when {
				star.isPriorityPick -> Colors.ChatColor.Aqua
				index == stars.size - 1 -> Colors.ChatColor.Gold
				else -> Colors.ChatColor.Blue
			}

			val pos = Vec3d(star.x, 78.0, star.z)
			event.drawString(pos, "Fallen Star", color, size = 3f)
			event.drawString(pos.subtract(0.0, 1.0, 0.0), remaining.formatShortDuration(), color, size = 3f)
		}

		stars.removeAll(toRemove)
	}
}