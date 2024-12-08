package net.oxyopia.vice.features.worlds.expeditions

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Debugger
import net.oxyopia.vice.events.EntityShouldRenderEvent
import net.oxyopia.vice.events.ModifySoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object StylePointsHider {
	private val stylePoints = listOf(
		"Cʀɪᴛɪᴄᴀʟ!",
		"Sʜᴀʀᴘsʜᴏᴏᴛᴇʀ!",
		"Mᴀsᴛᴇʀ!",
		"Dᴇsᴛʀᴏʏᴇʀ!",
		"Rᴇᴠᴇɴɢᴇ!",
		"Hᴇᴀᴅsʜᴏᴛ!"
	)

	@SubscribeEvent
	fun onEntityTryRender(event: EntityShouldRenderEvent) {
		if (!Vice.config.HIDE_EXPEDITION_STYLE_POINTS || ExpeditionAPI.isInExpedition()) return

		val entity = event.entity as? TextDisplayEntity ?: return
		val text = entity.data?.text?.string ?: return

		if (stylePoints.contains(text)) {
			event.setReturnValue(false)
		} else {
			Debugger.EXPEDITIONS.warn("Detected some weird text display: &&f$text")
		}
	}

	@SubscribeEvent
	fun onSound(event: ModifySoundEvent) {
		if (!Vice.config.HIDE_EXPEDITION_STYLE_POINTS || ExpeditionAPI.isInExpedition()) return

		if (event.soundName == "entity.experience_orb.pickup" && event.pitch >= 1.0f && event.volume == 3f) {
			event.cancel()
		}
	}
}