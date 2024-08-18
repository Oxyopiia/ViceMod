package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ModifySoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.SoundUtils

object Fishing {
	private const val BITE_SOUND = "block.note_block.pling"
	private const val FISHING_PING = "entity.experience_orb.pickup"

	@SubscribeEvent
	fun onSound(event: ModifySoundEvent) {
		if (!Vice.config.FISHING_DING || event.soundName != FISHING_PING || event.volume != 3f) return
		val fishHook = MinecraftClient.getInstance().player?.fishHook ?: return
		if (!fishHook.isInOpenWater) return

		SoundUtils.playSound(BITE_SOUND, 1.4f, 3f)
		HudUtils.sendVanillaTitle("", "&&bBite!", 0.8f, 0.1f)

		event.cancel()
	}
}
