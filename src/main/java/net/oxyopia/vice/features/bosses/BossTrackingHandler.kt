package net.oxyopia.vice.features.bosses

import net.minecraft.entity.player.PlayerEntity
import net.oxyopia.vice.Vice
import net.oxyopia.vice.config.features.BossStorage
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.TitleEvent
import net.oxyopia.vice.events.core.SubscribeEvent

object  BossTrackingHandler {
	private val viceTimeRegex = Regex("You ran out of time to defeat Vice\\. \\(\\d+m\\)")
	private val gelatoTimeRegex = Regex("You ran out of time to defeat \"Corrupted Vice\" Phase 3\\. \\(\\d+m\\)")
	private val pppTimeRegex = Regex("You ran out of time to defeat Percentage Player Percentage. \\(\\d+m\\)")
	private val minehutTimeRegex = Regex("You ran out of time to defeat \\?\\?\\?\\. \\(\\d+m\\)")
	private val shadowTimeRegex = Regex("You ran out of time to defeat Phase 3 of Shadow Gelato. \\(\\d+m\\)")
	private val abyssalCompletionRegex = Regex("Abyssal Vice: No\\.\\. This- This Can't Be\\?!")

	private val bosses get() = Vice.storage.bosses

	@SubscribeEvent
	fun onChatMessage(event: ChatEvent) {
		if (!event.hasNoSender && event.sender != "Anodized") return
		val content = event.string

		when {
			World.Vice.isInWorld() && content.contains(viceTimeRegex) -> bosses.vice.decrementCompletions()
			World.Gelato.isInWorld() && content.contains(gelatoTimeRegex) -> bosses.gelato.decrementCompletions()
			World.PPP.isInWorld() && content.contains(pppTimeRegex) -> bosses.ppp.decrementCompletions()
			World.Minehut.isInWorld() && content.contains(minehutTimeRegex) -> bosses.minehut.decrementCompletions()
			World.ShadowGelato.isInWorld() && content.contains(shadowTimeRegex) -> bosses.shadowGelato.decrementCompletions()
			World.AbyssalVice.isInWorld() && content.contains(abyssalCompletionRegex) -> bosses.abyssalVice.incrementCompletions()
			World.Elderpork.isInWorld() && content.contains("TAPE FINISHED.") -> bosses.elderpork.incrementCompletions()
			event.sender == "Anodized" && World.Vatican.isInWorld() && content.contains("It's about time you come with me.") -> bosses.vatican.incrementCompletions()
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
}