package net.oxyopia.vice.features.misc

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ServerChatMessageEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object ChatFilter {
	private var lastMessageString: String = ""

	@SubscribeEvent
	fun onChat(event: ServerChatMessageEvent) {
		if (Vice.config.HIDE_WORLDGUARD_MESSAGES && event.string.startsWith("Hey! Sorry,")) {
			event.cancel()
			return
		}

		else if (Vice.config.HIDE_SET_REQUIREMENT_MESSAGES && event.string.startsWith("You must be wearing at least")) {
			event.cancel()
			return
		}

		lastMessageString = event.string
	}
}