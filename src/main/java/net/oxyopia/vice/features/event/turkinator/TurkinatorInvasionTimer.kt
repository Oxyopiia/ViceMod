package net.oxyopia.vice.features.event.turkinator

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.SoundUtils
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object TurkinatorInvasionTimer : HudElement(
	"Turkinator Invasion Timer",
	Vice.storage.event.turkinator.invasionTimerPos,
	{ Vice.storage.event.turkinator.invasionTimerPos = it },
	enabled = { Vice.config.TURKINATOR_INVASION_TIMER },
) {
	private val SPAWN_COOLDOWN = 30.minutes
	private val SPAWN_MESSAGE = Regex("Turkinator has Invaded (.+)!")
	private var lastKnownSpawnTime: Long = 0
	private var lastKnownLocation: String? = null

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!canDraw()) return

		val list: MutableList<String> = mutableListOf()

		val diff = lastKnownSpawnTime.timeDelta()
		val remainingTime = SPAWN_COOLDOWN.inWholeSeconds - diff.inWholeSeconds
		val formatted = remainingTime.seconds.formatDuration()

		if (lastKnownLocation != null) {
			list.add("§6Last Location: §e$lastKnownLocation")
		} else {
			list.add("§6Last Location: §cUnknown")
		}

		if (remainingTime > 0) {
			list.add("§6Next Turkinator in: §a${formatted}")
		} else {
			list.add("§6Turkinator is ready to invade!")
		}

		position.drawStrings(list, event.context)
	}

	@SubscribeEvent
	fun onChatEvent(event: ChatEvent) {
		SPAWN_MESSAGE.find(event.string)?.apply {
			if (Vice.config.TURKINATOR_INVASION_TIMER) SoundUtils.playSound("block.bell.use", volume = 1.0f)
			lastKnownSpawnTime = System.currentTimeMillis()
			lastKnownLocation = groupValues[1]
			Vice.storage.markDirty()
		}
	}

	override fun Position.drawPreview(context: DrawContext): Size {
		val list = listOf(
			"§6Last Location: §eThe Lost Temple",
			"§6Next Turkinator in: §a13:56"
		)
		return position.drawStrings(list, context)
	}
}