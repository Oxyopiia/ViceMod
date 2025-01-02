package net.oxyopia.vice.features.misc

import net.minecraft.item.ItemStack
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ClientTickEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.hud.PlayerStats
import net.oxyopia.vice.utils.hud.HudUtils
import net.oxyopia.vice.utils.ItemUtils.getLore
import net.oxyopia.vice.utils.SoundUtils
import net.oxyopia.vice.utils.TimeUtils.timeDeltaWithin
import net.oxyopia.vice.utils.Utils
import kotlin.time.Duration.Companion.seconds

object YetiHeadWarning {
	private val NOTIFICATION_RATE = 30.seconds
	private var lastNotified = -1L

	@SubscribeEvent
	fun onHudRender(event: ClientTickEvent) {
		if (!event.repeatSeconds(1) || !Vice.config.NO_YETI_HEAD_WARNING) return
		if (!World.ArcticAssault.isInWorld() || PlayerStats.getFishingTime().isNone()) return

		val armor = Utils.getPlayer()?.inventory?.armor?.get(0) ?: ItemStack.EMPTY
		if (!armor.getLore().contains("Ability: Yeti Scare") && !lastNotified.timeDeltaWithin(NOTIFICATION_RATE)) {
			lastNotified = System.currentTimeMillis()
			HudUtils.sendViceTitle("&&cNo Yeti Head!", 3f)
			SoundUtils.playDing()
		}
	}
}