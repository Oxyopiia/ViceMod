package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.minecraft.entity.effect.StatusEffects
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.SubtitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object RevolverBlindnessHider {
	@SubscribeEvent
	fun onSubtitle(event: SubtitleEvent) {
		if (!Vice.config.HIDE_REVOLVER_BLINDNESS || !event.subtitle.contains("Left click to fire")) return
		val player = MinecraftClient.getInstance().player ?: return

		if (player.hasStatusEffect(StatusEffects.BLINDNESS)) player.removeStatusEffect(StatusEffects.BLINDNESS)
	}
}