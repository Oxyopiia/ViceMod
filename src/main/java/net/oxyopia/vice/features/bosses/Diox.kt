package net.oxyopia.vice.features.bosses

import net.minecraft.entity.boss.BossBar
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.*
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.Utils.concatenate
import kotlin.time.Duration.Companion.seconds

object Diox : Boss(
	World.Diox,
	Regex("(?:TRUE )?Diox - (?:(?<health>\\d+(?:\\.\\d+)?)/\\d+(?:\\.\\d+)?|∞) ❤ \\[PHASE (?<phase>\\d+)]"),
	phaseTimesSec = listOf(5 * 60, 3 * 60, 5 * 60, 5 * 60, 85)
){
	private const val PHASE_1_MAX_TIME = 5 * 60

	private var portalCount = 0
	private var mode = DioxMode.EASY

	enum class DioxMode {
		EASY,
		NORMAL
	}

	@SubscribeEvent
	fun onChatMessage(event: ChatEvent) {
		val content = event.string
		if (World.Diox.isInWorld() && event.sender == "Diox") {

			when {
				content.contains("Diox: ITS NICE FOR US TO FINALLY MEET FACE TO FACE.") -> {
					lastKnownPhase = 0
				}
				content.contains("Diox: BUT SADLY, YOUR JOURNEY ENDS HERE.") -> {
					lastKnownPhase = 1
					lastSpawned = System.currentTimeMillis()
					portalCount = 3
				}
				content.contains("Diox: THE STRONGEST WEAPON IS ONE'S MIND.") -> {
					lastKnownPhase = 2
				}
				content.contains("Diox: YOUR DEMISE IS INEVITABLE.") -> {
					lastKnownPhase = 3
				}
				content.contains("Diox: SAY HELLO TO YOUR FRIEND, UNFORTUNATELY MINDLESS UNDER MY CONTROL.") -> {
					lastKnownPhase = 4
				}
				content.contains("Diox: ITS NICE FOR US TO FINALLY MEET FACE TO FACE.") -> {
					mode = DioxMode.EASY
				}
			}
		}
	}

	@SubscribeEvent
	fun onActionBar(event: ActionBarEvent) {
		if(event.content.toString().contains("Jump to break free.")) mode = DioxMode.NORMAL
	}

	@SubscribeEvent
	fun onSubtitle(event: SubtitleEvent) {
		if(event.subtitle == "TERMINATED") {
			lastKnownPhase = 0

			val storage = Vice.storage.bosses.diox

			when (mode) {
				DioxMode.EASY -> storage.easyCompletions++
				DioxMode.NORMAL -> storage.normalCompletions++
			}

			storage.completions++
		}
	}

	@SubscribeEvent
	fun onEntityDeath(event: EntityDeathEvent) {
		if (!world.isInWorld()) return
		val entityName = event.entity.customName?.string ?: return

		if(entityName.contains("Bound Soul")) {
			portalCount--
		}
	}

	@SubscribeEvent
	fun onBossbarAfter(event: BossBarEvents.Insert) {
		if (!World.Diox.isInWorld()) return

		if (lastKnownPhase == 1 && portalCount > 0) {
			val diff = lastSpawned.timeDelta()

			val timer = diff.formatTimer(PHASE_1_MAX_TIME.seconds)

			val text = listOf(
				"Diox - $portalCount/3 Portals".toText(Colors.ChatColor.Green, bold = true),
				" ❤ ".toText(Colors.ChatColor.Green),
				"[PHASE 1]$timer".toText(Colors.ChatColor.Green, bold = true)
			).concatenate()

			event.add(
				text,
				1.0f / 3 * portalCount,
				BossBar.Color.GREEN,
				BossBar.Style.NOTCHED_10
			)
		}
	}
}