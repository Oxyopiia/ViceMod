package net.oxyopia.vice.features.bosses

import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.*
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawTexts
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.Utils
import java.awt.Color

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
		val list: MutableList<Text> = mutableListOf("Bosses".toText(Vice.PRIMARY, bold = true))

		list.addBossStat("Vice", Colors.ViceBoss, bosses.vice.completions)
		list.addBossStat("Wasteyard", Colors.ChatColor.DarkRed, bosses.wasteyard.completions)
		list.addBossStat("El Gelato", Colors.ChatColor.Green, bosses.gelato.completions)
		list.addBossStat("PPP", Colors.ChatColor.Red, bosses.ppp.completions)
		list.addBossStat("Minehut", Colors.ChatColor.Aqua, bosses.minehut.completions)
		list.addBossStat("Elderpork", Colors.Elderpork, bosses.elderpork.completions)
		list.addBossStat("Shadow Gelato", Colors.ShadowGelato, bosses.shadowGelato.completions)
		list.addBossStat("Abyssal Vice", Colors.Diox, bosses.abyssalVice.completions)

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
			World.Vice.isInWorld() && content.contains(viceTimeRegex) -> bosses.vice.completions--
			World.Gelato.isInWorld() && content.contains(gelatoTimeRegex) -> bosses.gelato.completions--
			World.PPP.isInWorld() && content.contains(pppTimeRegex) -> bosses.ppp.completions--
			World.Minehut.isInWorld() && content.contains(minehutTimeRegex) -> bosses.minehut.completions--
			World.ShadowGelato.isInWorld() && content.contains(shadowTimeRegex) -> bosses.shadowGelato.completions--
			World.AbyssalVice.isInWorld() && content.contains(abyssalCompletionRegex) -> bosses.abyssalVice.completions++
			World.Elderpork.isInWorld() && content.contains("TAPE FINISHED.") -> bosses.elderpork.completions++
			else -> return
		}

		Vice.storage.markDirty()
    }

	@SubscribeEvent
	fun onEntityDeath(event: EntityDeathEvent) {
		if (event.entity is PlayerEntity) return
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
    fun onTitle(event: TitleEvent) {
        if (!World.Tower.isInWorld() || event.title != "You Have Escaped Wasteyard.") return

		bosses.wasteyard.completions++
		Vice.storage.markDirty()
    }

	private fun MutableList<Text>.addBossStat(string: String, color: Color, value: Int) {
		if (value > 0) add(string.toText(color).append(Text.literal(" $value").formatted(Formatting.WHITE)))
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
