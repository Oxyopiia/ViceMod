package net.oxyopia.vice.features.itemabilities

import net.minecraft.client.sound.SoundInstance
import net.oxyopia.vice.Vice.Companion.config

object AbilitySoundHider {
	// called by MixinSoundSystem, returns new volume
	fun onSound(sound: SoundInstance): Float {
		when {
			(sound.soundName() == "block.note_block.bit" || sound.soundName() == "entity.player.attack.crit") && ItemAbility.EIGHT_BIT_KATANA.remainingCooldown() >= 8f -> {
				return config.EIGHT_BIT_KATANA_VOLUME / 100f
			}

			(sound.soundName() == "entity.blaze.shoot" || sound.soundName() == "entity.generic.explode") && ItemAbility.BARBED_SHOTGUN.remainingCooldown() >= 4.25f -> {
				return config.BARBED_SHOTGUN_VOLUME / 100f
			}

			sound.soundName() == "entity.snowball.throw" && sound.volume == 9999f && ItemAbility.SNOWBALL_CANNON.remainingCooldown() >= 0.2f -> {
				return config.SNOWBALL_CANNON_VOLUME / 100f
			}

			(sound.soundName() == "entity.snowball.throw" || sound.soundName() == "entity.generic.explode") && sound.volume == 9999f && ItemAbility.ARCTIC_CORE.remainingCooldown() > 0f -> {
				return config.ARCTIC_CORE_VOLUME / 100f
			}

			(sound.soundName() == "block.note_block.bit" || sound.soundName() == "entity.generic.explode") && ItemAbility.LASER_POINT_MINIGUN.remainingCooldown() > 0.05f -> {
				return config.LASER_POINT_MINIGUN_VOLUME / 100f
			}

			(sound.soundName() == "entity.bat.takeoff" || sound.soundName() == "block.beacon.activate" || sound.soundName() == "entity.wither.break_block") && sound.volume == 9999f && ItemAbility.BEDROCK_BREAKER.remainingCooldown() >= 1f -> {
				return config.BEDROCK_BREAKER_VOLUME / 100f
			}
		}

		return -1f
	}

	private fun SoundInstance.soundName(): String = this.id.path
}