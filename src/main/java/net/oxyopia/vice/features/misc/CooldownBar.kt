package net.oxyopia.vice.features.misc

import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.SubtitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils

object CooldownBar {

    @SubscribeEvent
    fun onTitle(event: SubtitleEvent) {
        if(Vice.config.HIDE_ITEM_COOLDOWN_TITLES == 1 && event.subtitle.contains("Cooldown")) {
            val cooldownRegex = Regex("Cooldown \\((?:(\\d+) hour(?:s)?(?: and )?)?(?:(\\d+(?:\\.\\d+)?) minute(?:s)?(?: and )?)?(?:\\.?(\\d+\\.\\d+)?s)?\\)")

            cooldownRegex.find(event.subtitle)?.apply {
                val hours = groupValues[1]?.toIntOrNull() ?: 0
                val minutes = groupValues[2]?.toFloatOrNull() ?: 0f
                val seconds = groupValues[3]?.toFloatOrNull() ?: 0f

                val formattedCooldown = when {
                    hours > 0 -> "&&cCooldown (&&6$hours hour${if (hours != 1) "s" else ""} and $minutes minute${if (minutes.toInt() != 1) "s" else ""}&&c)"
                    minutes > 0 -> "&&cCooldown (&&6${minutes.toInt()} minute${if (minutes.toInt() != 1) "s" else ""} and ${String.format("%.2f", seconds)}s&&c)"
                    else -> "&&cCooldown (&&6${String.format("%.2f", seconds)}s&&c)"
                }
                HudUtils.sendVanillaAuctionBar(formattedCooldown)
            }




        }
    }
}