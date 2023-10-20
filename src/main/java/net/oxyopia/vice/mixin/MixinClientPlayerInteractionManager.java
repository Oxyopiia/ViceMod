package net.oxyopia.vice.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.oxyopia.vice.Vice.config;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager {
	@Inject(at = @At("HEAD"), method = "interactBlock", cancellable = true)
	private void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		ItemStack stack = (hand == Hand.MAIN_HAND) ? player.getMainHandStack() : player.getOffHandStack();

		if (stack.getItem() == Items.PLAYER_HEAD && config.PREVENT_PLACING_PLAYER_HEADS) {
			cir.setReturnValue(ActionResult.PASS);
		}
	}
}
