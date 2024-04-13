package net.oxyopia.vice.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.oxyopia.vice.Vice;
import net.oxyopia.vice.events.EntityIsChildEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
	@Shadow public abstract ItemStack getMainHandStack();

	@Inject(at = @At("HEAD"), method = "isBaby", cancellable = true)
	private void isBaby(CallbackInfoReturnable<Boolean> cir) {
		EntityIsChildEvent result = EVENT_MANAGER.publish(new EntityIsChildEvent());

		if (result.isCanceled() || Vice.config.DEV_BABY_MODE) cir.setReturnValue(true);
	}
}