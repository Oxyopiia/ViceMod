package net.oxyopia.vice.events

import net.minecraft.text.Text

class ServerChatMessageEvent(content: Text) : ViceEvent.Cancelable<Boolean>() {
	val string: String = content.string
}