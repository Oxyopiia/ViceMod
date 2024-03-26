package net.oxyopia.vice.events

import net.minecraft.client.gui.hud.ClientBossBar
import net.minecraft.entity.boss.BossBar
import net.minecraft.text.Text
import java.util.UUID

class BossBarEvent : ViceEvent() {
	class Override(val instance: ClientBossBar, val original: Text) : Cancelable<Text>()

	class Insert : Cancelable<HashMap<UUID, ClientBossBar>>() {
		private var collectedBars = linkedMapOf<UUID, ClientBossBar>()

		fun add(text: String, percent: Float, color: BossBar.Color, style: BossBar.Style, darkenSky: Boolean = false, dragonMusic: Boolean = false, thickenFog: Boolean = false) {
			add(Text.of(text), percent, color, style, darkenSky, dragonMusic, thickenFog)
		}

		fun add(text: Text, percent: Float, color: BossBar.Color, style: BossBar.Style, darkenSky: Boolean = false, dragonMusic: Boolean = false, thickenFog: Boolean = false) {
			val uuid = UUID.randomUUID()
			collectedBars[uuid] = ClientBossBar(uuid, text, percent, color, style, darkenSky, dragonMusic, thickenFog)
		}

		override fun onceSent() = setReturnValue(collectedBars)
	}
}