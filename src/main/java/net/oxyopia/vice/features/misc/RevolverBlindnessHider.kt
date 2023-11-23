package net.oxyopia.vice.features.misc

import net.minecraft.entity.effect.StatusEffects
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.SubtitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object RevolverBlindnessHider {
	@SubscribeEvent
	fun onSubtitle(event: SubtitleEvent) {
		if (Vice.config.HIDE_REVOLVER_BLINDNESS && event.subtitle.contains("Left click to fire") && Vice.client.player != null) {
			if (Vice.client.player?.hasStatusEffect(StatusEffects.BLINDNESS) == true) {
				Vice.client.player?.removeStatusEffect(StatusEffects.BLINDNESS)
			} else if (Vice.client.player?.hasStatusEffect(StatusEffects.DARKNESS) == true) {
				Vice.client.player?.removeStatusEffect(StatusEffects.DARKNESS)
			}
		}
	}
}