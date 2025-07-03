package net.oxyopia.vice.config;

import gg.essential.universal.UDesktop;
import gg.essential.universal.UScreen;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import net.oxyopia.vice.Vice;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URI;
import java.util.*;
import java.util.List;

public class Config extends Vigilant {

	@SuppressWarnings("unused")
	@Property(
		type = PropertyType.BUTTON,
		name = "Edit HUD Locations",
		description = """
			Edit the positions of HUD Elements:
			 - Drag to reposition. §8(WASD/Arrow Keys)§7
			 - Scroll to scale. §8(+/-)§7
			 - Middle Click to toggle centering. §8(TAB)§7
			 - Press H or V to center horizontally/vertically.""",
		category = "General",
		subcategory = "Vice",
		placeholder = "Edit HUD Locations"
	)
	public void EDIT_HUD_LOCATIONS() {
		UScreen.displayScreen(HudEditor.INSTANCE);
	}

	@Property(
		type = PropertyType.SWITCH,
		name = "HUD Text Shadow",
		description = "Toggle text shadow on Vice HUD elements.",
		category = "General",
		subcategory = "Vice"
	)
	public boolean HUD_TEXT_SHADOW = true;


	@SuppressWarnings("unused")
	@Property(
		type = PropertyType.BUTTON,
		name = "Vice Discord",
		description = "Join the Vice Discord to receive updates, post suggestions, and more!",
		category = "General",
		subcategory = "Vice",
		placeholder = "Join"
	)
	public void JOIN_VICE_DISCORD() {
		UDesktop.browse(URI.create("https://discord.gg/7nb9KcZHug"));
	}

	// General/Developer

	@Property(
		type = PropertyType.SWITCH,
		name = "Developer Mode",
		description = "dev mode for beta versions/debugging\n§cOnly enable if you know what you're doing!",
		category = "General",
		subcategory = "Developer"
	)
	public boolean DEVMODE = false;

	@SuppressWarnings("unused")
	@Property(
		type = PropertyType.BUTTON,
		name = "Dev Menu",
		description = "Open the developer menu",
		category = "General",
		subcategory = "Developer",
		placeholder = "Open"
	)
	public void OPEN_DEV_CONFIG() {
		UScreen.displayScreen(Vice.devConfig.gui());
	}

	@Property(
		type = PropertyType.SELECTOR,
		name = "Gaming Mode",
		description = "Choose your epic gaming mode for optimal gaming performance.",
		category = "General",
		subcategory = "Developer",
		options = {"None", "Vice", "DoomTowers smashing", "msmdude", "digmonireland", "Trump", "clive", "law Abiding Citizen", "meme", "african digi"}
	)
	public int DEV_GAMING_MODE = 0;

	@Property(
		type = PropertyType.SWITCH,
		name = "Baby Mode",
		description = "Baby mode from other thing but everywhere\n§eDoes not change hitboxes! Works globally!",
		category = "General",
		subcategory = "Developer"
	)
	public boolean DEV_BABY_MODE = false;

	// General/Quality of Life

