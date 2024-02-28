package net.oxyopia.vice.features.itemabilities

import net.minecraft.client.MinecraftClient
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.SubtitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils

object CooldownDisplayChanger {
    private class DisplayType {
        @Suppress("unused")
        companion object {
            const val NORMAL = 0
            const val ACTION_BAR = 1
            const val HIDDEN = 2
        }
    }

    // Known Bug: will clear other cooldowns (daily rewards/arenas)
    @SubscribeEvent
    fun onSubtitle(event: SubtitleEvent) {
        if (!event.subtitle.contains("Cooldown")) return
        if (Vice.config.ITEM_COOLDOWN_TITLE_TYPE == DisplayType.NORMAL) return

        if (Vice.config.ITEM_COOLDOWN_TITLE_TYPE == DisplayType.ACTION_BAR) {
            HudUtils.sendVanillaActionBar(event.rawSubtitle)
        }

        MinecraftClient.getInstance().inGameHud.clearTitle()
        event.cancel()
    }
}