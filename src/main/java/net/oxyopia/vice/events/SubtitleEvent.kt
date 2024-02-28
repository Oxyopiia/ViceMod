package net.oxyopia.vice.events

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

class SubtitleEvent(val subtitle: String, val title: String, private val callbackInfo: CallbackInfo) : ViceEvent.Cancelable<Boolean>() {
	override fun cancel() {
		callbackInfo.cancel()
	}
}