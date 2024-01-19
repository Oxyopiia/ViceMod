package net.oxyopia.vice.events

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket

class BlockUpdatePacketEvent(packet: BlockUpdateS2CPacket) : ViceEvent() {
	private val state: BlockState = packet.state
	val block: Block = state.block
}