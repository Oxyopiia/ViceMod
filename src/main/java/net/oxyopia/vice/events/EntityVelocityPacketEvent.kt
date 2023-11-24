package net.oxyopia.vice.events

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket

class EntityVelocityPacketEvent(val packet: EntityVelocityUpdateS2CPacket) : BaseEvent()