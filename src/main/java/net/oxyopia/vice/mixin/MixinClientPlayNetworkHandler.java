package net.oxyopia.vice.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.oxyopia.vice.features.Fishing;
import static net.oxyopia.vice.Vice.*;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

	@Inject(at = @At("HEAD"), method = "onPlaySound", cancellable = true)
	private void onPlaySound(PlaySoundS2CPacket packet, CallbackInfo callbackInfo) {
		if (client.isOnThread() && packet.getSound().value().getId().toString().equalsIgnoreCase("minecraft:entity.fishing_bobber.splash")) {
			if (config.FISHING_DING) {
				Fishing.handleFishingSplash(packet);
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "onEntityVelocityUpdate")
	private void onEntityVelocityUpdate(EntityVelocityUpdateS2CPacket packet, CallbackInfo callbackInfo){
		if (client.isOnThread() && config.FISHING_DING) {
			if (client.player != null && client.player.fishHook != null) {
				Fishing.handleVelocityUpdate(packet, client.player.fishHook);
			}
		}
	}
}
