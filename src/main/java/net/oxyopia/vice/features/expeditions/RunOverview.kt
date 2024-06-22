package net.oxyopia.vice.features.expeditions

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.math.floor
import kotlin.math.roundToInt

object RunOverview : HudElement("Expedition Run Overview", Vice.storage.expeditions.runOverviewPos, searchTerm = "Run Overview") {
	override fun shouldDraw(): Boolean = Vice.config.EXPEDITION_OVERVIEW
	override fun drawCondition(): Boolean = ExpeditionAPI.isInExpedition()

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!shouldDraw() || !drawCondition()) return
		if (!ExpeditionAPI.currentSession.isActive()) return

		val list = mutableListOf(
			"&&a&&lExpedition",
			"&&7Time Elapsed: &&a${ExpeditionAPI.currentSession.startTime.timeDelta().formatDuration()}",
			"&&7Current Room: &&a${ExpeditionAPI.getRoomByZ()?.id}",
			"&&7Max Room: &&a${floor(ExpeditionAPI.currentSession.gameState / 2.0).roundToInt()}",
			"&&7Players: &&a${ExpeditionAPI.currentSession.players.size}",
		)

		if (DevUtils.hasDevMode(Vice.devConfig.EXPEDITION_DEBUGGER)) {
			list.add("")
			ExpeditionAPI.currentSession.players.forEach { list.add("&&7- ${it.name.string}") }
		}

		position.drawStrings(list, event.context)
	}

	override fun storePosition(position: Position) {
		Vice.storage.expeditions.runOverviewPos = position
		Vice.storage.markDirty()
	}

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		val list = mutableListOf(
			"&&a&&lExpedition",
			"&&7Time Elapsed: &&a03:45",
			"&&7Current Room: &&a2",
			"&&7Max Room: &&a3",
			"&&7Players: &&a1",
		)

		return position.drawStrings(list, context)
	}
}