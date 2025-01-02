package net.oxyopia.vice.data

import net.minecraft.text.Text
import net.oxyopia.vice.utils.hud.HudUtils.toText
import java.awt.Color
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

enum class Brew(val duration: Duration, val displayName: Text) {
	FISHY_BREW(5.minutes, "Fishy Brew".toText(Color(127, 246, 250))),
	FETID_FLASK(5.minutes, "Fetid Flask".toText(Color(88, 164, 96))),
}