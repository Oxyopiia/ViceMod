package net.oxyopia.vice.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier

object SoundUtils {
	private val client = MinecraftClient.getInstance()

	fun playSound(identifier: Identifier, pitch: Float = 1f, volume: Float = 1f) {
		try {
			client.soundManager.play(PositionedSoundInstance.master(SoundEvent.of(identifier), pitch, volume))
		} catch (err: Exception) {
			DevUtils.sendErrorMessage(err, "An error occurred attempting to play a sound")
		}
	}

	fun playSound(string: String, pitch: Float = 1f, volume: Float = 1f) {
		var namespace = "minecraft"
		var name = string

		if(string.contains(":")) {
			val parts = string.split(":")
			namespace = parts[0]
			name = parts[1]
		}

		val identifier = Identifier.of(namespace, name) ?: return
		playSound(identifier, pitch, volume)
	}

	fun playDing() {
		playSound("entity.arrow.hit_player")
	}

	fun playFail() {
		playSound("entity.arrow.hit_player", 0.2f)
	}
}