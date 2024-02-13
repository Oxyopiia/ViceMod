package net.oxyopia.vice.events

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket
import net.minecraft.util.math.BlockPos

class BlockUpdateEvent(packet: BlockUpdateS2CPacket) : ViceEvent() {
	val blockState: BlockState = packet.state
	val block: Block = blockState.block
	val pos: BlockPos = packet.pos
}