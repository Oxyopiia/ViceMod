package net.oxyopia.vice.features.arenas

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.formatTimer
import net.oxyopia.vice.utils.Utils.ms
import net.oxyopia.vice.utils.Utils.timeDelta
import kotlin.time.Duration.Companion.seconds

object LiveArenaInformation : HudElement("Live Arena Information", Vice.storage.arenas.liveArenaPos) {
	private const val WAVE_TIME_SECONDS = 60

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!shouldDraw() || !ArenaSession.active || ArenaSession.relatedWorld != Utils.getDTWorld()) return

		val session = ArenaSession
		val world = session.relatedWorld
		val color = "&&${world.displayColor}"
		val dropName = ArenaAPI.getUniqueDropName(world)

		val list = mutableListOf(
			"${color}&&l${world.displayName}",
			"${color}Wave ${session.waveNumber}",
		)

		if (Vice.config.LIVE_ARENA_ROUND_TIMER){
			list.add("")
			list.add(session.waveStartTime.timeDelta().formatTimer(WAVE_TIME_SECONDS))
		}

		val showDropsState = Vice.config.LIVE_ARENA_DROPS
		if (showDropsState != 0) {
			list.add("")

			if (showDropsState == 1 || showDropsState == 3) list.add("&&fCommon Drop &&7${session.calcCommonDrops()}x")
			if (showDropsState >= 2) list.add("${color}${dropName} &&7${session.calcUniqueDropChance()}%")
		}

		if (Vice.config.LIVE_ARENA_MOBS) {
			list.add("")
			list.add("&&fMobs Remaining &&c${session.mobsRemaining.coerceAtLeast(0)}")
		}

		position.drawStrings(list, event.context)
	}

	override fun shouldDraw(): Boolean {
		return Vice.config.LIVE_ARENA_TOGGLE
	}

	override fun storePosition(position: Position) {
		Vice.storage.arenas.liveArenaPos = position
		Vice.storage.markDirty()
	}

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		val list = mutableListOf(
			"&&b&&lCryonic Caverns",
			"&&bWave 16",
		)

		if (Vice.config.LIVE_ARENA_ROUND_TIMER){
			list.add("")
			list.add(26.seconds.ms().formatTimer(WAVE_TIME_SECONDS))
		}

		val showDropsState = Vice.config.LIVE_ARENA_DROPS
		if (showDropsState != 0) {
			list.add("")

			if (showDropsState == 1 || showDropsState == 3) list.add("&&fCommon Drop &&733x")
			if (showDropsState >= 2) list.add("&&bArctic Scroll &&76.5%")
		}

		if (Vice.config.LIVE_ARENA_MOBS) {
			list.add("")
			list.add("&&fMobs Remaining &&c7")
		}

		return position.drawStrings(list, context)
	}
}