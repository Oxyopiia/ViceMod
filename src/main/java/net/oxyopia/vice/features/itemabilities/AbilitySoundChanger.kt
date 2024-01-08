package net.oxyopia.vice.features.itemabilities

import net.minecraft.client.sound.SoundInstance
import net.oxyopia.vice.Vice.Companion.config
import net.oxyopia.vice.events.ModifySoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object AbilitySoundChanger {
	@SubscribeEvent
	fun onSound(event: ModifySoundEvent) {
		val sound = event.sound

		when {
			(sound.soundName() == "block.note_block.bit" || sound.soundName() == "entity.player.attack.crit") && ItemAbility.EIGHT_BIT_KATANA.remainingCooldown() >= 8f -> {
				event.returnValue = config.EIGHT_BIT_KATANA_VOLUME
			}

			(sound.soundName() == "entity.blaze.shoot" || sound.soundName() == "entity.generic.explode") && ItemAbility.BARBED_SHOTGUN.remainingCooldown() >= 4.25f -> {
				event.returnValue = config.BARBED_SHOTGUN_VOLUME
			}

			sound.soundName() == "entity.snowball.throw" && sound.volume == 9999f && ItemAbility.SNOWBALL_CANNON.remainingCooldown() >= 0.2f -> {
				event.returnValue = config.SNOWBALL_CANNON_VOLUME
			}

			(sound.soundName() == "entity.snowball.throw" || sound.soundName() == "entity.generic.explode") && sound.volume == 9999f && ItemAbility.ARCTIC_CORE.remainingCooldown() > 0f -> {
				event.returnValue = config.ARCTIC_CORE_VOLUME
			}

			(sound.soundName() == "block.note_block.bit" || sound.soundName() == "entity.generic.explode") && ItemAbility.LASER_POINT_MINIGUN.remainingCooldown() > 0.05f -> {
				event.returnValue = config.LASER_POINT_MINIGUN_VOLUME
			}

			(sound.soundName() == "entity.bat.takeoff" || sound.soundName() == "block.beacon.activate" || sound.soundName() == "entity.wither.break_block") && sound.volume == 9999f && ItemAbility.BEDROCK_BREAKER.remainingCooldown() >= 1f -> {
				event.returnValue = config.BEDROCK_BREAKER_VOLUME
			}
		}
	}

	private fun SoundInstance.soundName(): String = this.id.path
}