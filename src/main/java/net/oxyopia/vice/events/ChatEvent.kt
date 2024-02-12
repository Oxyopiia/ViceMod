package net.oxyopia.vice.events

import net.minecraft.text.Text

class ChatEvent(content: Text) : ViceEvent.Cancelable<Boolean>() {
	val string: String = content.string

	override fun cancel() {
		setReturnValue(true)
	}
}