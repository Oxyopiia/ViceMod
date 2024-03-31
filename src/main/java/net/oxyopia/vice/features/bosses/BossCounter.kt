package net.oxyopia.vice.features.bosses

import gg.essential.elementa.svg.data.plus
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.Text
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

	private var dioxMode = "EASY"

    private val bosses get() = Vice.storage.bosses

	/*fun getDioxMode(values: List<String>) {
		val bossName = values[0].split(" - ")[0]
		Utils.sendViceMessage(bossName)
		if(bossName == "Diox") {
			val maxHP = values[1].split("/")[1]
			Utils.sendViceMessage(maxHP)
			if(maxHP == "375") Utils.sendViceMessage("NORMAL MODE, :sunglasses:!")
			if(maxHP == "187.5") Utils.sendViceMessage("EASY MODE, BABY!")
		}
	}*/

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        val content = event.string

		when {
			World.Vice.isInWorld() && content.contains(viceTimeRegex) -> bosses.vice.completions--
			World.Gelato.isInWorld() && content.contains(gelatoTimeRegex) -> bosses.gelato.completions--
			World.PPP.isInWorld() && content.contains(pppTimeRegex) -> bosses.ppp.completions--
			World.Minehut.isInWorld() && content.contains(minehutTimeRegex) -> bosses.minehut.completions--
			World.ShadowGelato.isInWorld() && content.contains(shadowTimeRegex) -> bosses.shadowGelato.completions--
			World.DarkVice.isInWorld() && content.contains(abyssalCompletionRegex) -> bosses.abyssalVice.completions++
			World.Diox.isInWorld() && content.contains("Diox: ITS NICE FOR US TO FINALLY MEET FACE TO FACE.") -> dioxMode = "EASY" // restart mode to easy
			else -> return
		}

		Vice.storage.markDirty()
    }

	@SubscribeEvent
	fun onActionBar(event: ActionBarEvent) {
		if(event.content.toString().contains("Jump to break free.")) dioxMode = "NORMAL"
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
	fun onSubtitle(event: SubtitleEvent) {
		if(event.subtitle.equals("TERMINATED")) {
			if(dioxMode == "NORMAL") bosses.diox.completions++
			else if(dioxMode == "EASY") bosses.dioxEasy.completions++
			Vice.storage.markDirty()
		}
	}

	@SubscribeEvent
    fun onSound(event: SoundEvent) {
        if (World.Wasteyard.isInWorld()) {
			if (event.soundName != "ui.toast.challenge_complete" || event.pitch != 1f || event.volume != 9999f) return

			bosses.wasteyard.completions++
			Vice.storage.markDirty()
		}
    }

	private fun draw(context: DrawContext): Pair<Float, Float> {
		val list: MutableList<String> = mutableListOf("&&b&&lBosses")

		list.addBossStat("&&5Vice", bosses.vice.completions, null)
		list.addBossStat("&&4Wasteyard", bosses.wasteyard.completions, null)
		list.addBossStat("&&aEl Gelato", bosses.gelato.completions, null)
		list.addBossStat("&&cPPP", bosses.ppp.completions, null)
		list.addBossStat("&&bMinehut", bosses.minehut.completions, null)
		list.addBossStat("&&2Diox", bosses.diox.completions, bosses.dioxEasy.completions)
		list.addBossStat("&&dShadow Gelato", bosses.shadowGelato.completions, null)
		list.addBossStat("&&8Abyssal Vice", bosses.abyssalVice.completions, null)

		if (list.size == 1) {
			list.add("&&cNo bosses slain yet!")
		}

		return position.drawStrings(list, context)
	}

	private fun MutableList<String>.addBossStat(string: String, value: Int, value2: Int?) {
		if(value2 != null) {
			if((value2 + value) > 0) add("$string &&a$value2 &&f| &&c$value &&f(${value2+value})")
		} else if (value > 0) add("$string &&f$value")
	}

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (!shouldDraw()) return
		draw(event.context)
    }

    override fun storePosition(position: Position) {
        Vice.storage.bosses.bossCounterPos = position
        Vice.storage.markDirty()
    }

    override fun shouldDraw(): Boolean = Vice.config.BOSS_COUNTER

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
        return draw(context)
    }
}
