package net.oxyopia.vice.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.oxyopia.vice.events.BlockInteractEvent;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager {
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;contains(Lnet/minecraft/util/math/BlockPos;)Z"), method = "interactBlock", cancellable = true)
	private void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			Object modified = EVENT_MANAGER.publish(new BlockInteractEvent(player, hand, hitResult));

			if (modified instanceof ActionResult) {
				cir.setReturnValue((ActionResult) modified);
			}
		}
	}
}
