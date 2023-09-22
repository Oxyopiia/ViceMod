package net.oxyopia.vice.features;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.oxyopia.vice.utils.DevUtils;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.*;

public class Fishing {
	public static final double MAX_SOUND_DEVIATION = 2.8;
	public static final double MIN_Y_VELOCITY_DETECTION = -500;
	public static final long MAX_SOUND_DETECTION_TIME = 600;

	private static long lastDetectedSound = -1;
	private static long lastHandled = -1;
	private static long lastTouchedWater = -1;

	public static void handleFishingSplash(PlaySoundS2CPacket packet) {
		if (client.player != null && client.player.fishHook != null && client.player.fishHook.isTouchingWater()) {
			FishingBobberEntity fishHook = client.player.fishHook;
			double soundDeviation = fishHook.squaredDistanceTo(packet.getX(), packet.getY(), packet.getZ());

			DevUtils.sendDebugChat("Deviation of &&a" + soundDeviation + "&&r blocks squared from Hook to Sound origin.");

			if (soundDeviation <= MAX_SOUND_DEVIATION) {
				if (config.FISHING_DING) {
					// Saves the timestamp that the sound packet was detected at for handleVelocityUpdate to detect if it has caught or is just falling
					lastDetectedSound = System.currentTimeMillis();
				}
			}
		}
	}

	public static void handleVelocityUpdate(EntityVelocityUpdateS2CPacket packet, FishingBobberEntity fishHook) {
		if (packet.getId() == fishHook.getId()) {
			int velocityY = packet.getVelocityY();

			if (fishHook.isTouchingWater() && lastTouchedWater > 0 && velocityY <= MIN_Y_VELOCITY_DETECTION) {
				DevUtils.sendDebugChat("Detected velocity of &&a" + velocityY + "&&r");

				client.executeTask(() -> {
					long currentTime = System.currentTimeMillis();

					if (lastDetectedSound > 0 && currentTime - lastDetectedSound <= MAX_SOUND_DETECTION_TIME) {
						client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), SoundCategory.BLOCKS, 3F, 1.4F);
						lastHandled = currentTime;
						lastDetectedSound = -1;

					} else if (config.FISHING_DING_DONT_DETECT_SOUND_PACKET && currentTime - lastHandled >= 1000) {
						client.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), SoundCategory.BLOCKS, 3F, 1.4F);
						lastHandled = currentTime;
					}
				});

			} else {
				if (lastTouchedWater == -1 && fishHook.isTouchingWater()) {
					lastTouchedWater = System.currentTimeMillis();
					DevUtils.sendDebugChat("Set lastTouchedWater to &&a" + lastTouchedWater);
				} else if (velocityY <= MIN_Y_VELOCITY_DETECTION && fishHook.age <= 30) {
					lastTouchedWater = -1;
					DevUtils.sendDebugChat("Set lastTouchedWater to &&a-1");
				}
			}
		}
	}
}