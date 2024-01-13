package net.oxyopia.vice.events

import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket

class SoundEvent(val packet: PlaySoundS2CPacket) : BaseEvent() {
	val soundName: String = packet.sound.value().id.path
	val pitch = packet.pitch
	val volume = packet.volume
}