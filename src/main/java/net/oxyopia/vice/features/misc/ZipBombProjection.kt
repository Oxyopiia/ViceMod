package net.oxyopia.vice.features.misc

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.AbilityUseEvent
import net.oxyopia.vice.events.EntityVelocityEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.itemabilities.ItemAbility
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.Utils

object ZipBombProjection {
	private var thrownYaw = 0f
	private var thrownPitch = 0f

	@SubscribeEvent
	fun onAbilityUse(event: AbilityUseEvent) {
		if (!Vice.config.ZIP_BOMB_PROJECTION || event.ability == ItemAbility.ZIP_BOMB) return

		Utils.getPlayer()?.apply {
			thrownYaw = yaw
			thrownPitch = pitch
		}
	}

	@SubscribeEvent
	fun onVelocityUpdate(event: EntityVelocityEvent) {
		if (!Vice.config.ZIP_BOMB_PROJECTION) return

		val entity = Utils.getWorld()?.getEntityById(event.entityId) ?: return
		if (!entity.name.string.contains("ZIP")) return

		val velocity = event.trueVelocity
		DevUtils.sendDebugChat("Zip Bomb vel: (${velocity.x}, ${velocity.y}, ${velocity.z})", "PREDICT_PROJECTILE_DEBUGGER")
	}
}