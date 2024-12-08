package net.oxyopia.vice.features.itemabilities

import net.oxyopia.vice.Vice.Companion.config
import net.oxyopia.vice.events.ModifySoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object AbilitySoundChanger {
	@SubscribeEvent
	fun onSound(event: ModifySoundEvent) {
		val sound = event.soundName
		val volume = event.volume

		when {
			(sound == "block.note_block.bit" || sound == "entity.player.attack.crit") && ItemAbility.EIGHT_BIT_KATANA.remainingCooldown() >= 8f -> {
				event.setReturnValue(config.EIGHT_BIT_KATANA_VOLUME)
			}

			(sound == "entity.blaze.shoot" || sound == "entity.generic.explode") && ItemAbility.BARBED_SHOTGUN.remainingCooldown() >= 4.25f -> {
				event.setReturnValue(config.BARBED_SHOTGUN_VOLUME)
			}

			sound == "entity.snowball.throw" && volume == 9999f && ItemAbility.SNOWBALL_CANNON.remainingCooldown() >= 0.2f -> {
				event.setReturnValue(config.SNOWBALL_CANNON_VOLUME)
			}

			(sound == "entity.snowball.throw" || sound == "entity.generic.explode") && volume == 9999f && ItemAbility.ARCTIC_CORE.isOnCooldown() -> {
				event.setReturnValue(config.ARCTIC_CORE_VOLUME)
			}

			(sound == "block.note_block.bit" || sound == "entity.generic.explode") && ItemAbility.LASER_POINT_MINIGUN.remainingCooldown() > 0.05f -> {
				event.setReturnValue(config.LASER_POINT_MINIGUN_VOLUME)
			}

			(sound == "entity.bat.takeoff" || sound == "block.beacon.activate" || sound == "entity.wither.break_block") && volume == 9999f && ItemAbility.BEDROCK_BREAKER.remainingCooldown() >= 1f -> {
				event.setReturnValue(config.BEDROCK_BREAKER_VOLUME)
			}

			sound == "block.respawn_anchor.deplete" && ItemAbility.GLITCH_MALLET.remainingCooldown() >= 4f -> {
				event.setReturnValue(config.GLITCH_MALLET_VOLUME)
			}

			(sound == "entity.guardian.attack" || sound == "block.conduit.activate") && ItemAbility.JYNX_CHAIN_GUN.isOnCooldown() -> {
				event.setReturnValue(config.JYNX_CHAIN_GUN_VOLUME)
			}

			sound == "entity.illusioner.cast_spell" && ItemAbility.SHADOW_GELATO_DRUM_GUN.isOnCooldown() -> {
				event.setReturnValue(config.SHADOW_DRUM_GUN_VOLUME)
			}

			volume == 3f && (
					sound == "block.beacon.activate" ||
					sound == "item.shield.block" ||
					sound == "entity.player.hurt_freeze" ||
					sound == "entity.elder_guardian.curse" ||
					sound == "item.trident.riptide_1" ||
					(sound == "entity.evoker_fangs.attack" && config.APPLY_WAVE_PULSER_VOLUME_TO_FANGS)
			) && ItemAbility.WAVE_PULSER.remainingCooldown() >= 7f -> {
				event.setReturnValue(config.WAVE_PULSER_VOLUME)
			}
		}
	}
}