package net.oxyopia.vice.features.summer

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChestRenderEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawTexts
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.ItemUtils.getLore
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.timeDeltaUntil
import java.awt.Color
import kotlin.time.Duration.Companion.seconds

object VioletOverlay : HudElement("Violet's Exchange Overlay", Vice.storage.summer.violetInfoPos){
	override fun shouldDraw(): Boolean = Vice.config.VIOLET_EXCHANGE_OVERLAY
	override fun drawCondition(): Boolean = World.Summer.isInWorld()

	private var lastMenuID = -1
	private val shopCycleRegex by lazy {
		Regex("(?:(?<mins>\\d+) minutes? and )?(?<seconds>\\d+(?:.\\d+)?) seconds? Until Shop Refresh")
	}

	@SubscribeEvent
	fun onChestRender(event: ChestRenderEvent) {
		if (!World.Summer.isInWorld() || !event.chestName.contains("Fish Exchange") || lastMenuID == event.id) return

		val mainShopIcon = event.components.firstOrNull { it.stack.cleanName() == "Violet's Fish Exchange" } ?: return
		lastMenuID = event.id

		val lore = mainShopIcon.stack.getLore().firstNotNullOfOrNull { shopCycleRegex.find(it) } ?: return
		val mins = lore.groups["mins"]?.value?.toDoubleOrNull() ?: 0.0
		val sec = lore.groups["seconds"]?.value?.toDoubleOrNull() ?: 0.0

		val totalSeconds = mins * 60.0 + sec
		val resetTime = System.currentTimeMillis() + totalSeconds.toLong() * 1000L + 1000L

		Vice.storage.summer.violetReset = resetTime
		Vice.storage.markDirty()
	}

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!shouldDraw() || !drawCondition()) return

		val reset = Vice.storage.summer.violetReset.timeDeltaUntil()
		if (reset.inWholeMilliseconds < 0) return

		val list = listOf(
			"Violet's Exchange".toText(Color(114, 89, 255), bold = true),
			"Resets in ".toText(Colors.ChatColor.Grey).append(reset.formatDuration().toText(Color(114, 89, 255)))
		)

		position.drawTexts(list, event.context)
	}

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		val list = listOf(
			"Violet's Exchange".toText(Color(114, 89, 255), bold = true),
			"Resets in ".toText(Colors.ChatColor.Grey).append(362.seconds.formatDuration().toText(Color(114, 89, 255)))
		)

		return position.drawTexts(list, context)
	}

	override fun storePosition(position: Position) {
		Vice.storage.summer.violetInfoPos = position
		Vice.storage.markDirty()
	}
}