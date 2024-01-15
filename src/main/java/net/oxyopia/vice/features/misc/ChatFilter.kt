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

		lastMessageString = event.string
	}
}