package net.oxyopia.vice.features.expeditions

import net.minecraft.entity.decoration.DisplayEntity.TextDisplayEntity
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.EntityShouldRenderEvent
import net.oxyopia.vice.events.ModifySoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.Utils

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
		if (!Vice.config.HIDE_EXPEDITION_STYLE_POINTS || Utils.getDTWorld()?.type != World.WorldType.EXPEDITION) return

		val entity = event.entity as? TextDisplayEntity ?: return
		val text = entity.data?.text?.string ?: return

		if (stylePoints.contains(text)) {
			event.setReturnValue(false)
		} else {
			DevUtils.sendDebugChat("&&aEXPEDITIONS &&cDetected some weird text display: &&f$text", "EXPEDITION_DEBUGGER")
		}
	}

	@SubscribeEvent
	fun onSound(event: ModifySoundEvent) {
		if (!Vice.config.HIDE_EXPEDITION_STYLE_POINTS || Utils.getDTWorld()?.type != World.WorldType.EXPEDITION) return

		if (event.soundName == "entity.experience_orb.pickup" && event.pitch >= 1.0f && event.volume == 3f) {
			event.cancel()
		}
	}
}