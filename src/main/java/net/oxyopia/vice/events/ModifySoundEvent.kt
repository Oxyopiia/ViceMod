package net.oxyopia.vice.events

import net.minecraft.client.sound.SoundInstance
import net.oxyopia.vice.events.core.Cancelable

@Cancelable
class ModifySoundEvent(val sound: SoundInstance) : BaseEvent()