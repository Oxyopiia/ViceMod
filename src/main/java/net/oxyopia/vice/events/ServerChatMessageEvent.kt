package net.oxyopia.vice.events

import net.minecraft.text.Text
import net.oxyopia.vice.events.core.Cancelable

@Cancelable
class ServerChatMessageEvent(val content: Text) : ViceEvent() {
	val string: String = content.string
}