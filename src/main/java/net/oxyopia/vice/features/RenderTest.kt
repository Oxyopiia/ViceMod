package net.oxyopia.vice.features

import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.Position
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawString
import net.oxyopia.vice.utils.HudUtils.drawStrings

object RenderTest {
	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!Vice.config.DEVMODE) return

		// Anchor top left to (50, 20)
		val pos50201uc = Position(50f, 20f, centered = false)
		pos50201uc.drawString("&&b50, 20, s1, uc", event.context)

		// Anchor center to (50, 50)
		val pos50501 = Position(50f, 50f)
		pos50501.drawString("&&c50, 50, s1", event.context)

		// Anchor center to (50, 75) with scale 2
		val pos50752 = Position(50f, 75f, scale = 2f)
		pos50752.drawString("&&a50, 75, s2", event.context)

		// Anchor top left to (50, 100) with scale 2
		val pos100502uc = Position(50f, 100f, scale = 2f, centered = false)
		pos100502uc.drawString("&&d50, 100, s2, uc", event.context)

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
		scalar2.drawStrings(listOf(
			"&&aScalar 2 Test 1",
			"&&aScalar 2 Test 2",
		), event.context)

	}
}