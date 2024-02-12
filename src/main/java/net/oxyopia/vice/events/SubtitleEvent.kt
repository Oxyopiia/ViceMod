package net.oxyopia.vice.events

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

class SubtitleEvent(val subtitle: String, val title: String, val callbackInfo: CallbackInfo) : ViceEvent()