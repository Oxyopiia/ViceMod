package net.oxyopia.vice.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.oxyopia.vice.features.itemabilities.ItemAbilityCooldown;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.oxyopia.vice.Vice.client;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

	@Inject(at = @At("HEAD"), method = "doAttack")
	private void onAttack(CallbackInfoReturnable<Boolean> cir) {
		ItemAbilityCooldown.Companion.onLeftClick(client.player != null ? client.player.getMainHandStack() : ItemStack.EMPTY);
	}

	@Inject(at = @At("HEAD"), method = "doItemUse")
	private void onUse(CallbackInfo ci) {
		ItemAbilityCooldown.Companion.onRightClick(client.player != null ? client.player.getMainHandStack() : ItemStack.EMPTY);
	}
}