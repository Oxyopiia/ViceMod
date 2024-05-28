package net.oxyopia.vice.config

import gg.essential.api.EssentialAPI
import gg.essential.universal.UDesktop.browse
import gg.essential.universal.UScreen
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.Migration
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyData
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.SortingBehavior
import net.oxyopia.vice.Vice
import net.oxyopia.vice.Vice.Companion.version
import net.oxyopia.vice.utils.Utils.inDoomTowers
import java.io.File
import java.net.URI
import kotlin.Comparator

@Suppress("PropertyName")
class Config : Vigilant(
	File("./config/vice/config.toml"),
	"Vice §b$version",
	sortingBehavior = ConfigSorting()
) {
	@Suppress("unused")
	@Property(
		type = PropertyType.BUTTON,
		name = "Edit HUD Locations",
		description =
			"Edit the positions of HUD Elements:\n" +
			"- Drag to reposition. §8(WASD/Arrow Keys)§7\n" +
			"- Scroll to scale. §8(+/-)§7\n" +
			"- Middle Click to toggle centering. §8(TAB)§7\n" +
			"- Press H or V to center horizontally/vertically.",
		category = "General",
		subcategory = "Vice",
		placeholder = "Edit HUD Locations"
	)
	fun openHudEditor() {
		if (!inDoomTowers) {
			EssentialAPI.getNotifications().push("HUD Manager", "Please open while in DoomTowers!", 3f)
			return
		}

		UScreen.displayScreen(HudEditor)
	}

	@Property(
		type = PropertyType.SWITCH,
		name = "HUD Text Shadow",
		description = "Toggle text shadow on Vice HUD elements.",
		category = "General",
		subcategory = "Vice"
	)
	var HUD_TEXT_SHADOW: Boolean = true


	@Suppress("unused")
	@Property(
		type = PropertyType.BUTTON,
		name = "Vice Discord",
		description = "Join the vice Discord to receive updates, post suggestions, and more!",
		category = "General",
		subcategory = "Vice",
		placeholder = "Join"
	)
	fun joinViceDiscord() {
		browse(URI.create("https://discord.gg/7nb9KcZHug"))
		EssentialAPI.getNotifications().push("Vice", "Hopefully opened Discord/Web Browser!", 3f)
	}

	// General/Developer

	@Property(
		type = PropertyType.SWITCH,
		name = "Developer Mode",
		description = "dev mode for beta versions/debugging\n§cOnly enable if you know what you're doing!",
		category = "General",
		subcategory = "Developer"
	)
	var DEVMODE: Boolean = false

	@Suppress("unused")
	@Property(
		type = PropertyType.BUTTON,
		name = "Dev Menu",
		description = "Open the developer menu",
		category = "General",
		subcategory = "Developer",
		placeholder = "Open"
	)
	fun openDevConfig() {
		UScreen.displayScreen(Vice.devConfig.gui())
	}

	@Property(
		type = PropertyType.SELECTOR,
		name = "Gaming Mode",
		description = "Choose your epic gaming mode for optimal gaming performance\n§a2 NEW GAMING MODES! ",
		category = "General",
		subcategory = "Developer",
		options = ["None", "Vice", "DoomTowers smashing", "msmdude", "digmonireland", "Trump", "clive", "law Abiding Citizen", "meme", "african digi"]
	)
	var DEV_GAMING_MODE: Int = 0

	@JvmField
	@Property(
		type = PropertyType.SWITCH,
		name = "Baby Mode",
		description = "Baby mode from other thing but everywhere\n§eDoes not change hitboxes! Works globally!",
		category = "General",
		subcategory = "Developer"
	)
	var DEV_BABY_MODE: Boolean = false

	// General/Quality of Life

	@Property(
		type = PropertyType.SWITCH,
		name = "Prevent Consuming Items",
		description = "Blocks block place packets when using consumable items, like Player Heads or Train Keys.\nItem Abilities still activate when enabled.",
		category = "General",
		subcategory = "Quality of Life"
	)
	var PREVENT_PLACING_PLAYER_HEADS: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Backpack Renaming",
		description = "Master toggle for Backpack Renaming.\nUse /vicebackpackrename (name|reset) to change!\nNormal Minecraft Color codes are valid.",
		category = "General",
		subcategory = "Quality of Life"
	)
	var BACKPACK_RENAMING: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Trash Protection",
		description = "Blocks clicking valuable items while inside the Trash menu.\nUse /viceprotectitem to protect any item.\n§eHold LCONTROL while clicking to bypass!",
		category = "General",
		subcategory = "Quality of Life",
		searchTags = ["dispose", "disposal", "protect"]
	)
	var TRASH_PROTECTION: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Train Timer",
		description = "Displays a HUD graphic showing information about the Train in World 11.",
		category = "General",
		subcategory = "Quality of Life"
	)
	var TRAIN_TIMER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Train Timer outside World 11",
		description = "Show the train timer while outside World 11, yeah",
		category = "General",
		subcategory = "Quality of Life"
	)
	var TRAIN_TIMER_OUTSIDE: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Revolver Blindness",
		description = "Hides the blindness effect when aiming the revolver.",
		category = "General",
		subcategory = "Quality of Life"
	)
	var HIDE_REVOLVER_BLINDNESS: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Forge Timers",
		description = "Displays the time remaining for items in the Forge to smelt.",
		category = "General",
		subcategory = "Quality of Life"
	)
	var FORGE_TIMERS: Boolean = true

	@JvmField
	@Property(
		type = PropertyType.SWITCH,
		name = "Better Tower Beacon UI",
		description = "Display the floor number as a stack size, and add the featured worlds in the lore.",
		category = "General",
		subcategory = "Quality of Life"
	)
	var BETTER_TOWER_BEACON_UI: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Player Stats",
		description = "Displays a HUD graphic showing stats such as Speed and Defence in DoomTowers.",
		category = "General",
		subcategory = "Quality of Life"
	)
	var PLAYER_STATS: Boolean = false

	// General/Spam Hider

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Server Tips",
		description = "Hides messages such as the periodic Discord tip in chat.",
		category = "General",
		subcategory = "Spam Hider",
		hidden = true
	)
	var HIDE_SERVER_TIPS: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Worldguard Messages",
		description = "Hides the 'Hey! Sorry' Worldguard Messages",
		category = "General",
		subcategory = "Spam Hider"
	)
	var HIDE_WORLDGUARD_MESSAGES: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Set Requirement Messages",
		description = "Hides the 'You must be wearing [x] number of [Set] to use this item' messages",
		category = "General",
		subcategory = "Spam Hider"
	)
	var HIDE_SET_REQUIREMENT_MESSAGES: Boolean = false

	// General/World 4

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Next Cooking Item",
		description = "Displays the next required items for your order.",
		category = "General",
		subcategory = "World 4"
	)
	var SHOW_NEXT_COOKING_ITEM: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Stock Information",
		description = "Adds your stock to the Cooking HUD.",
		category = "General",
		subcategory = "World 4"
	)
	var SHOW_COOKING_STOCK_INFO: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Simplify Cooking Displays",
		description = "Shows Cooking displays in a more simplified form.",
		category = "General",
		subcategory = "World 4"
	)
	var SIMPLIFY_COOKING_DISPLAYS: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Cooking Timer",
		description = "Displays the time remaining to cook a burger above the stove.",
		category = "General",
		subcategory = "World 4"
	)
	var COOKING_TIMER: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Order Tracker",
		description = "Displays a HUD graphic showing the total number of requests, completions and completion rate of each order in World 4.",
		category = "General",
		subcategory = "World 4"
	)
	var COOKING_ORDER_TRACKER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Handled Chat Messages",
		description = "Hides chat messages displayed by Cooking features",
		category = "General",
		subcategory = "World 4"
	)
	var HIDE_HANDLED_COOKING_MESSAGES: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Auto Apply Bread",
		description = "Normally, Bread is not removed when completing an order, but can sometimes bug out and lock the order. As a result, Vice automatically deselects Bread for each new order, but this functionality can be bypassed with this feature.\n§eCan bug out from time to time!§8 (no clue why)",
		category = "General",
		subcategory = "World 4"
	)
	var AUTO_APPLY_BREAD: Boolean = false

	// General/Journey to the Glitch HQ

	@Property(
		type = PropertyType.SWITCH,
		name = "Evan Notification",
		description = "Reminds you when your cooldown for Evan's Quiz has passed.",
		category = "General",
		subcategory = "Journey to the Glitch HQ"
	)
	var EVAN_NOTIFICATION: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Evan Solver",
		description = "Solves Evan minigame in the Glitch HQ. Changes the title to 'TRUE' or 'FALSE' depending on the answer.",
		category = "General",
		subcategory = "Journey to the Glitch HQ",
		hidden = true
	)
	var GLITCH_HQ_EVAN_SOLVER: Boolean = false

	// General/Lost In Time

	@Property(
		type = PropertyType.SWITCH,
		name = "Cave-In Prediction",
		description = "Shows a prediction of the time until the next Cave-In when mining in the Soulswift Sands.",
		category = "General",
		subcategory = "Lost in Time"
	)
	var LOST_IN_TIME_CAVE_PREDICTION: Boolean = true

	// General/Fishing

	@Property(
		type = PropertyType.SWITCH,
		name = "Fishing Bite Ding",
		description = "Plays a unique ding sound and title when your bobber is bitten.",
		category = "General",
		subcategory = "Fishing"
	)
	var FISHING_DING: Boolean = true


	// Abilities/Quality of Life

	@Property(
		type = PropertyType.SELECTOR,
		name = "Display Cooldown Titles",
		description = "Change the title displayed when using an item on Cooldown.",
		category = "Abilities",
		subcategory = "Quality of Life",
		options = ["Normal", "Action Bar", "Hidden"]
	)
	var ITEM_COOLDOWN_TITLE_TYPE: Int = 0

	@Property(
		type = PropertyType.SWITCH,
		name = "Wrong Set Indicator",
		description = "Display a red overlay on an item if you do not have the required Set equipped.",
		category = "Abilities",
		subcategory = "Quality of Life"
	)
	var WRONG_SET_INDICATOR: Boolean = false

	// Abilities/Cooldown Overlay

	@Property(
		type = PropertyType.SWITCH,
		name = "Item Cooldown Display",
		description = "Highlights a hotbar slot when on cooldown, and can display a HUD graphic. Customizable when enabled!",
		category = "Abilities",
		subcategory = "Cooldown Overlay"
	)
	var ITEM_COOLDOWN_DISPLAY: Boolean = false

	@Property(
		type = PropertyType.SELECTOR,
		name = "Cooldown Display Type",
		description = "Changes how the Item Cooldown Display is shown.",
		category = "Abilities",
		subcategory = "Cooldown Overlay",
		options = ["Vanilla", "Static Background", "Color Fade", "Percentage Based", "Text Only"]
	)
	var ITEMCD_DISPLAY_TYPE: Int = 1

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Timer/Ready Text in Hotbar",
		description = "Displays a text stating the cooldown remaining or when ready.",
		category = "Abilities",
		subcategory = "Cooldown Overlay"
	)
	var SHOW_ITEMCD_TEXT: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Timer/Ready Text near Crosshair",
		description = "Displays a text stating the cooldown remaining or when ready near your crosshair.",
		category = "Abilities",
		subcategory = "Cooldown Overlay"
	)
	var SHOW_ITEMCD_TEXT_CROSSHAIR: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide when Ready",
		description = "Instead of highlighting as Green, hides the Overlay.",
		category = "Abilities",
		subcategory = "Cooldown Overlay"
	)
	var HIDE_ITEMCD_WHEN_READY: Boolean = false

	@Property(
		type = PropertyType.PERCENT_SLIDER,
		name = "Background Opacity",
		description = "Opacity for any item backgrounds rendered in the Item Cooldown Display.",
		category = "Abilities",
		subcategory = "Cooldown Overlay",
		minF = 0.01f,
		maxF = 1f
	)
	var ITEMCD_BACKGROUND_OPACITY: Float = 0.2f

	// Arenas/Quality of Life

	@Property(
		type = PropertyType.SWITCH,
		name = "Arenas Cooldown Notification",
		description = "Sends a chat notification when an Arena cooldown has expired.",
		category = "Arenas",
		subcategory = "Quality of Life"
	)
	var ARENAS_COOLDOWN_NOTIFIER: Boolean = true


	// Arenas/Live Arena Info

	@Property(
		type = PropertyType.SWITCH,
		name = "Live Arena Information",
		description = "Display useful statistics during an Arena session. At base, displays current Wave.",
		category = "Arenas",
		subcategory = "Live Arena Info"
	)
	var LIVE_ARENA_TOGGLE: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Display Mobs Remaining",
		description = "Adds a Mobs remaining stat to the Live Arena Information.",
		category = "Arenas",
		subcategory = "Live Arena Info"
	)
	var LIVE_ARENA_MOBS: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Display Wave Timer",
		description = "Adds the time remaining for the wave to the Live Arena Information.",
		category = "Arenas",
		subcategory = "Live Arena Info"
	)
	var LIVE_ARENA_ROUND_TIMER: Boolean = true

	@Property(
		type = PropertyType.SELECTOR,
		name = "Display Projected Drops",
		description = "Adds drops to Live Arena Information.\nBasic Drops: Amethyst, Polar Fur, Glowing Matter, etc\nUnique Drops: Chance for Galactic Hand Cannon, Arctic Scroll, etc",
		category = "Arenas",
		subcategory = "Live Arena Info",
		options = ["None", "Basic Drops Only", "Unique Drops Only", "All"]
	)
	var LIVE_ARENA_DROPS: Int = 2

	@Property(
		type = PropertyType.SWITCH,
		name = "Mob Effects Notification",
		description = "Sends a chat message when mobs gain certain potion effects during an Arena.",
		category = "Arenas",
		subcategory = "Live Arena Info"
	)
	var ARENAS_MOB_EFFECT_NOTIFICATION: Boolean = true

	// Auxiliary/Exonitas

	@Property(
		type = PropertyType.SWITCH,
		name = "Power Box Timer",
		description = "Draws a HUD graphic displaying the time of power boxes in Exonitas.",
		category = "Auxiliary",
		subcategory = "Exonitas"
	)
	var EXONITAS_POWER_BOX_TIMER: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Bloat Messages",
		description = "Hides the chat and titles when entering a level in Exonitas.",
		category = "Auxiliary",
		subcategory = "Exonitas"
	)
	var HIDE_BLOAT_EXONITAS_MESSAGES: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Baby Mode",
		description = "Turns nearby players into babies when in Exonitas, useful for the Security segment.\n§eDoes not change player hitboxes!",
		category = "Auxiliary",
		subcategory = "Exonitas"
	)
	var EXONITAS_BABY_MODE: Boolean = true

	// Bosses/Quality of Life

	@Property(
		type = PropertyType.SWITCH,
		name = "Boss Despawn Timers",
		description = "Adjusts the Bossbar to display the despawn timer of the boss.",
		category = "Bosses",
		subcategory = "Quality of Life"
	)
	var BOSS_DESPAWN_TIMERS: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Low Time Warning",
		description = "Displays a warning when the Boss is relatively close to despawning.",
		category = "Bosses",
		subcategory = "Quality of Life"
	)
	var BOSS_DESPAWN_WARNING: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Boss Counter",
		description = "Displays the number of times you have killed each boss.",
		category = "Bosses",
		subcategory = "Quality of Life"
	)
	var BOSS_COUNTER: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Boss Counter outside Boss Arenas",
		description = "Show the boss counter even when outside Boss Arenas.",
		category = "Bosses",
		subcategory = "Quality of Life"
	)
	var BOSS_COUNTER_OUTSIDE: Boolean = false

	// Bosses/Wasteyard

	@Property(
		type = PropertyType.SWITCH,
		name = "Wasteyard Timer",
		description = "Timer for the Wasteyard cooldown, useful for grinding runs.\nOnly shows when a run has been completed within the last 5 minutes.",
		category = "Bosses",
		subcategory = "Wasteyard"
	)
	var WASTEYARD_TIMER: Boolean = true

	@Property(
		type = PropertyType.TEXT,
		name = "Wasteyard Timer Sound",
		description = "Sound that is played when the Wasteyard is available.\nSearch up 'minecraft sound ids' for a list of sounds.",
		category = "Bosses",
		subcategory = "Wasteyard"
	)
	var WASTEYARD_TIMER_SOUND: String = "entity.arrow.hit_player"

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Wasteyard Timer Sound Pitch",
		description = "Pitch of the reminder.",
		category = "Bosses",
		subcategory = "Wasteyard",
		minF = 0.1f,
		maxF = 2.0f
	)
	var WASTEYARD_TIMER_PITCH: Float = 1.0f

	// Bosses/Abyssal Vice

	@Property(
		type = PropertyType.SWITCH,
		name = "Abyssal Vice Laser Warning",
		description = "Shows a warning title when Abyssal Vice is about to shoot its laser.",
		category = "Bosses",
		subcategory = "Abyssal Vice"
	)
	var ABYSSAL_VICE_LASER_WARNING: Boolean = true

	// Expeditions
	@Property(
		type = PropertyType.SWITCH,
		name = "Run Overview",
		description = "Displays stats about your run, like Tokens, Current Room, and more.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	var EXPEDITION_OVERVIEW: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Double-tap to Drop Valuable Items",
		description = "Makes items that are valuable in Expeditions require you to press the drop key twice to actually drop.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	var EXPEDITION_ITEM_PROTECTION: Boolean = true

	@Property(
		type = PropertyType.SELECTOR,
		name = "Item Protection Threshold",
		description = "The minimum rarity that requires being dropped twice.",
		category = "Expeditions",
		subcategory = "Quality of Life",
		options = ["Common", "Uncommon", "Rare", "Epic", "Legendary", "Mythical"]
	)
	var EXPEDITION_ITEM_PROTECTION_THRESHOLD: Int = 3

	@Property(
		type = PropertyType.SWITCH,
		name = "Merchants Overlay",
		description = "Shows a list of all found Merchants and their live prices during Expeditions.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	var EXPEDITION_MERCHANT_OVERLAY: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Room Waypoints",
		description = "Displays the room number, type, and merchant status on the doors during Expeditions. Useful for backtracking to rooms with Merchants.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	var EXPEDITION_ROOM_WAYPOINTS: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Defibrillator Use Counter",
		description = "Displays the number of uses a defibrillator has directly in the hotbar.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	var DEFIB_COUNTER: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Style Points",
		description = "Hides and mutes style point sound effects.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	var HIDE_EXPEDITION_STYLE_POINTS: Boolean = false

	@Property(
		type = PropertyType.SWITCH,
		name = "Auto Relay Information",
		description = "Automatically sends and reads information about your Expedition between your teammates.",
		category = "Expeditions",
		subcategory = "Information Sharing"
	)
	var AUTO_COMMUNICATE_EXPEDITION_INFO: Boolean = true

	@Property(
		type = PropertyType.SWITCH,
		name = "Filter Communications",
		description = "Hides Expedition communications from chat.\nWorks with Information Relaying disabled.",
		category = "Expeditions",
		subcategory = "Information Sharing"
	)
	var FILTER_EXPEDITION_COMMUNICATIONS: Boolean = true

	// Sounds

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "8-Bit Katana Volume",
		category = "Sounds",
		maxF = 2f
	)
	var EIGHT_BIT_KATANA_VOLUME: Float = 1f

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Glitch Mallet Volume",
		category = "Sounds",
		maxF = 2f
	)
	var GLITCH_MALLET_VOLUME: Float = 1f

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Arctic Core Volume",
		category = "Sounds",
		maxF = 2f
	)
	var ARCTIC_CORE_VOLUME: Float = 1f

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Barbed Shotgun Volume",
		category = "Sounds",
		maxF = 2f
	)
	var BARBED_SHOTGUN_VOLUME: Float = 1f

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Bedrock Breaker Volume",
		description = "§eMay have inaccuracies with the 'regenerating' (beacon) sounds.",
		category = "Sounds",
		maxF = 2f
	)
	var BEDROCK_BREAKER_VOLUME: Float = 1f

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Laser Point Minigun Volume",
		category = "Sounds",
		maxF = 2f
	)
	var LASER_POINT_MINIGUN_VOLUME: Float = 1f

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Snowball Cannon Volume",
		category = "Sounds",
		maxF = 2f
	)
	var SNOWBALL_CANNON_VOLUME: Float = 1f

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Shadow Gelato Drum Gun Volume",
		category = "Sounds",
		maxF = 2f
	)
	var SHADOW_DRUM_GUN_VOLUME: Float = 1f

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Jynx's Chain Gun Volume",
		category = "Sounds",
		maxF = 2f
	)
	var JYNX_CHAIN_GUN_VOLUME: Float = 1f

	fun init() {
		initialize()
		markDirty()

		addDependency("TRAIN_TIMER_OUTSIDE", "TRAIN_TIMER")
		addDependency("BOSS_COUNTER_OUTSIDE", "BOSS_COUNTER")

		addDependency("EXPEDITION_ITEM_PROTECTION_THRESHOLD", "EXPEDITION_ITEM_PROTECTION")

		addDependency("ITEMCD_DISPLAY_TYPE", "ITEM_COOLDOWN_DISPLAY")
		addDependency("SHOW_ITEMCD_TEXT", "ITEM_COOLDOWN_DISPLAY")
		addDependency("SHOW_ITEMCD_TEXT_CROSSHAIR", "ITEM_COOLDOWN_DISPLAY")
		addDependency("HIDE_ITEMCD_WHEN_READY", "ITEMCD_DISPLAY_TYPE") { value: Int -> ITEM_COOLDOWN_DISPLAY && value != 0 && value != 4 }
		addDependency("ITEMCD_BACKGROUND_OPACITY", "ITEM_COOLDOWN_DISPLAY")

		addDependency("LIVE_ARENA_MOBS", "LIVE_ARENA_TOGGLE")
		addDependency("LIVE_ARENA_ROUND_TIMER", "LIVE_ARENA_TOGGLE")
		addDependency("LIVE_ARENA_DROPS", "LIVE_ARENA_TOGGLE")

	}

	override val migrations: List<Migration>
		get() = listOf(
			Migration { config -> // Migration 1: Item Cooldowns to new category
				config.move("general.item_cooldowns.item_cooldown_display", "abilities.cooldown_overlay.item_cooldown_display")
				config.move("general.item_cooldowns.cooldown_display_type", "abilities.cooldown_overlay.cooldown_display_type")
				config.move("general.item_cooldowns.show_timer/ready_text_near_crosshair", "abilities.cooldown_overlay.show_timer/ready_text_near_crosshair")
				config.move("general.item_cooldowns.show_timer/ready_text_in_hotbar", "abilities.cooldown_overlay.show_timer/ready_text_in_hotbar")
				config.move("general.item_cooldowns.background_opacity", "abilities.cooldown_overlay.background_opacity")
				config.move("general.item_cooldowns.hide_when_ready", "abilities.cooldown_overlay.hide_when_ready")
			}
		)

	private fun MutableMap<String, Any?>.move(original: String, new: String) {
		this[original]?.let {
			this[new] = it
			remove(original)
		}
	}

	class ConfigSorting : SortingBehavior() {
		override fun getCategoryComparator(): Comparator<in Category> {
			return Comparator { o1: Category, o2: Category ->
				return@Comparator when {
					o1.name == "General" -> -1
					o2.name == "General" -> 1
					else -> o1.name.compareTo(o2.name)
				}
			}
		}

		override fun getSubcategoryComparator(): Comparator<in Map.Entry<String, List<PropertyData>>> {
			return Comparator { entry1, entry2 ->
				val key1 = entry1.key
				val key2 = entry2.key

				// This places Vice & Quality of Life at the top, and Developer at the bottom.
				val keyOrderMap = hashMapOf(
					"Vice" to -2,
					"Quality of Life" to -1,
					"Developer" to 1
				)

				val order1 = keyOrderMap.getOrDefault(key1, 0)
				val order2 = keyOrderMap.getOrDefault(key2, 0)

				if (order1 != order2) {
					return@Comparator order1.compareTo(order2)
				} else {
					return@Comparator key1.compareTo(key2)
				}
			}
		}
	}
}
