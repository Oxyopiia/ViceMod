package net.oxyopia.vice.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File

@Suppress("unused")
class DevConfig : Vigilant(File("./config/vice/developerSettings.toml"), "Vice Developer Menu") {
	/** EXPERIMENTS  */
	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Red Fadeout Multiplier",
		description = "Red fade for item cooldown display option Color Fade\nDefault 2.7f",
		category = "Experiments",
		minF = 0.5f,
		maxF = 7.5f,
		subcategory = "Item Cooldown Display"
	)
	var ITEMCD_RED_FADE_OVERRIDE: Float = 2.7f

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Green Fadein Multiplier",
		description = "Green fade for item cooldown display option Color Fade\nDefault 1.3f",
		category = "Experiments",
		minF = 0.5f,
		maxF = 7.5f,
		subcategory = "Item Cooldown Display"
	)
	var ITEMCD_GREEN_FADE_OVERRIDE: Float = 1.3f

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Live Arena Overlay in HUD Manager",
		description = "DOES NOTHING!! literally just a mockup graphic and test for HudManager",
		category = "Experiments",
		subcategory = "HudManager"
	)
	var LIVE_ARENA_OVERLAY_THING: Boolean = false

	/** BYPASSES  */
	@Property(
		type = PropertyType.SWITCH,
		name = "Bypass Scoreboard Checker",
		description = "bypasses inDoomTowers check for scoreboard\nthis will make doomtowers features render anywhere and everywhere",
		category = "Bypasses"
	)
	var BYPASS_INSTANCE_CHECK: Boolean = false

	/** DEBUGS  */
	@Property(
		type = PropertyType.SWITCH,
		name = "Sounds",
		description = "sends sound information when there is a LITERAL SOUND (sent from server only)",
		category = "Debugs"
	)
	var SEND_SOUND_INFO: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Bossbar debugger",
		description = "yep",
		category = "Debugs"
	)
	var BOSSBAR_DEBUGGER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Mob Spawn",
		description = "mobbobobobo",
		category = "Debugs"
	)
	var MOB_SPAWN_DEBUGGER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "PredictProjectile",
		description = "origin and end coordinates, calling patterns",
		category = "Debugs"
	)
	var PREDICT_PROJECTILE_DEBUGGER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Fishing",
		description = "sound deviations, velocity packets, last hook info",
		category = "Debugs"
	)
	var FISHING_DEBUGGER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "GameRenderer",
		description = "line rendering, box rendering utils",
		category = "Debugs"
	)
	var GAME_RENDERER_DEBUGGER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "InGameHud mixin",
		description = "Debugger for title data, subtitle data, and more",
		category = "Debugs"
	)
	var INGAMEHUD_MIXIN_DEBUGGER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "ItemAbility",
		description = "item ability sutff",
		category = "Debugs"
	)
	var ITEM_ABILITY_DEBUGGER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Arenas",
		description = "arenas",
		category = "Debugs"
	)
	var ARENAS_DEBUGGER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Cooking debugger",
		description = "w4 stuff",
		category = "Debugs"
	)
	var COOKING_DEBUGGER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Boss Title Detection information",
		description = "robotop is hot",
		category = "Debugs"
	)
	var BOSS_DETECTION_INFO: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "SlotClickEvent",
		description = "slot click event LMFAOOOOOOOOOO",
		category = "Debugs"
	)
	var SLOT_CLICK_DEBUGGER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Expeditions",
		description = "e",
		category = "Debugs"
	)
	var EXPEDITION_DEBUGGER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Lost In Time stuff",
		description = "e",
		category = "Debugs"
	)
	var LOST_IN_TIME_DEBUGGER: Boolean = false

	fun init() {
		initialize()
		markDirty()
	}
}
