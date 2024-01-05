package net.oxyopia.vice.events

import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket

class SoundPacketEvent(val packet: PlaySoundS2CPacket) : BaseEvent()