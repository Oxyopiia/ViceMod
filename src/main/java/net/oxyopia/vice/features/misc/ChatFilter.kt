package net.oxyopia.vice.features.misc

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object ChatFilter {
	private var lastMessageString: String = ""

	@SubscribeEvent
	fun onChat(event: ChatEvent) {
		when {
			Vice.config.HIDE_WORLDGUARD_MESSAGES && event.string.startsWith("Hey! Sorry,") -> event.cancel()
			Vice.config.HIDE_SET_REQUIREMENT_MESSAGES && event.string.startsWith("You must be wearing at least") -> event.cancel()
			else -> lastMessageString = event.string
		}
	}
}