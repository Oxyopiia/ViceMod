package net.oxyopia.vice.features.bosses.masteries

import net.minecraft.item.Items
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Debugger
import net.oxyopia.vice.events.ChestRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.ItemUtils.getLore

object MasteryHandler {
	private var lastMenuID = -1
	internal val tierThresholds = listOf(10, 25, 50, 150)

	private val masteriesTitle by lazy {
		Regex("Masteries \\((.+)\\)")
	}
	private val masteriesProgress by lazy {
		Regex("Progress: (\\d+)/(\\d+)")
	}

	@SubscribeEvent
	fun onChestRenderFully(event: ChestRenderEvent) {
		if (!event.isActuallyFullyRendered() || !event.chestName.contains("Masteries (") || lastMenuID == event.id) return
		lastMenuID = event.id

		val boss = masteriesTitle.find(event.chestName)?.groupValues?.get(1) ?: return
		val interactables = event.slots.filter { it.stack.item != Items.GRAY_STAINED_GLASS_PANE && it.stack.cleanName().startsWith("Mastery Tier") }
		Debugger.MASTERY.debug("Found ${interactables.size} interactables for $boss")

		val bossStorage = Vice.storage.bosses
		val storageEntry = when (boss) {
			"Vice" -> bossStorage.vice
			"Wasteyard" -> bossStorage.wasteyard
			"Gelato" -> bossStorage.gelato
			"PPP" -> bossStorage.ppp
			"LBS Unique" -> bossStorage.minehut
			"Elderpork" -> bossStorage.elderpork
			// "Vatican" -> bossStorage.vatican
			else -> return
		}

		interactables.forEachIndexed { index, it ->
			val lore = it.stack.getLore()
			val progressLine = lore.firstOrNull { line -> line.startsWith("Progress: ") } ?: return@forEachIndexed

			masteriesProgress.find(progressLine)?.let { match ->
				val count = match.groupValues[1].toIntOrNull() ?: return
				storageEntry.masteryCompletions = count
				Vice.storage.markDirty()
			}

			if (it.stack.item == Items.LIME_STAINED_GLASS_PANE && !storageEntry.claimedTiers.contains(index + 1)) {
				storageEntry.claimedTiers.add(index + 1)
				Vice.storage.markDirty()
			}
		}

		if (!storageEntry.hasOpened) {
			storageEntry.hasOpened = true
			Vice.storage.markDirty()
		}
	}
}