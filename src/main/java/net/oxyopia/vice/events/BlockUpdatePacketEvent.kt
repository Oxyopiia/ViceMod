package net.oxyopia.vice.events

import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket

class BlockUpdatePacketEvent(val packet: BlockUpdateS2CPacket) : BaseEvent()