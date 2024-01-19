package net.oxyopia.vice.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.oxyopia.vice.events.ClientTickEvent;
import net.oxyopia.vice.events.EntityGlowEvent;
import net.oxyopia.vice.events.LeftClickEvent;
import net.oxyopia.vice.events.RightClickEvent;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

	@Inject(at = @At("HEAD"), method = "doAttack")
	private void onAttack(CallbackInfoReturnable<Boolean> cir) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new LeftClickEvent());
		}
	}

	@Inject(at = @At("HEAD"), method = "doItemUse")
	private void onUse(CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new RightClickEvent());
		}
	}

	@Unique private int tickTotal = 0;

	@Inject(at = @At("HEAD"), method = "tick")
	private void onClientTick(CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			tickTotal++;
			EVENT_MANAGER.publish(new ClientTickEvent(tickTotal));
		}
	}

	@Redirect(at = @At(value="INVOKE", target="Lnet/minecraft/entity/Entity;isGlowing()Z"), method = "hasOutline")
	private boolean updateGlowing(Entity entity) {
		MinecraftClient client = MinecraftClient.getInstance();
		boolean shouldGlowDefault = entity.isGlowing() || client.player != null && client.player.isSpectator() && client.options.spectatorOutlinesKey.isPressed() && entity.getType() == EntityType.PLAYER;

		if (Utils.INSTANCE.getInDoomTowers()) {
			EntityGlowEvent result = EVENT_MANAGER.publish(new EntityGlowEvent(entity));

			if (result.getReturnValue() != null) {
				return result.getReturnValue();
			}
		}

		return shouldGlowDefault;
	}
}