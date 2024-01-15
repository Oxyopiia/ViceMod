package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.minecraft.entity.projectile.FishingBobberEntity
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.EntityVelocityPacketEvent
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.Utils

object Fishing {
	private const val MAX_SOUND_DEVIATION = 2.8
	private const val MIN_Y_VELOCITY_DETECTION = -200.0
	private const val MAX_SOUND_DETECTION_TIME: Long = 600

	private const val BITE_SOUND = "block.note_block.pling"
	private const val SPLASH_SOUND = "minecraft:entity.fishing_bobber.splash"

	private var lastDetectedSound: Long = -1
	private var lastHandled: Long = -1
	private var lastTouchedWater: Long = -1

	@SubscribeEvent
	fun onSound(event: SoundEvent) {
		if (!Vice.config.FISHING_DING || event.soundName != SPLASH_SOUND) return
		val fishHook = MinecraftClient.getInstance().player?.fishHook ?: return
		if (!fishHook.isTouchingWater) return

		val soundDeviation = fishHook.squaredDistanceTo(event.packet.x, event.packet.y, event.packet.z)
		DevUtils.sendDebugChat("Deviation of &&a$soundDeviation&&r blocks squared from Hook to Sound origin.", "FISHING_DEBUGGER")

		if (soundDeviation <= MAX_SOUND_DEVIATION) {
			lastDetectedSound = System.currentTimeMillis()
		}
	}

	@SubscribeEvent
	fun onVelocityUpdate(event: EntityVelocityPacketEvent) {
		val fishHook = MinecraftClient.getInstance().player?.fishHook ?: return
		if (!Vice.config.FISHING_DING || event.packet.id != fishHook.id) return

		val velocityY = event.packet.velocityY

		if (fishHook.isTouchingWater && lastTouchedWater > 0 && velocityY <= MIN_Y_VELOCITY_DETECTION) {
			DevUtils.sendDebugChat("Detected velocity of &&a$velocityY&&r", "FISHING_DEBUGGER")

			MinecraftClient.getInstance().executeTask {
				handleVelocityUpdate()
			}

		} else {
			updateLastTouchedWater(velocityY, fishHook)
		}
	}

	private fun handleVelocityUpdate() {
		val currentTime = System.currentTimeMillis()

		if (lastDetectedSound > 0 && currentTime - lastDetectedSound <= MAX_SOUND_DETECTION_TIME) {
			sendBiteNotification(currentTime)
			lastDetectedSound = -1
		} else if (Vice.config.FISHING_DING_DONT_DETECT_SOUND_PACKET && currentTime - lastHandled >= 1000) {
			sendBiteNotification(currentTime)
		}
	}

	private fun sendBiteNotification(timestamp: Long) {
		Utils.playSound(BITE_SOUND, 1.4f, 3f)
		HudUtils.sendVanillaTitle("", "&&bBite!", 0.8f, 0.1f)
		lastHandled = timestamp
	}

	private fun updateLastTouchedWater(velocityY: Int, fishHook: FishingBobberEntity) {
		if (lastTouchedWater == -1L && fishHook.isTouchingWater) {
			lastTouchedWater = System.currentTimeMillis()
			DevUtils.sendDebugChat("Set lastTouchedWater to &&a$lastTouchedWater", "FISHING_DEBUGGER")
		} else if (velocityY <= MIN_Y_VELOCITY_DETECTION && fishHook.age <= 30 && lastTouchedWater != 1L) {
			lastTouchedWater = -1
			DevUtils.sendDebugChat("Set lastTouchedWater to &&a-1", "FISHING_DEBUGGER")
		}
	}
}
