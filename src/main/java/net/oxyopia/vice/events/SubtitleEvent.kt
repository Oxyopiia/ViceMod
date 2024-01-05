package net.oxyopia.vice.events

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

class SubtitleEvent(val subtitle: String, val callbackInfo: CallbackInfo) : BaseEvent()