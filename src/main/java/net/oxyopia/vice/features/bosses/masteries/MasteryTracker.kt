package net.oxyopia.vice.features.bosses.masteries

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
import net.oxyopia.vice.utils.Utils.name
import java.awt.Color

object MasteryTracker : HudElement(
	"Mastery Tracker",
	Vice.storage.bosses.masteryTrackerPos,
	{ Vice.storage.bosses.masteryTrackerPos = it },
	enabled = { Vice.config.MASTERY_TRACKER },
	drawCondition = { Vice.config.ALWAYS_SHOW_MASTERY_TRACKER || Utils.getDTWorld()?.properties?.contains(World.WorldProperty.MASTERABLE) ?: false }
) {
	private val options = listOf(
		null,
		World.Vice,
		World.Wasteyard,
		World.Gelato,
		World.PPP,
		World.Minehut,
		World.Elderpork,
		// World.Vatican
	)

	private val bosses get() = Vice.storage.bosses

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!enabled() || !drawCondition()) return
		draw(Utils.getDTWorld(), event.context)
	}

	private fun draw(world: World?, context: DrawContext): Size {
		val isInMasterableWorld = Utils.getDTWorld()?.properties?.contains(World.WorldProperty.MASTERABLE) ?: false
		val preferred = options[Vice.config.DEFAULT_MASTERY_BOSS] ?: bosses.mostRecentMasterableBoss
		val actualWorld = (if (isInMasterableWorld) world else preferred) ?: preferred

		return when (actualWorld) {
			World.Vice -> drawMasteryInfo(bosses.vice, "Vice", Colors.ViceBoss, context)
			World.Wasteyard -> drawMasteryInfo(bosses.wasteyard, "Wasteyard", Colors.ChatColor.DarkRed, context)
			World.Gelato -> drawMasteryInfo(bosses.gelato, "El Gelato", Colors.ChatColor.Green, context)
			World.PPP -> drawMasteryInfo(bosses.ppp, "PPP", Colors.ChatColor.Red, context)
			World.Minehut -> drawMasteryInfo(bosses.minehut, "Minehut", Colors.ChatColor.Aqua, context)
			World.Elderpork -> drawMasteryInfo(bosses.elderpork, "Elderpork", Colors.Elderpork, context)
			// World.Vatican -> drawMasteryInfo(bosses.vatican, "THE VATICAN", Colors.Vatican, context)
			else -> draw(preferred, context)
		}
	}

	@SubscribeEvent
	fun onWorldChange(event: WorldChangeEvent) {
		val world = World.getById(event.world.name()) ?: return
		if (!world.properties.contains(World.WorldProperty.MASTERABLE)) return

		bosses.mostRecentMasterableBoss = world
		Vice.storage.markDirty()
	}

	private fun drawMasteryInfo(
		data: BossStorage.MasterableBoss,
		displayName: String,
		color: Color,
		context: DrawContext
	): Size {
		val thresholds = MasteryHandler.tierThresholds
		val classicCompletions = data.completions
		val masteryCompletions = data.masteryCompletions

		val tierIndex = thresholds.indexOfFirst { masteryCompletions < it }.takeIf { it != -1 } ?: thresholds.size
		val tierColor = if (tierIndex >= thresholds.size) Colors.ChatColor.Gold else Colors.ChatColor.Green
		val masteryCountText = "$masteryCompletions".takeIf { data.hasOpened } ?: "???"
		val list = mutableListOf(
			displayName.toText(color, bold = true),
			"✪ Tier $tierIndex".toText(tierColor),
			"§7Masteries: ".toText().append(masteryCountText.toText(color))
		)

		if (classicCompletions > masteryCompletions) {
			list.add("§7Completions: ".toText().append("${data.completions}".toText(color)))
		}

		if (tierIndex < thresholds.size) {
			val remainingRuns = thresholds[tierIndex] - masteryCompletions
			list.add("§7Tier §a${tierIndex + 1} §7in §a$remainingRuns §7runs.".toText())
		}

		if (!data.hasOpened) {
			list.add(Text.empty())
			list.add("§cOpen your $displayName Masteries menu!".toText())
		}

		val unclaimedTiers = (1..tierIndex).filterNot { data.claimedTiers.contains(it) }

		if (unclaimedTiers.isNotEmpty()) {
			val message = if (unclaimedTiers.size == 1) {
				"Tier ${unclaimedTiers[0]} has not been claimed!"
			} else {
				val last = unclaimedTiers.last()
				val allButLast = unclaimedTiers.dropLast(1).joinToString(", ")
				"Tiers $allButLast and $last are unclaimed!"
			}

			list.add("".toText())
			list.add("§c$message".toText())
		}

		return position.drawTexts(list, context)
	}

	override fun Position.drawPreview(context: DrawContext): Size {
		return draw(Utils.getDTWorld(), context)
	}
}
