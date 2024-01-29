package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ModifySoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.Utils

object Fishing {
	private const val BITE_SOUND = "block.note_block.pling"
	private const val FISHING_PING = "entity.experience_orb.pickup"

	@SubscribeEvent
	fun onSound(event: ModifySoundEvent) {
		if (!Vice.config.FISHING_DING || event.soundName != FISHING_PING) return
		val fishHook = MinecraftClient.getInstance().player?.fishHook ?: return
		if (!fishHook.isInOpenWater) return

		sendBiteNotification()
		event.cancel()
	}

	/*
	 If server-side Fishing Ding becomes toggleable in the future, please see to the following file to re-integrate velocity detection:

	 https://github.com/Oxyopiia/ViceMod/blob/1.0-Beta5/src/main/java/net/oxyopia/vice/features/misc/Fishing.kt
	*/

	private fun sendBiteNotification() {
		Utils.playSound(BITE_SOUND, 1.4f, 3f)
		HudUtils.sendVanillaTitle("", "&&bBite!", 0.8f, 0.1f)
	}
}
