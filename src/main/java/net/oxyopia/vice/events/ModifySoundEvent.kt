package net.oxyopia.vice.events

import net.minecraft.client.sound.SoundInstance

class ModifySoundEvent(val sound: SoundInstance) : ViceEvent.Cancelable<Float>() {
	val soundName: String = sound.id.path
	val pitch = sound.pitch
	val volume = sound.volume
	val x = sound.x
	val y = sound.y
	val z = sound.z

	override fun cancel() {
		setReturnValue(0f)
	}
}
