package net.oxyopia.vice.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.*;
//import net.oxyopia.vice.features.bosses.ShadowGelato;
import net.oxyopia.vice.events.BlockUpdatePacketEvent;
import net.oxyopia.vice.events.EntitySpawnPacketEvent;
import net.oxyopia.vice.events.SoundPacketEvent;
import net.oxyopia.vice.utils.DevUtils;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.oxyopia.vice.features.misc.Fishing;

import static net.oxyopia.vice.Vice.*;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

	@Inject(at = @At("HEAD"), method = "onPlaySound")
	private void onSound(PlaySoundS2CPacket packet, CallbackInfo callbackInfo) {
		if (client.isOnThread() && Utils.inDoomTowers()) {
			if (config.FISHING_DING && packet.getSound().value().getId().toString().equalsIgnoreCase("minecraft:entity.fishing_bobber.splash")) {
				Fishing.handleFishingSplash(packet);
			}

			EVENT_MANAGER.publish(new SoundPacketEvent(packet));

			DevUtils.sendDebugChat("&&bSOUND&&r " + packet.getSound().value().getId().toString() + " &&dP " + packet.getPitch() + " &&eV" + packet.getVolume(), "SEND_SOUND_INFO");
		}

	}

	@Inject(at = @At("HEAD"), method = "onEntityVelocityUpdate")
	private void fishingVelocityUpdate(EntityVelocityUpdateS2CPacket packet, CallbackInfo callbackInfo){
		if (client.isOnThread() && config.FISHING_DING && client.player != null && client.player.fishHook != null) {
			Fishing.handleVelocityUpdate(packet, client.player.fishHook);
		}
	}

	@Inject(at = @At("HEAD"), method = "onEntitySpawn")
	private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
		if (client.isOnThread() && Utils.inDoomTowers()) {
			EVENT_MANAGER.publish(new EntitySpawnPacketEvent(packet));
		}
	}
	
	@Inject(at = @At("HEAD"), method = "onBlockUpdate")
	private void shadowGelatoFeatures(BlockUpdateS2CPacket packet, CallbackInfo ci) {
		if (client.isOnThread() && Utils.inDoomTowers()) {
			EVENT_MANAGER.publish(new BlockUpdatePacketEvent(packet));
		}
	}
}