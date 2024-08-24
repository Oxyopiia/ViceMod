package net.oxyopia.vice.features.summer

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.MutableText
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawTexts
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.timeDeltaUntil
import java.awt.Color
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object SummerTimers : HudElement(
	"Summer Timers",
	Vice.storage.summer.violetInfoPos,
	{ Vice.storage.summer.violetInfoPos = it },
	enabled = { Vice.config.SUMMER_TIMERS },
	drawCondition = { World.Summer.isInWorld() }
){
	private val storage get() = Vice.storage.summer

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!canDraw()) return

		val list = mutableListOf(
			"Summer Timers".toText(Vice.PRIMARY, bold = true)
		)

		list.addTimer("Violet's Exchange", storage.violetReset, Color(114, 89, 255))
		list.addTimer("Ice Cream Minigame", storage.iceCreamReset, Color(251, 169, 246))
		list.addTimer("Bar Minigame", storage.barReset, Color(122, 252, 133))

		if (list.size == 1) {
			list.add("Start a minigame or open Violet's shop!".toText(Colors.ChatColor.Red))
		}

		position.drawTexts(list, event.context)
	}

	private fun MutableList<MutableText>.addTimer(title: String, reset: Long, color: Color) {
		if (reset < 0) return
		addTimerInternal(title, reset.timeDeltaUntil(), color)
	}

	private fun MutableList<MutableText>.addTimerInternal(title: String, delta: Duration, color: Color) {
		val text = title.toText(color)

		if (delta.inWholeMilliseconds < 0) {
			add(text.append(" Ready!".toText(Colors.ChatColor.Green)))
		} else {
			add(text.append(" Ready in ".toText(Colors.ChatColor.Grey).append(delta.formatDuration().toText(color))))
		}
	}

	override fun Position.drawPreview(context: DrawContext): Size {
		val list = mutableListOf(
			"Summer Timers".toText(Vice.PRIMARY, bold = true),
		)

		list.addTimerInternal("Violet's Exchange", 362.seconds, Color(114, 89, 255))
		list.addTimerInternal("Ice Cream Minigame", 22.minutes, Color(251, 169, 246))
		list.addTimerInternal("Bar Minigame", 19.minutes.plus(52.seconds), Color(122, 252, 133))

		return position.drawTexts(list, context)
	}
}