	@Property(
		type = PropertyType.SWITCH,
		name = "Prevent Consuming Items",
		description = "Blocks block place packets when using consumable items, like Player Heads or Train Keys.\nItem Abilities still activate when enabled.",
		category = "General",
		subcategory = "Quality of Life"
	)
	public boolean PREVENT_PLACING_PLAYER_HEADS = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Backpack Renaming",
		description = "Master toggle for Backpack Renaming.\nUse /vicebackpackrename (name|reset) to change!\nNormal Minecraft Color codes are valid.",
		category = "General",
		subcategory = "Quality of Life"
	)
	public boolean BACKPACK_RENAMING = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Trash Protection",
		description = "Blocks clicking valuable items while inside the Trash menu.\nUse /viceprotectitem to protect any item.\n§eHold LCONTROL while clicking to bypass!",
		category = "General",
		subcategory = "Quality of Life",
		searchTags = {"dispose", "disposal", "protect"}
	)
	public boolean TRASH_PROTECTION = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Better Warp Menu",
		description = "Adds the Worlds and Boss to the tooltip of each floor in the /warp menu.",
		category = "General",
		subcategory = "Quality of Life"
	)
	public boolean BETTER_WARP_MENU = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Player Stats",
		description = "Displays a HUD graphic showing stats such as Speed and Defence.",
		category = "General",
		subcategory = "Quality of Life"
	)
	public boolean PLAYER_STATS = false;

	@Property(
			type = PropertyType.SWITCH,
			name = "Crimson Cosmonaut Timer",
			description = "Shows how long until the next Crimson Cosmonaut.",
			category = "General",
			subcategory = "The Shattered Sector"
	)
	public boolean CRIMSON_COSMONAUT_TIMER = false;


	// General/Spam Hider

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Worldguard Messages",
		description = "Hides the 'Hey! Sorry' Worldguard Messages",
		category = "General",
		subcategory = "Spam Hider"
	)
	public boolean HIDE_WORLDGUARD_MESSAGES = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Set Requirement Messages",
		description = "Hides the 'You must be wearing [x] number of [Set] to use this item' messages",
		category = "General",
		subcategory = "Spam Hider"
	)
	public boolean HIDE_SET_REQUIREMENT_MESSAGES = false;

	// General/Mining

	@Property(
		type = PropertyType.SWITCH,
		name = "Cave-In Prediction",
		description = "Show a prediction of the time until the next Cave-In when mining in dynamic mining areas.",
		category = "General",
		subcategory = "Mining"
	)
	public boolean CAVE_IN_PREDICTION = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Adjust Dynamic Mining Bossbar",
		description = "Adjust the cave-in bossbar to show the progress to the next cave in.",
		category = "General",
		subcategory = "Mining"
	)
	public boolean ADJUST_DYNAMIC_MINING_BOSSBAR = true;

	// General/Fishing

	@Property(
		type = PropertyType.SWITCH,
		name = "Fishing Bite Ding",
		description = "Plays a unique ding sound and title when your bobber is bitten.",
		category = "General",
		subcategory = "Fishing"
	)
	public boolean FISHING_DING = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Brew Timer",
		description = "Shows a timer for the effects of consumables like the Fishy Brew.",
		category = "General",
		subcategory = "Fishing"
	)
	public boolean FISHING_BREW_TIMER = true;

	// Abilities/Quality of Life

	@Property(
		type = PropertyType.SELECTOR,
		name = "Display Cooldown Titles",
		description = "Change the title displayed when using an item on Cooldown.",
		category = "Abilities",
		subcategory = "Quality of Life",
		options = {"Normal", "Action Bar", "Hidden"}
	)
	public int ITEM_COOLDOWN_TITLE_TYPE = 0;
	
	@Property(
		type = PropertyType.SWITCH,
		name = "Show Extra Info in Tooltip",
		description = "Shows extra info, such as Cooldowns and increased Damage, in the tooltip of ability items.",
		category = "Abilities",
		subcategory = "Quality of Life"
	)
	public boolean SHOW_EXTRA_ABILITY_INFO = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Wrong Set Indicator",
		description = "Display an orange overlay on a hotbar item if you do not have the required Set equipped.",
		category = "Abilities",
		subcategory = "Quality of Life"
	)
	public boolean WRONG_SET_INDICATOR = false;

	// Abilities/Cooldown Overlay

	@Property(
		type = PropertyType.SWITCH,
		name = "Item Cooldown Display",
		description = "Highlights a hotbar slot when on cooldown, and can display a HUD graphic. Customizable when enabled!",
		category = "Abilities",
		subcategory = "Cooldown Overlay"
	)
	public boolean ITEM_COOLDOWN_DISPLAY = false;

	@Property(
		type = PropertyType.SELECTOR,
		name = "Cooldown Display Type",
		description = "Changes how the Item Cooldown Display is shown.",
		category = "Abilities",
		subcategory = "Cooldown Overlay",
		options = {"Vanilla", "Static Background", "Color Fade", "Percentage Based", "Durability", "Text Only"}
	)
	public int ITEMCD_DISPLAY_TYPE = 1;

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Timer/Ready Text in Hotbar",
		description = "Displays a text stating the cooldown remaining or when ready.",
		category = "Abilities",
		subcategory = "Cooldown Overlay"
	)
	public boolean SHOW_ITEMCD_TEXT = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Timer/Ready Text near Crosshair",
		description = "Displays a text stating the cooldown remaining or when ready near your crosshair.",
		category = "Abilities",
		subcategory = "Cooldown Overlay"
	)
	public boolean SHOW_ITEMCD_TEXT_CROSSHAIR = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide when Ready",
		description = "Instead of highlighting as Green, hides the Overlay.",
		category = "Abilities",
		subcategory = "Cooldown Overlay"
	)
	public boolean HIDE_ITEMCD_WHEN_READY = false;

	@Property(
		type = PropertyType.PERCENT_SLIDER,
		name = "Background Opacity",
		description = "Opacity for any item backgrounds rendered in the Item Cooldown Display.",
		category = "Abilities",
		subcategory = "Cooldown Overlay",
		minF = 0.01f,
		maxF = 1f
	)
	public float ITEMCD_BACKGROUND_OPACITY = 0.2f;

	// Abilities/Set Color Overlay

	@Property(
		type = PropertyType.SWITCH,
		name = "Set Colors in Inventory",
		description = "Highlights ability items in the inventory with their respective set color.",
		category = "Abilities",
		subcategory = "Set Colors"
	)
	public boolean INVENTORY_SET_COLORS = false;

	@Property(
		type = PropertyType.PERCENT_SLIDER,
		name = "Set Colors Opacity",
		description = "The opacity of Set Colors in Inventory.",
		category = "Abilities",
		subcategory = "Set Colors"
	)
	public float INVENTORY_SET_COLORS_OPACITY = 0.5f;

	@Property(
		type = PropertyType.SWITCH,
		name = "Include Armor in Set Colors",
		description = "Whether to highlight armor with their Set Color too.",
		category = "Abilities",
		subcategory = "Set Colors"
	)
	public boolean INCLUDE_ARMOR_IN_SET_COLORS = true;

	// Abilities/Specific

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Revolver Blindness",
		description = "Hides the blindness effect when aiming the revolver.",
		category = "Abilities",
		subcategory = "Specific"
	)
	public boolean HIDE_REVOLVER_BLINDNESS = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Ammo Counter",
		description = "Show the count of ammo in weapons like the AK-47 in the hotbar.",
		category = "Abilities",
		subcategory = "Specific"
	)
	public boolean AMMO_COUNTER = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Ammo Reload Warning",
		description = "Display a title when an ammo-based weapon needs to be reloaded.",
		category = "Abilities",
		subcategory = "Specific"
	)
	public boolean AMMO_RELOAD_TITLE = true;

	// Arenas/Quality of Life

	@Property(
		type = PropertyType.SWITCH,
		name = "Arenas Cooldown Notification",
		description = "Sends a chat notification when an Arena cooldown has expired.",
		category = "Arenas",
		subcategory = "Quality of Life",
		hidden = true
	)
	public boolean ARENAS_COOLDOWN_NOTIFIER = true;


	// Arenas/Live Arena Info
	@Property(
		type = PropertyType.SWITCH,
		name = "Live Arena Information",
		description = "Display useful statistics during an Arena session. At base, displays current Wave.",
		category = "Arenas",
		subcategory = "Live Arena Info",
		hidden = true
	)
	public boolean LIVE_ARENA_TOGGLE = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Display Mobs Remaining",
		description = "Adds a Mobs remaining stat to the Live Arena Information.",
		category = "Arenas",
		subcategory = "Live Arena Info",
		hidden = true
	)
	public boolean LIVE_ARENA_MOBS = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Display Wave Timer",
		description = "Adds the time remaining for the wave to the Live Arena Information.",
		category = "Arenas",
		subcategory = "Live Arena Info",
		hidden = true
	)
	public boolean LIVE_ARENA_ROUND_TIMER = true;

	@Property(
		type = PropertyType.SELECTOR,
		name = "Display Projected Drops",
		description = "Adds drops to Live Arena Information.\nBasic Drops: Amethyst, Polar Fur, Glowing Matter, etc\nUnique Drops: Chance for Galactic Hand Cannon, Arctic Scroll, etc",
		category = "Arenas",
		subcategory = "Live Arena Info",
		options = {"None", "Basic Drops Only", "Unique Drops Only", "All"},
		hidden = true
	)
	public int LIVE_ARENA_DROPS = 2;

	@Property(
		type = PropertyType.SWITCH,
		name = "Mob Effects Notification",
		description = "Sends a chat message when mobs gain certain potion effects during an Arena.",
		category = "Arenas",
		subcategory = "Live Arena Info",
		hidden = true
	)
	public boolean ARENAS_MOB_EFFECT_NOTIFICATION = true;

	// Auxiliary/Exonitas

	@Property(
		type = PropertyType.SWITCH,
		name = "Power Box Timer",
		description = "Draws a HUD graphic displaying the time of power boxes in Exonitas.",
		category = "Auxiliary",
		subcategory = "Exonitas"
	)
	public boolean EXONITAS_POWER_BOX_TIMER = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Bloat Messages",
		description = "Hides the chat and titles when entering a level in Exonitas.",
		category = "Auxiliary",
		subcategory = "Exonitas"
	)
	public boolean HIDE_BLOAT_EXONITAS_MESSAGES = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Baby Mode",
		description = "Turns nearby players into babies when in Exonitas, useful for the Security segment.\n§eDoes not change player hitboxes!",
		category = "Auxiliary",
		subcategory = "Exonitas"
	)
	public boolean EXONITAS_BABY_MODE = true;

	// Bosses/Quality of Life

	@Property(
		type = PropertyType.SWITCH,
		name = "Boss Despawn Timers",
		description = "Adjusts the Bossbar to display the despawn timer of the boss.",
		category = "Bosses",
		subcategory = "Quality of Life"
	)
	public boolean BOSS_DESPAWN_TIMERS = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Low Time Warning",
		description = "Displays a warning when the Boss is relatively close to despawning.",
		category = "Bosses",
		subcategory = "Quality of Life"
	)
	public boolean BOSS_DESPAWN_WARNING = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Mastery Tracker",
		description = "Displays the tier, count and progress to your Masteries while in the respective Boss Arena.",
		category = "Bosses",
		subcategory = "Quality of Life"
	)
	public boolean MASTERY_TRACKER = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Always show Mastery Tracker",
		description = "Always show the Mastery Tracker, even outside of Boss Arenas.",
		category = "Bosses",
		subcategory = "Quality of Life"
	)
	public boolean ALWAYS_SHOW_MASTERY_TRACKER = false;

	@Property(
		type = PropertyType.SELECTOR,
		name = "Default Mastery Tracker Boss",
		description = "The Mastery shown when outside a Mastery Boss arena.",
		category = "Bosses",
		subcategory = "Quality of Life",
		options = {"Most Recent", "Vice", "Wasteyard", "El Gelato", "PPP", "Minehut", "Elderpork"}
	)
	public int DEFAULT_MASTERY_BOSS = 0;

	@Property(
		type = PropertyType.SWITCH,
		name = "Boss Counter",
		description = "Displays the number of times you have killed each boss.",
		category = "Bosses",
		subcategory = "Quality of Life"
	)
	public boolean BOSS_COUNTER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Boss Counter outside Boss Arenas",
		description = "Show the boss counter even when outside Boss Arenas.",
		category = "Bosses",
		subcategory = "Quality of Life"
	)
	public boolean BOSS_COUNTER_OUTSIDE = false;

	// Bosses/Wasteyard

	@Property(
		type = PropertyType.SWITCH,
		name = "Wasteyard Timer",
		description = "Timer for the Wasteyard cooldown, useful for grinding runs.\nOnly shows when a run has been completed within the last 5 minutes.",
		category = "Bosses",
		subcategory = "Wasteyard"
	)
	public boolean WASTEYARD_TIMER = true;

	@Property(
		type = PropertyType.TEXT,
		name = "Wasteyard Timer Sound",
		description = "Sound that is played when the Wasteyard is available.\nSearch up 'minecraft sound ids' for a list of sounds.",
		category = "Bosses",
		subcategory = "Wasteyard"
	)
	public String WASTEYARD_TIMER_SOUND = "entity.arrow.hit_player";

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Wasteyard Timer Sound Pitch",
		description = "Pitch of the reminder.",
		category = "Bosses",
		subcategory = "Wasteyard",
		minF = 0.1f,
		maxF = 2.0f
	)
	public float WASTEYARD_TIMER_PITCH = 1.0f;

	// Bosses/Elderpork

	@Property(
		type = PropertyType.SWITCH,
		name = "Elderpork Start Timer",
		description = "Displays a timer during the window to enter the Elderpork Boss with the computer.",
		category = "Bosses",
		subcategory = "Elderpork"
	)
	public boolean ELDERPORK_START_TIMER = false;

	// Bosses/Vatican

	@Property(
			type = PropertyType.SWITCH,
			name = "THE VATICAN Start Timer",
			description = "Displays a timer during the window to enter The Vatican Boss with the head.",
			category = "Bosses",
			subcategory = "Vatican"
	)
	public boolean VATICAN_START_TIMER = false;

	// Bosses/Abyssal Vice

	@Property(
		type = PropertyType.SWITCH,
		name = "Abyssal Vice Laser Warning",
		description = "Shows a warning title when Abyssal Vice is about to shoot its laser.",
		category = "Bosses",
		subcategory = "Abyssal Vice"
	)
	public boolean ABYSSAL_VICE_LASER_WARNING = true;

	// Expeditions

	@Property(
		type = PropertyType.SWITCH,
		name = "Run Overview",
		description = "Displays stats about your run, like Tokens, Current Room, and more.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	public boolean EXPEDITION_OVERVIEW = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Double-tap to Drop Valuable Items",
		description = "Makes items that are valuable in Expeditions require you to press the drop key twice to actually drop.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	public boolean EXPEDITION_ITEM_PROTECTION = true;

	@Property(
		type = PropertyType.SELECTOR,
		name = "Item Protection Threshold",
		description = "The minimum rarity that requires being dropped twice.",
		category = "Expeditions",
		subcategory = "Quality of Life",
		options = {"Common", "Uncommon", "Rare", "Epic", "Legendary", "Contraband", "Mythical"}
	)
	public int EXPEDITION_ITEM_PROTECTION_THRESHOLD = 3;

	@Property(
		type = PropertyType.SWITCH,
		name = "Merchants Overlay",
		description = "Shows a list of all found Merchants and their live prices during Expeditions.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	public boolean EXPEDITION_MERCHANT_OVERLAY = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Room Waypoints",
		description = "Displays the room number, type, and merchant status on the doors during Expeditions. Useful for backtracking to rooms with Merchants.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	public boolean EXPEDITION_ROOM_WAYPOINTS = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Defibrillator Use Counter",
		description = "Displays the number of uses a defibrillator has directly in the hotbar.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	public boolean DEFIB_COUNTER = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Style Points",
		description = "Hides and mutes style point sound effects.",
		category = "Expeditions",
		subcategory = "Quality of Life"
	)
	public boolean HIDE_EXPEDITION_STYLE_POINTS = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Auto Relay Information",
		description = "Automatically sends and reads information about your Expedition between your teammates.",
		category = "Expeditions",
		subcategory = "Information Sharing"
	)
	public boolean AUTO_COMMUNICATE_EXPEDITION_INFO = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Filter Communications",
		description = "Hides Expedition communications from other Vice users in chat.\nWorks with Information Relaying disabled.",
		category = "Expeditions",
		subcategory = "Information Sharing"
	)
	public boolean FILTER_EXPEDITION_COMMUNICATIONS = true;


	// Event

	// Event/Pillars

	@Property(
		type = PropertyType.SWITCH,
		name = "Next Item Bossbar",
		description = "Draws a bossbar showing how long until the next Pillars item is given.",
		category = "Event",
		subcategory = "Pillars"
	)
	public boolean PILLARS_NEXT_ITEM_BOSSBAR = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Event Timers",
		description = "Timers for events like being able to start another match, or border phases.",
		category = "Event",
		subcategory = "Pillars"
	)
	public boolean PILLARS_EVENT_TIMERS = false;

	// Event/Summer

	@Property(
		type = PropertyType.SWITCH,
		name = "Fishing Drops Tracker",
		description = "Shows your Summer Fishing Drop counts.",
		category = "Event",
		subcategory = "Summer"
	)
	public boolean SUMMER_FISHING_TRACKER = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Fishing Tracker Globally",
		description = "Whether to show the Summer Fishing Tracker outside of the Summer world.",
		category = "Event",
		subcategory = "Summer"
	)
	public boolean SHOW_SUMMER_FISHING_TRACKER_GLOBALLY = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Summer Timers",
		description = "Shows timers for Violet's Exchange Refresh and Summer Minigames.",
		category = "Event",
		subcategory = "Summer"
	)
	public boolean SUMMER_TIMERS = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Bar Minigame Bossbar",
		description = "Shows a bossbar of the remaining time to complete the Bar Minigame.",
		category = "Event",
		subcategory = "Summer"
	)
	public boolean SUMMER_BAR_MINIGAME_BOSSBAR = false;

	// Event/Turkinator

	@Property(
		type = PropertyType.SWITCH,
		name = "Invasion Timer",
		description = "Shows how long until the next Turkinator Invasion, as well as where he last invaded.",
		category = "Event",
		subcategory = "Turkinator"
	)
	public boolean TURKINATOR_INVASION_TIMER = false;

	// Sounds

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "8-Bit Katana Volume",
		category = "Sounds",
		maxF = 2f
	)
	public float EIGHT_BIT_KATANA_VOLUME = 1f;

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Wave Pulser Volume",
		category = "Sounds",
		maxF = 2f
	)
	public float WAVE_PULSER_VOLUME = 1f;

	@Property(
		type = PropertyType.SWITCH,
		name = "Apply to Wave Pulser Volume to Evoker Fangs",
		description = "Wether the Evoker Fangs sound is affected by Wave Pulser Volume. Disable if you still want to know how many mobs were hit without getting earraped.",
		category = "Sounds"
	)
	public boolean APPLY_WAVE_PULSER_VOLUME_TO_FANGS = true;

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Glitch Mallet Volume",
		category = "Sounds",
		maxF = 2f
	)
	public float GLITCH_MALLET_VOLUME = 1f;

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Arctic Core Volume",
		category = "Sounds",
		maxF = 2f
	)
	public float ARCTIC_CORE_VOLUME = 1f;

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Barbed Shotgun Volume",
		category = "Sounds",
		maxF = 2f
	)
	public float BARBED_SHOTGUN_VOLUME = 1f;

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Bedrock Breaker Volume",
		description = "§eMay have inaccuracies with the 'regenerating' (beacon) sounds.",
		category = "Sounds",
		maxF = 2f
	)
	public float BEDROCK_BREAKER_VOLUME = 1f;

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Laser Point Minigun Volume",
		category = "Sounds",
		maxF = 2f
	)
	public float LASER_POINT_MINIGUN_VOLUME = 1f;

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Snowball Cannon Volume",
		category = "Sounds",
		maxF = 2f
	)
	public float SNOWBALL_CANNON_VOLUME = 1f;

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Shadow Gelato Drum Gun Volume",
		category = "Sounds",
		maxF = 2f
	)
	public float SHADOW_DRUM_GUN_VOLUME = 1f;

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Jynx's Chain Gun Volume",
		category = "Sounds",
		maxF = 2f
	)
	public float JYNX_CHAIN_GUN_VOLUME = 1f;

	// Worlds/Fastest Food

	@Property(
		type = PropertyType.SWITCH,
		name = "Cooking Helper",
		description = "Displays the next two required ingredients for your order.",
		category = "Worlds",
		subcategory = "Fastest Food"
	)
	public boolean COOKING_HELPER = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Succeeding Ingredients",
		description = "Show the next two ingredients rather than one to make it easier to prepare to click the next one. (its crazy ik)",
		category = "Worlds",
		subcategory = "Fastest Food"
	)
	public boolean SHOW_SUCCEEDING_INGREDIENTS = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Cooking Timer",
		description = "Displays the time remaining to cook a burger above the stove.",
		category = "Worlds",
		subcategory = "Fastest Food"
	)
	public boolean COOKING_TIMER = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Order Tracker",
		description = "Displays a HUD graphic showing the total number of requests, completions and completion rate of each order in World 4.",
		category = "Worlds",
		subcategory = "Fastest Food"
	)
	public boolean COOKING_ORDER_TRACKER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Hide Handled Chat Messages",
		description = "Hides chat messages displayed by Cooking features",
		category = "Worlds",
		subcategory = "Fastest Food"
	)
	public boolean HIDE_HANDLED_COOKING_MESSAGES = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Auto Apply Bread",
		description = "Normally, Bread is not removed when completing an order, but can sometimes bug out and lock the order. As a result, Vice automatically deselects Bread for each new order, but this functionality can be bypassed with this feature.\n§eCan bug out from time to time!§8 (no clue why)",
		category = "Worlds",
		subcategory = "Fastest Food"
	)
	public boolean AUTO_APPLY_BREAD = false;

	// World/Bitter Battleground

	@Property(
		type = PropertyType.SWITCH,
		name = "No Yeti Head Warning",
		description = "Displays a warning when trying to fish in the Bitter Battleground without wearing a Yeti Head.",
		category = "Worlds",
		subcategory = "Bitter Battleground"
	)
	public boolean NO_YETI_HEAD_WARNING = false;

	// Worlds/Magma Heights

	@Property(
		type = PropertyType.SWITCH,
		name = "Forge Timers",
		description = "Displays the time remaining for items in the Forge to smelt.",
		category = "Worlds",
		subcategory = "Magma Heights"
	)
	public boolean FORGE_TIMERS = true;

	// Worlds/Showdown

	@Property(
		type = PropertyType.SWITCH,
		name = "Train Timer",
		description = "Displays a HUD graphic showing information about the Train in World 11.",
		category = "Worlds",
		subcategory = "Showdown"
	)
	public boolean TRAIN_TIMER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Train Timer outside World 11",
		description = "Show the train timer while outside World 11, yeah",
		category = "Worlds",
		subcategory = "Showdown"
	)
	public boolean TRAIN_TIMER_OUTSIDE = false;


	// Worlds/Journey to the Glitch HQ

	@Property(
		type = PropertyType.SWITCH,
		name = "Evan Notification",
		description = "Reminds you when your cooldown for Evan's Quiz has passed.",
		category = "Worlds",
		subcategory = "Journey to the Glitch HQ"
	)
	public boolean EVAN_NOTIFICATION = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Evan Solver",
		description = "Solves Evan minigame in the Glitch HQ. Changes the title to 'TRUE' or 'FALSE' depending on the answer.",
		category = "Worlds",
		subcategory = "Journey to the Glitch HQ",
		hidden = true
	)
	public boolean GLITCH_HQ_EVAN_SOLVER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Delivery Timer",
		description = "Shows bossbar timers related to the Delivery, including the Van Spawn and Remaining Delivery Time.",
		category = "Worlds",
		subcategory = "Journey to the Glitch HQ"
	)
	public boolean GLITCH_HQ_DELIVERY_TIMER = true;

	// Worlds/Starry Streets

	@Property(
		type = PropertyType.SWITCH,
		name = "Cheese Highlight",
		description = "Highlights all tradeable items for cheese whilst in the Cheese Exchange.",
		category = "Worlds",
		subcategory = "Starry Streets"
	)
	public boolean STARRY_STREETS_CHEESE_HIGHLIGHT = true;

	@Property(
		type = PropertyType.SWITCH,
		name = "Cheese Display",
		description = "Counts all cheese-tradeable items in your inventory and their total value.",
		category = "Worlds",
		subcategory = "Starry Streets",
		hidden = true
	)
	public boolean STARRY_STREETS_CHEESE_DISPLAY = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Star Waypoints",
		description = "Displays text in the air above where stars have dropped.",
		category = "Worlds",
		subcategory = "Starry Streets"
	)
	public boolean STAR_WAYPOINTS = true;


	public Config() {
		super(new File("./config/vice/config.toml"), "Vice §b" + Vice.Companion.getVersion(), new JVMAnnotationPropertyCollector(), new ConfigSorting());
	}

	public void init() {
		initialize();
		markDirty();

		addDependency("TRAIN_TIMER_OUTSIDE", "TRAIN_TIMER");

		addDependency("DEFAULT_MASTERY_BOSS", "ALWAYS_SHOW_MASTERY_TRACKER");

		addDependency("EXPEDITION_ITEM_PROTECTION_THRESHOLD", "EXPEDITION_ITEM_PROTECTION");

		addDependency("ITEMCD_DISPLAY_TYPE", "ITEM_COOLDOWN_DISPLAY");
		addDependency("SHOW_ITEMCD_TEXT", "ITEM_COOLDOWN_DISPLAY");
		addDependency("SHOW_ITEMCD_TEXT_CROSSHAIR", "ITEM_COOLDOWN_DISPLAY");
		addDependency("HIDE_ITEMCD_WHEN_READY", "ITEMCD_DISPLAY_TYPE", (value) -> ITEM_COOLDOWN_DISPLAY && (int) value != 0 && (int) value != 5);
		addDependency("ITEMCD_BACKGROUND_OPACITY", "ITEM_COOLDOWN_DISPLAY");
		addDependency("INVENTORY_SET_COLORS_OPACITY", "INVENTORY_SET_COLORS");
		addDependency("INCLUDE_ARMOR_IN_SET_COLORS", "INVENTORY_SET_COLORS");

		addDependency("LIVE_ARENA_MOBS", "LIVE_ARENA_TOGGLE");
		addDependency("LIVE_ARENA_ROUND_TIMER", "LIVE_ARENA_TOGGLE");
		addDependency("LIVE_ARENA_DROPS", "LIVE_ARENA_TOGGLE");

		addDependency("APPLY_WAVE_PULSER_VOLUME_TO_FANGS", "WAVE_PULSER_VOLUME", (value) -> (float) value != 1f);
	}

	public @NotNull List<Migration> getMigrations() {
		List<Migration> migrations = new ArrayList<>();
		migrations.add(config -> { // Migration 1: Item Cooldowns to new category
			move(config, "general.item_cooldowns.item_cooldown_display", "abilities.cooldown_overlay.item_cooldown_display");
			move(config, "general.item_cooldowns.cooldown_display_type", "abilities.cooldown_overlay.cooldown_display_type");
			move(config, "general.item_cooldowns.show_timer/ready_text_near_crosshair", "abilities.cooldown_overlay.show_timer/ready_text_near_crosshair");
			move(config, "general.item_cooldowns.show_timer/ready_text_in_hotbar", "abilities.cooldown_overlay.show_timer/ready_text_in_hotbar");
			move(config, "general.item_cooldowns.background_opacity", "abilities.cooldown_overlay.background_opacity");
			move(config, "general.item_cooldowns.hide_when_ready", "abilities.cooldown_overlay.hide_when_ready");
		});
		migrations.add(config -> { // Migration 2: World specific stuff to new category
			move(config, "general.quality_of_life.forge_timers", "worlds.magma_heights.forge_timers");
			move(config, "general.lost_in_time.cave-in_prediction", "worlds.lost_in_time.cave-in_prediction");
			move(config, "general.journey_to_the_glitch_hq.evan_solver", "worlds.journey_to_the_glitch_hq.evan_solver");
			move(config, "general.journey_to_the_glitch_hq.evan_notification", "worlds.journey_to_the_glitch_hq.evan_notification");
			move(config, "general.world_4.show_next_cooking_item", "worlds.fastest_food.cooking_helper");
			move(config, "general.world_4.cooking_timer", "worlds.fastest_food.cooking_timer");
			move(config, "general.world_4.order_tracker", "worlds.fastest_food.order_tracker");
			move(config, "general.world_4.hide_handled_chat_messages", "worlds.fastest_food.hide_handled_chat_messages");
			move(config, "general.world_4.auto_apply_bread", "worlds.fastest_food.auto_apply_bread");
			move(config, "general.quality_of_life.train_timer", "worlds.showdown.train_timer");
			move(config, "general.quality_of_life.show_train_timer_outside_world_11", "worlds.showdown.show_train_timer_outside_world_11");
		});
		migrations.add(config -> { // Migration 3: Cherry Overlay -> Summer Timers
			move(config, "event.summer.violet's_exchange_overlay", "event.summer.summer_timers");
		});
		migrations.add(config -> { // Migration 4: Generalise Cave In Prediction
			move(config, "worlds.lost_in_time.cave-in_prediction", "general.mining.cave-in_prediction");
		});
		migrations.add(config -> { // Migration 5: Move ability related config options to Abilities/Specific
			move(config, "general.quality_of_life.hide_revolver_blindness", "abilities.specific.hide_revolver_blindness");
		});
		return migrations;
	}

	private void move(Map<String, Object> map, String original, String newKey) {
		if (map.containsKey(original)) {
			map.put(newKey, map.get(original));
			map.remove(original);
		}
	}

	public static class ConfigSorting extends SortingBehavior {
		@NotNull
		@Override
		public Comparator<? super Category> getCategoryComparator() {
			return (o1, o2) -> {
				if ("General".equals(o1.getName())) {
					return -1;
				}
				if ("General".equals(o2.getName())) {
					return 1;
				} else {
					return Comparator.comparing(Category::getName).compare(o1, o2);
				}
			};
		}

		@NotNull
		@Override
		public Comparator<? super Map.Entry<String, ? extends List<PropertyData>>> getSubcategoryComparator() {
			return (entry1, entry2) -> {
				String key1 = entry1.getKey();
				String key2 = entry2.getKey();

				// This places Vice & Quality of Life at the top, and Developer at the bottom.
				Map<String, Integer> keyOrderMap = new HashMap<>();
				keyOrderMap.put("Vice", -2);
				keyOrderMap.put("Quality of Life", -1);
				keyOrderMap.put("Developer", 1);

				int order1 = keyOrderMap.getOrDefault(key1, 0);
				int order2 = keyOrderMap.getOrDefault(key2, 0);

				if (order1 != order2) {
					return Integer.compare(order1, order2);
				} else {
					return key1.compareTo(key2);
				}
			};
		}
	}
}
