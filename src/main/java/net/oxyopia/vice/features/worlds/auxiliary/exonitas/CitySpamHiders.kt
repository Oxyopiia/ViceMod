package net.oxyopia.vice.features.worlds.auxiliary.exonitas

import net.minecraft.client.MinecraftClient
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.SubtitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object CitySpamHiders {
	@SubscribeEvent
	fun onTitle(event: SubtitleEvent) {
		if (!Vice.config.HIDE_BLOAT_EXONITAS_MESSAGES || !World.Exonitas.isInWorld() || !event.title.startsWith("LEVEL ")) return

		MinecraftClient.getInstance().inGameHud.clearTitle()
	}

	@SubscribeEvent
	fun onChat(event: ChatEvent) {
		if (!Vice.config.HIDE_BLOAT_EXONITAS_MESSAGES || !World.Exonitas.isInWorld()) return

		val s = event.string
		if (
			s.startsWith("Cameras") ||
			s.startsWith("Marked Blocks") ||
			s.startsWith("Blocks marked") ||
			s.startsWith("Eyes") ||
			s.startsWith("Power Boxes") ||
			s.startsWith("Hitting a power box") ||
			s.startsWith("Quantumizer")
		) event.cancel()
	}
}