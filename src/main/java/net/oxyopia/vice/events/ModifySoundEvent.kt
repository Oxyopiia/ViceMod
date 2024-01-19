package net.oxyopia.vice.events

import net.minecraft.client.sound.SoundInstance

// TODO - Make event Returnable with Float Type
class ModifySoundEvent(val sound: SoundInstance) : ViceEvent()