package net.oxyopia.vice.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.network.packet.s2c.play.*;
import net.oxyopia.vice.data.Debugger;
import net.oxyopia.vice.events.*;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.*;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

	@Inject(at = @At("HEAD"), method = "onPlaySound")
	private void onSound(PlaySoundS2CPacket packet, CallbackInfo callbackInfo) {
		if (MinecraftClient.getInstance().isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			String path = packet.getSound().value().getId().getPath();
			float pitch = packet.getPitch();
			float volume = packet.getVolume();

			EVENT_MANAGER.publish(new SoundEvent(path, pitch, volume));

			Debugger.SOUND.debug(path + " §dP " + pitch + " §eV" + volume);
		}

	}

	@Inject(at = @At("HEAD"), method = "onEntityVelocityUpdate")
	private void onEntityVelocityUpdate(EntityVelocityUpdateS2CPacket packet, CallbackInfo callbackInfo){
		if (MinecraftClient.getInstance().isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new EntityVelocityPacketEvent(packet));
		}
	}

	@Inject(at = @At("HEAD"), method = "playSpawnSound")
	private void onEntitySpawn(Entity entity, CallbackInfo ci) {
		if (MinecraftClient.getInstance().isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new EntitySpawnEvent(entity));

			Debugger.MOB.debug(entity.getName().getString(), "SPAWN");
		}
	}
	
	@Inject(at = @At("HEAD"), method = "onEntityStatus")
	private void onEntityStatusUpdate(EntityStatusS2CPacket packet, CallbackInfo ci) {
		if (MinecraftClient.getInstance().isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			if (packet.getStatus() == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
				Entity entity = packet.getEntity(Utils.INSTANCE.getWorld());

				if (entity != null) EVENT_MANAGER.publish(new EntityDeathEvent(entity));
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "onDeathMessage")
	private void onDeathMessage(DeathMessageS2CPacket packet, CallbackInfo ci) {
		if (MinecraftClient.getInstance().isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new PlayerDeathEvent(packet));
		}
	}
}