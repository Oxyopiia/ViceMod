package net.oxyopia.vice.features.bosses

import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerEntity
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
    private val viceTimeRegex = Regex("You ran out of time to defeat Vice\\. \\(\\d+m\\)")
    private val gelatoTimeRegex = Regex("You ran out of time to defeat \"Corrupted Vice\" Phase 3\\. \\(\\d+m\\)")
    private val pppTimeRegex = Regex("You ran out of time to defeat Percentage Player Percentage. \\(\\d+m\\)")
    private val minehutTimeRegex = Regex("You ran out of time to defeat \\?\\?\\?\\. \\(\\d+m\\)")
    private val shadowTimeRegex = Regex("You ran out of time to defeat Phase 3 of Shadow Gelato. \\(\\d+m\\)")
    private val abyssalCompletionRegex = Regex("Abyssal Vice: No\\.\\. This- This Can't Be\\?!")

    private val bosses get() = Vice.storage.bosses

	private fun draw(context: DrawContext): Size {
		// I'm not actually hard coding everything, this is just here for me to test a general concept of it.
		if (World.Wasteyard.isInWorld()) {
			val thresholds = MasteryHandler.tierThresholds
			val completions = bosses.wasteyard.masteryCompletions

			val tierIndex = thresholds.indexOfFirst { completions < it }.takeIf { it != -1 } ?: thresholds.size
			val tierColor = if (tierIndex >= thresholds.size) Colors.ChatColor.Gold else Colors.ChatColor.Green
			val list = mutableListOf(
				"§c§lWasteyard".toText(),
				"✪ Tier $tierIndex".toText(tierColor),
				"§7Completions: §c${bosses.wasteyard.completions}".toText(),
				"§7Masteries: §c$completions".toText()
			)

			if (tierIndex < thresholds.size) {
				val remainingRuns = thresholds[tierIndex] - completions
				list.add("§7Tier §a${tierIndex + 1} §7in §a$remainingRuns §7runs.".toText())
			}

			val unclaimedTiers = (1..tierIndex).filterNot { bosses.wasteyard.claimedTiers.contains(it) }

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
		val list: MutableList<Text> = mutableListOf("Bosses".toText(Vice.PRIMARY, bold = true))

		list.addBossStat("Vice", Colors.ViceBoss, bosses.vice)
		list.addBossStat("Wasteyard", Colors.ChatColor.DarkRed, bosses.wasteyard)
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

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        val content = event.string
		if (!event.hasNoSender) return

		when {
			World.Vice.isInWorld() && content.contains(viceTimeRegex) -> bosses.vice.decrementCompletions()
			World.Gelato.isInWorld() && content.contains(gelatoTimeRegex) -> bosses.gelato.decrementCompletions()
			World.PPP.isInWorld() && content.contains(pppTimeRegex) -> bosses.ppp.decrementCompletions()
			World.Minehut.isInWorld() && content.contains(minehutTimeRegex) -> bosses.minehut.decrementCompletions()
			World.ShadowGelato.isInWorld() && content.contains(shadowTimeRegex) -> bosses.shadowGelato.decrementCompletions()
			World.AbyssalVice.isInWorld() && content.contains(abyssalCompletionRegex) -> bosses.abyssalVice.incrementCompletions()
			World.Elderpork.isInWorld() && content.contains("TAPE FINISHED.") -> bosses.elderpork.incrementCompletions()
			else -> return
		}

		Vice.storage.markDirty()
    }

	@SubscribeEvent
	fun onEntityDeath(event: EntityDeathEvent) {
		if (event.entity is PlayerEntity) return
		val entityName = event.entity.customName?.string ?: return

		when {
			World.Vice.isInWorld() && entityName.contains("Vice") -> bosses.vice.incrementCompletions()
			World.Gelato.isInWorld() && entityName.contains("True Gelato") -> bosses.gelato.incrementCompletions()
			World.PPP.isInWorld() && entityName.contains("Monster") && PPP.lastKnownPhase == 2 -> bosses.ppp.incrementCompletions()
			World.Minehut.isInWorld() && entityName.contains("TheOwner") -> bosses.minehut.incrementCompletions()
			World.ShadowGelato.isInWorld() && entityName.contains("True Shadow Gelato") -> bosses.shadowGelato.incrementCompletions()
			else -> return
		}

		Vice.storage.markDirty()
	}

	@SubscribeEvent
    fun onTitle(event: TitleEvent) {
        if (!World.Tower.isInWorld() || event.title != "You Have Escaped Wasteyard.") return

		bosses.wasteyard.incrementCompletions()
		Vice.storage.markDirty()
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

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!enabled() || !drawCondition()) return
		draw(event.context)
    }

	private fun BossStorage.Boss.incrementCompletions() {
		completions++
		if (this is BossStorage.MasterableBoss) {
			masteryCompletions++
		}
	}

	private fun BossStorage.Boss.decrementCompletions() {
		completions--
		if (this is BossStorage.MasterableBoss) {
			masteryCompletions--
		}
	}

    override fun storePosition(position: Position) {
        Vice.storage.bosses.bossCounterPos = position
        Vice.storage.markDirty()
    }

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
	override fun Position.drawPreview(context: DrawContext): Size {
        return draw(context)
    }
}
