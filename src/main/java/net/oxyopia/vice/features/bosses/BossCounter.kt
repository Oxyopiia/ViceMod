package net.oxyopia.vice.features.bosses

import net.minecraft.client.gui.DrawContext
import net.oxyopia.vice.Vice
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
		val list: MutableList<String> = mutableListOf("&&b&&lBosses")

		list.addBossStat("&&5Vice", bosses.vice.completions)
		list.addBossStat("&&4Wasteyard", bosses.wasteyard.completions)
		list.addBossStat("&&aEl Gelato", bosses.gelato.completions)
		list.addBossStat("&&cPPP", bosses.ppp.completions)
		list.addBossStat("&&bMinehut", bosses.minehut.completions)
		list.addBossStat("&&dShadow Gelato", bosses.shadowGelato.completions)
		list.addBossStat("&&8Abyssal Vice", bosses.abyssalVice.completions)

		if (list.size == 1) {
			list.add("&&cNo bosses slain yet!")
		}

		return position.drawStrings(list, context)
	}

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        val content = event.string

		when {
			World.Vice.isInWorld() && content.contains(viceTimeRegex) -> bosses.vice.completions--
			World.Gelato.isInWorld() && content.contains(gelatoTimeRegex) -> bosses.gelato.completions--
			World.PPP.isInWorld() && content.contains(pppTimeRegex) -> bosses.ppp.completions--
			World.Minehut.isInWorld() && content.contains(minehutTimeRegex) -> bosses.minehut.completions--
			World.ShadowGelato.isInWorld() && content.contains(shadowTimeRegex) -> bosses.shadowGelato.completions--
			World.AbyssalVice.isInWorld() && content.contains(abyssalCompletionRegex) -> bosses.abyssalVice.completions++
			else -> return
		}

		Vice.storage.markDirty()
    }

	@SubscribeEvent
	fun onEntityDeath(event: EntityDeathEvent) {
		val entityName = event.entity.customName?.string ?: return

		when {
			World.Vice.isInWorld() && entityName.contains("Vice") -> bosses.vice.completions++
			World.Gelato.isInWorld() && entityName.contains("True Gelato") -> bosses.gelato.completions++
			World.PPP.isInWorld() && entityName.contains("Monster") && PPP.lastKnownPhase == 2 -> bosses.ppp.completions++
			World.Minehut.isInWorld() && entityName.contains("TheOwner") -> bosses.minehut.completions++
			World.ShadowGelato.isInWorld() && entityName.contains("True Shadow Gelato") -> bosses.shadowGelato.completions++
			else -> return
		}

		Vice.storage.markDirty()
	}

	@SubscribeEvent
    fun onSound(event: SoundEvent) {
        if (!World.Wasteyard.isInWorld()) return
		if (event.soundName != "ui.toast.challenge_complete" || event.pitch != 1f || event.volume != 9999f) return

		bosses.wasteyard.completions++
		Vice.storage.markDirty()
    }

	private fun MutableList<String>.addBossStat(string: String, value: Int) {
		if (value > 0) add("$string &&f$value")
	}

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!shouldDraw() || !drawCondition()) return
		draw(event.context)
    }

    override fun storePosition(position: Position) {
        Vice.storage.bosses.bossCounterPos = position
        Vice.storage.markDirty()
    }

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
        return draw(context)
    }
}
