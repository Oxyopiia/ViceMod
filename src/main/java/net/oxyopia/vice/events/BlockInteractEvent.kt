package net.oxyopia.vice.events

import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.oxyopia.vice.events.core.Returnable

@Returnable
class BlockInteractEvent(val player: ClientPlayerEntity, val hand: Hand, val hitResult: BlockHitResult) : BaseEvent()