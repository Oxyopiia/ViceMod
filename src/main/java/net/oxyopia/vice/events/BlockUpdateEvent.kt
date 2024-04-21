package net.oxyopia.vice.events

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos

class BlockUpdateEvent(
	val pos: BlockPos,
	val new: BlockState,
) : ViceEvent()