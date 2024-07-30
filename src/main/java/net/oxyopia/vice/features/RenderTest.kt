package net.oxyopia.vice.features

import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.boss.BossBar
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.BossBarEvents
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawString
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.HudUtils.drawText
import net.oxyopia.vice.utils.HudUtils.drawTexts
import java.awt.Color

object RenderTest : HudElement("WHAT???", Position(100f, 100f)){
	@SubscribeEvent
	fun onBossbarAfter(event: BossBarEvents.Insert) {
		if (!shouldDraw()) return

		event.add("Hello World!", 0.72f, BossBar.Color.PINK, BossBar.Style.PROGRESS)
		event.add("He3llo World!", 0.2f, BossBar.Color.YELLOW, BossBar.Style.NOTCHED_10)
		event.add("Hello2 World!", 1f, BossBar.Color.RED, BossBar.Style.NOTCHED_12)
		event.add("Hel4lo World!", 0f, BossBar.Color.WHITE, BossBar.Style.PROGRESS)
	}

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!shouldDraw()) return

		// Anchor top left to (50, 20)
		val pos50201uc = Position(50f, 20f, centered = false)
		pos50201uc.drawText(Text.literal("Color Test 1 uc").withColor(Color(55,134,255).rgb), event.context)

		// Anchor center to (50, 50)
		val pos50501 = Position(50f, 50f)
		pos50501.drawText(Text.literal("Color Test 1 cc").withColor(Color(55,134,255).rgb), event.context)

		// Anchor center to (50, 75) with scale 2
		val pos50752 = Position(50f, 75f, scale = 2f)
		pos50752.drawText(Text.literal("Scale Test 2x cc").withColor(Color(55, 134, 255).rgb), event.context)

		// Anchor top left to (50, 100) with scale 2
		val pos100502uc = Position(50f, 100f, scale = 2f, centered = false)
		pos100502uc.drawText(Text.literal("Scale Test 2x uc").withColor(Color(55, 134, 255).rgb), event.context)

		// Anchor center to (100, 100) with scale 1.5
		val pos10010015 = Position(100f, 100f, scale = 1.5f)
		pos10010015.drawString("&&e100, 100, s1.5", event.context)

		// Anchor center to (300, 50)
		val scalar1 = Position(300f, 50f, scale = 1f)
		scalar1.drawStrings(listOf(
			"&&aScalar 1 Test 1",
			"&&aScalar 1 Test 2",
		), event.context)

		// Anchor center to (300, 100) with scale 2
		val scalar2 = Position(300f, 100f, scale = 2f, centered = false)
		scalar2.drawTexts(listOf(
			Text.literal("Scalar 2 Test 1").withColor(Colors.ChatColor.Green.rgb),
			Text.literal("Scalar 2 Test 2").withColor(Colors.ChatColor.Green.rgb),
		), event.context)
	}

	override fun storePosition(position: Position) {}

	override fun shouldDraw(): Boolean {
		return Vice.config.DEVMODE && Vice.devConfig.LIVE_ARENA_OVERLAY_THING
	}

	override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
		val list = listOf(
			"&&b&&lCryonic Caverns",
			"&&bWave 26",
			"",
			"&&c00:26",
			"",
			"&&fCommon Drop &&733x",
			"&&fArctic Scroll &&b6.5%",
			"",
			"&&fMobs Remaining &&c7"
		)

		return position.drawStrings(list, context)
	}
}