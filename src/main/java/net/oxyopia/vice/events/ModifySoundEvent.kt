package net.oxyopia.vice.events

import net.minecraft.client.sound.SoundInstance

class ModifySoundEvent(val sound: SoundInstance) : ViceEvent.Cancelable<Float>()