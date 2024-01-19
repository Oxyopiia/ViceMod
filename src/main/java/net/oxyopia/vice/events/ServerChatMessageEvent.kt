package net.oxyopia.vice.events

import net.minecraft.text.Text

//	TODO - Make event Cancelable
class ServerChatMessageEvent(val content: Text) : ViceEvent() {
	val string: String = content.string
}