package net.oxyopia.vice.events

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

class TitleEvent(val title: String, val callbackInfo: CallbackInfo) : ViceEvent()
