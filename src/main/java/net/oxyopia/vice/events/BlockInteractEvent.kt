package net.oxyopia.vice.events

import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.oxyopia.vice.events.core.Returnable

@Returnable
class BlockInteractEvent(val player: ClientPlayerEntity, val hand: Hand, val hitResult: BlockHitResult) : ViceEvent.Cancelable<Boolean>() {
	val itemStack: ItemStack = if (hand == Hand.MAIN_HAND) player.mainHandStack else player.offHandStack
}