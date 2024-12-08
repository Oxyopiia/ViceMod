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
	drawCondition = { Vice.config.BOSS_COUNTER_OUTSIDE || Utils.getDTWorld()?.type == World.WorldType.BOSS }
) {
    private val bosses get() = Vice.storage.bosses

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!enabled() || !drawCondition()) return
		draw(event.context)
	}

	private fun draw(context: DrawContext): Size {
		return when {
			World.Vice.isInWorld() -> drawMasteryInfo(bosses.vice, "Vice", Colors.ViceBoss, context)
			World.Wasteyard.isInWorld() -> drawMasteryInfo(bosses.wasteyard, "Wasteyard", Colors.ChatColor.DarkRed, context)
			World.Gelato.isInWorld() -> drawMasteryInfo(bosses.gelato, "El Gelato", Colors.ChatColor.Green, context)
			World.PPP.isInWorld() -> drawMasteryInfo(bosses.ppp, "PPP", Colors.ChatColor.Red, context)
			World.Minehut.isInWorld() -> drawMasteryInfo(bosses.minehut, "Minehut", Colors.ChatColor.Aqua, context)
			World.Elderpork.isInWorld() -> drawMasteryInfo(bosses.elderpork, "Elderpork", Colors.Elderpork, context)
			else -> drawBasicBossCounter(context)
		}
	}

	private fun drawMasteryInfo(data: BossStorage.MasterableBoss, displayName: String, color: Color, context: DrawContext): Size {
		val thresholds = MasteryHandler.tierThresholds
		val classicCompletions = data.completions
		val masteryCompletions = data.masteryCompletions

		val tierIndex = thresholds.indexOfFirst { masteryCompletions < it }.takeIf { it != -1 } ?: thresholds.size
		val tierColor = if (tierIndex >= thresholds.size) Colors.ChatColor.Gold else Colors.ChatColor.Green
		val list = mutableListOf(
			displayName.toText(color, bold = true),
			"✪ Tier $tierIndex".toText(tierColor),
			"§7Masteries: ".toText().append("$masteryCompletions".toText(color))
		)

		if (classicCompletions > masteryCompletions) {
			list.add("§7Completions: ".toText().append("${data.completions}".toText(color)))
		}

		if (tierIndex < thresholds.size) {
			val remainingRuns = thresholds[tierIndex] - masteryCompletions
			list.add("§7Tier §a${tierIndex + 1} §7in §a$remainingRuns §7runs.".toText())
		}

		val unclaimedTiers = (1..tierIndex).filterNot { data.claimedTiers.contains(it) }

		if (unclaimedTiers.isNotEmpty()) {
			val message = if (unclaimedTiers.size == 1) {
				"Tier ${unclaimedTiers[0]} has not been claimed!"
			}
			else {
				val last = unclaimedTiers.last()
				val allButLast = unclaimedTiers.dropLast(1).joinToString(", ")
				"Tiers $allButLast and $last are unclaimed!"
			}

			list.add("".toText())
			list.add("§c$message".toText())
		}

		return position.drawTexts(list, context)
	}

	private fun MutableList<Text>.addBossStat(string: String, color: Color, data: BossStorage.Boss) {
		val completions = data.completions

		if (data is BossStorage.MasterableBoss && data.masteryCompletions > 0) {
			val masteries = data.masteryCompletions
			val text = string.toText(color)

			if (completions <= masteries) {
				add(text.append(" §f$masteries".toText()))
				return

			} else {
				add(text.append(" §f$masteries §7(§c$completions§7)".toText()))
				return
			}
		}

		if (completions <= 0) return
		add(string.toText(color).append(" $completions".toText()))
	}

	private fun drawBasicBossCounter(context: DrawContext): Size {
		val list: MutableList<Text> = mutableListOf("Bosses".toText(Vice.PRIMARY, bold = true))

		list.addBossStat("Vice", Colors.ViceBoss, bosses.vice)
		list.addBossStat("Wasteyard", Colors.ChatColor.Red, bosses.wasteyard)
		list.addBossStat("El Gelato", Colors.ChatColor.Green, bosses.gelato)
		list.addBossStat("PPP", Colors.ChatColor.Red, bosses.ppp)
		list.addBossStat("Minehut", Colors.ChatColor.Aqua, bosses.minehut)
		list.addBossStat("Elderpork", Colors.Elderpork, bosses.elderpork)
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
