package net.oxyopia.vice.features.bosses

import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.config.features.BossStorage
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.*
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawTexts
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.Utils
import java.awt.Color

object BossCounter : HudElement(
	"Boss Counter",
	Vice.storage.bosses.bossCounterPos,
	{ Vice.storage.bosses.bossCounterPos = it },
	enabled = { Vice.config.BOSS_COUNTER },
	drawCondition = { Vice.config.BOSS_COUNTER_OUTSIDE || Utils.getDTWorld()?.properties?.contains(World.WorldProperty.BOSS) ?: false }
) {
    private val bosses get() = Vice.storage.bosses

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!enabled() || !drawCondition()) return
		draw(event.context)
	}

	private fun draw(context: DrawContext): Size {
		return drawBasicBossCounter(context)
	}

	private fun MutableList<Text>.addBossStat(string: String, color: Color, data: BossStorage.Boss) {
		val completions = data.completions

		if (data is BossStorage.DioxBossData) {
			val easyComps = data.easyCompletions
			val normalComps = data.normalCompletions

			if (easyComps + normalComps == 0) return

			add(string.toText(color).append(" §a$easyComps §7| §c$normalComps".toText()))
			return
		}

		if (completions == 0) return
		if (data is BossStorage.MasterableBoss && data.masteryCompletions > 0) {
			val masteries = data.masteryCompletions
			var text = string.toText(color).append(" §f$masteries".toText())

			if (completions > masteries) text = text.append(" §7(§c$completions§7)".toText())
			add(text)

			return
		}

		add(string.toText(color).append(" $completions".toText()))
	}

	private fun drawBasicBossCounter(context: DrawContext): Size {
		val list: MutableList<Text> = mutableListOf("Bosses".toText(Vice.PRIMARY, bold = true))

		list.addBossStat("Vice", Colors.ViceBoss, bosses.vice)
		list.addBossStat("Wasteyard", Colors.ChatColor.Red, bosses.wasteyard)
		list.addBossStat("El Gelato", Colors.ChatColor.Green, bosses.gelato)
		list.addBossStat("PPP", Colors.ChatColor.Red, bosses.ppp)
		list.addBossStat("Minehut", Colors.ChatColor.Aqua, bosses.minehut)
		list.addBossStat("Diox", Colors.Diox, bosses.diox)
		list.addBossStat("Elderpork", Colors.Elderpork, bosses.elderpork)
		list.addBossStat("THE VATICAN", Colors.Vatican, bosses.vatican)
		list.addBossStat("Shadow Gelato", Colors.ShadowGelato, bosses.shadowGelato)
		list.addBossStat("Abyssal Vice", Colors.Diox, bosses.abyssalVice)

		if (list.size == 1) {
			list.add("No Bosses slain yet!".toText(Colors.ChatColor.Red))
		}

		return position.drawTexts(list, context)
	}

	override fun Position.drawPreview(context: DrawContext): Size {
        return draw(context)
    }
}
