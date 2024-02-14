package net.oxyopia.vice.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.network.packet.s2c.play.*;
import net.oxyopia.vice.events.BlockUpdateEvent;
import net.oxyopia.vice.events.EntityDeathEvent;
import net.oxyopia.vice.events.EntitySpawnEvent;
import net.oxyopia.vice.events.EntityVelocityEvent;
import net.oxyopia.vice.events.SoundEvent;
import net.oxyopia.vice.utils.DevUtils;
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
			EVENT_MANAGER.publish(new SoundEvent(packet));

			DevUtils.sendDebugChat("&&bSOUND&&r " + packet.getSound().value().getId().toString() + " &&dP " + packet.getPitch() + " &&eV" + packet.getVolume(), "SEND_SOUND_INFO");
		}

	}

	@Inject(at = @At("HEAD"), method = "onEntityVelocityUpdate")
	private void onEntityVelocityUpdate(EntityVelocityUpdateS2CPacket packet, CallbackInfo callbackInfo){
		if (MinecraftClient.getInstance().isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new EntityVelocityEvent(packet));
		}
	}

	@Inject(at = @At("HEAD"), method = "playSpawnSound")
	private void onEntitySpawn(Entity entity, CallbackInfo ci) {
		if (MinecraftClient.getInstance().isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new EntitySpawnEvent(entity));

			DevUtils.sendDebugChat("&&aENTITY SPAWN&&r " + entity.getName().getString(), "MOB_SPAWN_DEBUGGER");
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
	
	@Inject(at = @At("HEAD"), method = "onBlockUpdate")
	private void onBlockUpdate(BlockUpdateS2CPacket packet, CallbackInfo ci) {
		if (MinecraftClient.getInstance().isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new BlockUpdateEvent(packet));
		}
	}
}