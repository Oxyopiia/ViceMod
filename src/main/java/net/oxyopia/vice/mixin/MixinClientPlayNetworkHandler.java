package net.oxyopia.vice.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
//import net.oxyopia.vice.features.bosses.ShadowGelato;
import net.oxyopia.vice.features.itemabilities.ItemAbilityCooldown;
import net.oxyopia.vice.utils.DevUtils;
import net.oxyopia.vice.utils.Utils;
import net.oxyopia.vice.utils.enums.Worlds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.oxyopia.vice.features.Fishing;

import java.util.Objects;

import static net.oxyopia.vice.Vice.*;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

	@Inject(at = @At("HEAD"), method = "onPlaySound")
	private void onSound(PlaySoundS2CPacket packet, CallbackInfo callbackInfo) {
		if (client.isOnThread()) {
			if (config.FISHING_DING && packet.getSound().value().getId().toString().equalsIgnoreCase("minecraft:entity.fishing_bobber.splash")) {
				Fishing.handleFishingSplash(packet);
			}
			if (config.ITEM_COOLDOWN_DISPLAY) ItemAbilityCooldown.Companion.onSound(packet);

			DevUtils.sendDebugChat("&&bSOUND&&r " + packet.getSound().value().getId().toString() + " &&dP " + packet.getPitch() + " &&eV" + packet.getVolume(), "SEND_SOUND_INFO");
		}

	}

	@Inject(at = @At("HEAD"), method = "onEntityVelocityUpdate")
	private void fishingVelocityUpdate(EntityVelocityUpdateS2CPacket packet, CallbackInfo callbackInfo){
		if (client.isOnThread() && config.FISHING_DING && client.player != null && client.player.fishHook != null) {
			Fishing.handleVelocityUpdate(packet, client.player.fishHook);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "onBlockUpdate")
	private void shadowGelatoFeatures(BlockUpdateS2CPacket packet, CallbackInfo ci) {
		if (client.isOnThread() && config.SHADOWGELATO_AMETHYST_WARNING && Objects.equals(Utils.getWorld(), Worlds.ShadowGelato.getId())) {
//			ShadowGelato.handleBlockUpdate(packet);
		}
	}
}