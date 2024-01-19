package net.oxyopia.vice.events

import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.oxyopia.vice.utils.Utils

class BlockInteractEvent(val player: ClientPlayerEntity, hand: Hand, hitResult: BlockHitResult) : ViceEvent.Cancelable<ActionResult>() {
	val itemStack: ItemStack = if (hand == Hand.MAIN_HAND) player.mainHandStack else player.offHandStack
	val block: Block = Utils.getWorld()?.getBlockState(hitResult.blockPos)?.block ?: Blocks.AIR
}