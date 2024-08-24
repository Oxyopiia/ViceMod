package net.oxyopia.vice.features.arenas

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.HudUtils.drawTexts
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.seconds

object LiveArenaInformation : HudElement("Live Arena Information", Vice.storage.arenas.liveArenaPos) {
	private const val WAVE_TIME_SECONDS = 60

	override fun shouldDraw(): Boolean = Vice.config.LIVE_ARENA_TOGGLE
	override fun drawCondition(): Boolean = ArenaSession.active && ArenaSession.relatedWorld == Utils.getDTWorld()

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!shouldDraw() || !drawCondition()) return

		val session = ArenaSession
		val world = session.relatedWorld
		val color = world.displayColor
		val dropName = ArenaAPI.getUniqueDropName(world)

		val list = mutableListOf(
			world.displayName.toText(color, bold = true),
			"Wave ${session.waveNumber}".toText(color),
		)

		if (Vice.config.LIVE_ARENA_ROUND_TIMER){
			list.add("".toText())
			list.add(session.waveStartTime.timeDelta().formatTimer(WAVE_TIME_SECONDS.seconds).toText())
		}

		val showDropsState = Vice.config.LIVE_ARENA_DROPS
		if (showDropsState != DisplayType.NONE) {
			list.add("".toText())

			if (showDropsState != DisplayType.UNIQUE_DROPS_ONLY) list.add("§fCommon Drop §7${session.calcCommonDrops()}x".toText())
			if (showDropsState != DisplayType.BASIC_DROPS_ONLY) list.add(dropName.toText(color)
				.append(session.calcUniqueDropChance().toString().toText(Colors.ChatColor.Grey)))
		}

		if (Vice.config.LIVE_ARENA_MOBS) {
			list.add("".toText())
			list.add("§fMobs Remaining §c${session.mobsRemaining.coerceAtLeast(0)}".toText())
		}

		position.drawTexts(list, event.context)
	}

	override fun storePosition(position: Position) {
		Vice.storage.arenas.liveArenaPos = position
		Vice.storage.markDirty()
	}

	override fun Position.drawPreview(context: DrawContext): Size {
		val list = mutableListOf(
			"§b&&lCryonic Caverns",
			"§bWave 16",
		)

		if (Vice.config.LIVE_ARENA_ROUND_TIMER){
			list.add("")
			list.add(26.seconds.formatTimer(WAVE_TIME_SECONDS.seconds))
		}

		val showDropsState = Vice.config.LIVE_ARENA_DROPS
		if (showDropsState != DisplayType.NONE) {
			list.add("")

			if (showDropsState != DisplayType.UNIQUE_DROPS_ONLY) list.add("§fCommon Drop §733x")
			if (showDropsState != DisplayType.BASIC_DROPS_ONLY) list.add("§bArctic Scroll §76.5%")
		}

		if (Vice.config.LIVE_ARENA_MOBS) {
			list.add("")
			list.add("§fMobs Remaining §c7")
		}

		return position.drawStrings(list, context)
	}

	private class DisplayType {
		companion object {
			const val NONE = 0
			const val BASIC_DROPS_ONLY = 1
			const val UNIQUE_DROPS_ONLY = 2
		}
	}
}