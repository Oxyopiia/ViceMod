package net.oxyopia.vice.events

import net.minecraft.text.Text
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

class SubtitleEvent(val rawSubtitle: Text, title: Text, private val callbackInfo: CallbackInfo) : ViceEvent.Cancelable<Boolean>() {
	val subtitle: String = rawSubtitle.string
	val title: String = title.string

	override fun cancel() {
		callbackInfo.cancel()
	}
}