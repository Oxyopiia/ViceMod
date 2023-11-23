package net.oxyopia.vice.events

import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

class BlockInteractEvent(val player: ClientPlayerEntity, val hand: Hand, val hitResult: BlockHitResult, val cir: CallbackInfoReturnable<ActionResult>) : BaseEvent()