package net.oxyopia.vice.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.oxyopia.vice.Vice;
import net.oxyopia.vice.events.ItemDropEvent;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends MixinLivingEntity {
	@Inject(at = @At("HEAD"), method = "dropSelectedItem", cancellable = true)
	private void onDrop(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
		if (!Utils.INSTANCE.getInDoomTowers()) return;

		ItemDropEvent result = Vice.EVENT_MANAGER.publish(new ItemDropEvent(getMainHandStack()));

		if (result.isCanceled()) {
			cir.cancel();
		}
	}
}