package net.oxyopia.vice.events

import net.minecraft.text.Text

class ChatEvent(content: Text) : ViceEvent.Cancelable<Boolean>() {
	val string: String = content.string

	val sender: String by lazy {
		string.substringBefore(":", "@Vice-NO_SENDER@").substringAfterLast(" ")
	}

	val hasNoSender by lazy {
		sender == "@Vice-NO_SENDER@"
	}

	override fun cancel() {
		setReturnValue(true)
	}
}