package net.oxyopia.vice.features.bosses

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
import net.oxyopia.vice.config.features.BossStorage
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.*
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.Utils

object BossCounter: HudElement("Boss Counter", Vice.storage.bosses.bossCounterPos) {
    private val viceTimeRegex = Regex("You ran out of time to defeat Vice\\. \\(\\d+m\\)")
    private val gelatoTimeRegex = Regex("You ran out of time to defeat \"Corrupted Vice\" Phase 3\\. \\(\\d+m\\)")
    private val pppTimeRegex = Regex("You ran out of time to defeat Percentage Player Percentage. \\(\\d+m\\)")
    private val minehutTimeRegex = Regex("You ran out of time to defeat \\?\\?\\?\\. \\(\\d+m\\)")
    private val shadowTimeRegex = Regex("You ran out of time to defeat Phase 3 of Shadow Gelato. \\(\\d+m\\)")
    private val abyssalCompletionRegex = Regex("Abyssal Vice: No\\.\\. This- This Can't Be\\?!")

    private val bosses get() = Vice.storage.bosses

	override fun shouldDraw(): Boolean = Vice.config.BOSS_COUNTER
	override fun drawCondition(): Boolean = Vice.config.BOSS_COUNTER_OUTSIDE || Utils.getDTWorld()?.type == World.WorldType.BOSS

	private fun draw(context: DrawContext): Pair<Float, Float> {
		// I'm not actually hard coding everything, this is just here for me to test a general concept of it.
		if (World.Wasteyard.isInWorld()) {
			val thresholds = MasteryHandler.tierThresholds
			val completions = bosses.wasteyard.masteryCompletions

			val tierIndex = thresholds.indexOfFirst { completions < it }.takeIf { it != -1 } ?: thresholds.size
			val tierColor = if (tierIndex >= thresholds.size) "&&6" else "&&a"
			val list = mutableListOf(
				"&&c&&lWasteyard",
				"$tierColor✪ Tier $tierIndex",
				"&&7Completions: &&c${bosses.wasteyard.completions}",
				"&&7Masteries: &&c$completions"
			)

			if (tierIndex < thresholds.size) {
				val remainingRuns = thresholds[tierIndex] - completions
				list.add("&&7Tier &&a${tierIndex + 1} &&7in &&a$remainingRuns &&7runs.")
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

				list.add("")
				list.add("&&c$message")
			}

			return position.drawStrings(list, context)
		}

		val list: MutableList<String> = mutableListOf("&&b&&lBosses")

		list.addBossStat("&&5Vice", bosses.vice)
		list.addBossStat("&&4Wasteyard", bosses.wasteyard)
		list.addBossStat("&&aEl Gelato", bosses.gelato)
		list.addBossStat("&&cPPP", bosses.ppp)
		list.addBossStat("&&bMinehut", bosses.minehut)
		list.addBossStat("&&dShadow Gelato", bosses.shadowGelato)
		list.addBossStat("&&8Abyssal Vice", bosses.abyssalVice)

		if (list.size == 1) {
			list.add("&&cNo bosses slain yet!")
		}

		return position.drawStrings(list, context)
	}

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        val content = event.string

		when {
			World.Vice.isInWorld() && content.contains(viceTimeRegex) -> bosses.vice.decrementCompletions()
			World.Gelato.isInWorld() && content.contains(gelatoTimeRegex) -> bosses.gelato.decrementCompletions()
			World.PPP.isInWorld() && content.contains(pppTimeRegex) -> bosses.ppp.decrementCompletions()
			World.Minehut.isInWorld() && content.contains(minehutTimeRegex) -> bosses.minehut.decrementCompletions()
			World.ShadowGelato.isInWorld() && content.contains(shadowTimeRegex) -> bosses.shadowGelato.decrementCompletions()
			World.AbyssalVice.isInWorld() && content.contains(abyssalCompletionRegex) -> bosses.abyssalVice.incrementCompletions()
			else -> return
		}

		Vice.storage.markDirty()
    }

	@SubscribeEvent
	fun onEntityDeath(event: EntityDeathEvent) {
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
    fun onSound(event: SoundEvent) {
        if (!World.Tower.isInWorld()) return
		if (event.soundName != "ui.toast.challenge_complete" || event.pitch != 1f || event.volume != 9999f) return

		bosses.wasteyard.incrementCompletions()
		Vice.storage.markDirty()
    }

	private fun MutableList<String>.addBossStat(string: String, data: BossStorage.Boss) {
		val completions = data.completions

		if (completions <= 0) return
		if (data is BossStorage.MasterableBoss) {
			val masteries = data.masteryCompletions

			if (completions != masteries && masteries != 0) {
				add("$string &&f$completions &&7(&&c$masteries&&7)")
				return
			}
		}

		add("$string &&f$completions")
	}

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!shouldDraw() || !drawCondition()) return
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
        return draw(context)
    }
}
