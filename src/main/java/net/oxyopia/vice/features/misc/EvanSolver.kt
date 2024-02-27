package net.oxyopia.vice.features.misc

import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.Utils

object EvanSolver {
	@SubscribeEvent
	fun onChat(event: ChatEvent) {
		if (!Vice.config.GLITCH_HQ_EVAN_SOLVER || !World.GlitchHQ.isInWorld()) return

		questions[event.string.lowercase()]?.let { isCorrect ->
			val text = if (isCorrect) {
				Utils.playSound("block.note_block.pling", 2f)
				"&&a&&lTRUE"
			} else {
				Utils.playSound("block.note_block.pling", 0.5f)
				"&&c&&lFALSE"
			}

			HudUtils.sendVanillaTitle(text, "")
		}
	}

	private val questions = mapOf(
		"Is the max amount of silver 900?" to true,
		"Is the Adventurer's Hook from World 5?" to true,
		"Does the World 11 Train spawn every 45m?" to true,
		"Is \"Quaking Quarry\" a real scrapped world?" to true,
		"Is World 9 called \"Glimpse\"" to true,
		"The answer is not False" to true,
		"Is the Jelly NPC on Floor 4?" to true,
		"Is Exonitas after World 11?" to true,
		"Is \"Chef\" a real set?" to true,
		"Do Pistons heal 2 hearts?" to true, // change after curzee fix it, it suppost be 1 heart so "false"
		"Is the \"Barrel\" the 5th Backpack Upgrade?" to true,
		"Are there 3 Porters with the Train?" to true, // change after curzee fix it, it suppost be 2 porters so "false",
		"Is the \"Ultimeatium\" a World 4 Recipe?" to true,

		"Was DoomTowers made on the 6th May 2023?" to false,
		"Is Exonitas after World 12?" to false,
		"Is \"Shifty Shoots\" a real scrapped world?" to false,
		"Is the Blowpipe the first Ability Item?" to false,
		"Are there only 6 bosses in-game?" to false,
		"Does PPP (Boss 4) have 3 phases?" to false,
		"Does PPP (Boss 4) have 4 phases?" to false,
		"Is World 10 called \"Arcade\"" to false,
		"Is the Jelly NPC on Floor 5?" to false,
		"Is the max Carnage Level 4?" to false,
		"Is the max Carnage Level 5?" to false,
		"The answer is not True" to false,
		"Is the Adventurer's Hook from World 4?" to false,
		"Is the max amount of silver 850?" to false,
		"Does the World 11 Train spawn every 40m?" to false,
		"Is the \"Whopper\" a World 4 Recipe?" to false,
		"Is the \"Whoppa\" a World 4 Recipe?" to false,
		"Are there 6 Mystic Items in World 5?" to false,
		"What colour am I?" to false,
		"Is \"Glitch\" a real set?" to false,
		"Is \"Carnage\" a real set?" to false,
		"Is the \"Large Backpack\" the 4th Backpack Upgrade?" to false,
	)
		get() = field.mapKeys { (key, _) -> key.lowercase() }
}