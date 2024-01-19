package net.oxyopia.vice.events

import net.minecraft.client.sound.SoundInstance
import net.oxyopia.vice.events.core.Returnable

@Returnable
class ModifySoundEvent(val sound: SoundInstance) : ViceEvent()