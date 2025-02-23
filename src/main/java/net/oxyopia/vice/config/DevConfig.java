package net.oxyopia.vice.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import net.oxyopia.vice.Vice;
import net.oxyopia.vice.config.repo.RepoManager;
import net.oxyopia.vice.utils.ChatUtils;

import java.io.File;

@SuppressWarnings("unused")
public class DevConfig extends Vigilant {
	/** EXPERIMENTS */

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Red Fadeout Multiplier",
		description = "Red fade for item cooldown display option Color Fade\nDefault 2.7f",
		category = "Experiments",
		minF = 0.5f,
		maxF = 7.5f,
		subcategory = "Item Cooldown Display"
	)
	public float ITEMCD_RED_FADE_OVERRIDE = 2.7f;

	@Property(
		type = PropertyType.DECIMAL_SLIDER,
		name = "Green Fadein Multiplier",
		description = "Green fade for item cooldown display option Color Fade\nDefault 1.3f",
		category = "Experiments",
		minF = 0.5f,
		maxF = 7.5f,
		subcategory = "Item Cooldown Display"
	)
	public float ITEMCD_GREEN_FADE_OVERRIDE = 1.3f;

	@Property(
		type = PropertyType.SWITCH,
		name = "Show Live Arena Overlay in HUD Manager",
		description = "DOES NOTHING!! literally just a mockup graphic and test for HudManager",
		category = "Experiments",
		subcategory = "HudManager"
	)
	public boolean LIVE_ARENA_OVERLAY_THING = false;

	/** BYPASSES */
	@Property(
		type = PropertyType.SWITCH,
		name = "Bypass Scoreboard Checker",
		description = "bypasses inDoomTowers check for scoreboard\nthis will make doomtowers features render anywhere and everywhere",
		category = "Bypasses"
	)
	public boolean BYPASS_INSTANCE_CHECK = false;

	@Property(
		type = PropertyType.BUTTON,
		name = "Reload Storage from File",
		description = "Unloads the current Storage file, §cwithout saving§7, and creates a new instance from the storage.json file. Good for editing without a restart.",
		category = "Bypasses",
		placeholder = "Reload Storage"
	)
	public void reloadStorageFromFile() {
		Vice.storage = new Storage();
		Vice.storage.initialize();
		STORAGE_MARK_DIRTY = true;
	}

	@Property(
		type = PropertyType.BUTTON,
		name = "Force Save Storage",
		description = "force save storage.json, even if not dirty\nuse to unmark dirty",
		category = "Bypasses",
		placeholder = "Force Save Storage"
	)
	public void forceSaveStorage() {
		Vice.storage.forceSave();
	}

	@Property(
		type = PropertyType.SWITCH,
		name = "Allow Dirty Storage",
		description = "Whether Storage.json can be overwritten. §cONLY DISABLE IF YOU KNOW WHAT YOU'RE DOING!\n§cRequires Dev Mode enabled.",
		category = "Bypasses"
	)
	public boolean STORAGE_MARK_DIRTY = true;
	
	@Property(
		type = PropertyType.SWITCH,
		name = "Throttle Events",
		description = "Make it so no events can be fired and responded to. (literally disables the mod)\n§cRequires Dev Mode enabled.",
		category = "Bypasses"
	)
	public boolean THROTTLE_ALL_EVENTS = false;

	/** DEBUGS */
	@Property(
		type = PropertyType.SWITCH,
		name = "Sounds",
		description = "sends sound information when there is a LITERAL SOUND (sent from server only)",
		category = "Debugs"
	)
	public boolean SEND_SOUND_INFO = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "InGameHud mixin",
		description = "Debugger for title data, subtitle data, and HandledScreen data.",
		category = "Debugs"
	)
	public boolean INGAMEHUD_MIXIN_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Mastery Data",
		description = "debugs grabbed data about masteries",
		category = "Debugs"
	)
	public boolean MASTERY_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Bossbar debugger",
		description = "yep",
		category = "Debugs"
	)
	public boolean BOSSBAR_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Mob Spawn",
		description = "mobbobobobo",
		category = "Debugs"
	)
	public boolean MOB_SPAWN_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "PredictProjectile",
		description = "origin and end coordinates, calling patterns",
		category = "Debugs"
	)
	public boolean PREDICT_PROJECTILE_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Fishing",
		description = "sound deviations, velocity packets, last hook info",
		category = "Debugs"
	)
	public boolean FISHING_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "GameRenderer",
		description = "line rendering, box rendering utils",
		category = "Debugs"
	)
	public boolean GAME_RENDERER_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "ItemAbility",
		description = "item ability sutff",
		category = "Debugs"
	)
	public boolean ITEM_ABILITY_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Arenas",
		description = "arenas",
		category = "Debugs"
	)
	public boolean ARENAS_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Cooking debugger",
		description = "w4 stuff",
		category = "Debugs"
	)
	public boolean COOKING_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Boss Title Detection information",
		description = "robotop is hot",
		category = "Debugs"
	)
	public boolean BOSS_DETECTION_INFO = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "SlotClickEvent",
		description = "slot click event LMFAOOOOOOOOOO",
		category = "Debugs"
	)
	public boolean SLOT_CLICK_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Expeditions",
		description = "e",
		category = "Debugs"
	)
	public boolean EXPEDITION_DEBUGGER = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Lost In Time stuff",
		description = "e",
		category = "Debugs"
	)
	public boolean LOST_IN_TIME_DEBUGGER = false;

	@Property(
		type = PropertyType.BUTTON,
		name = "Refresh Repo",
		category = "Repo"
	)
	public void refreshRepo() {
		RepoManager.INSTANCE.refreshData((success) -> {
			if (success) {
				ChatUtils.INSTANCE.sendViceMessage("§aRepo reloaded!");
			} else {
				ChatUtils.INSTANCE.sendViceMessage("§cRepo did not load properly!");
			}
			return null;
		});
	}

	@Property(
		type = PropertyType.SWITCH,
		name = "Should Use Custom Repo",
		description = "Whether to use the Custom Repo or the default.",
		category = "Repo",
		subcategory = "Custom Repo"
	)
	public boolean USE_CUSTOM_REPO = false;

	@Property(
		type = PropertyType.TEXT,
		name = "Custom Repo Username",
		category = "Repo",
		subcategory = "Custom Repo"
	)
	public String CUSTOM_REPO_USER = "Oxyopiia";

	@Property(
		type = PropertyType.TEXT,
		name = "Custom Repo Name",
		category = "Repo",
		subcategory = "Custom Repo"
	)
	public String CUSTOM_REPO_NAME = "doomtowers-repo";

	@Property(
		type = PropertyType.TEXT,
		name = "Custom Repo Branch",
		category = "Repo",
		subcategory = "Custom Repo"
	)
	public String CUSTOM_REPO_BRANCH = "master";

	@Property(
		type = PropertyType.TEXT,
		name = "Custom Repo Aggregate File Name",
		category = "Repo",
		subcategory = "Custom Repo"
	)
	public String CUSTOM_REPO_GLOBAL_FILE_NAME = "all_data.json";

	@Property(
		type = PropertyType.BUTTON,
		name = "Reset Custom Repo",
		description = "doesn't update values in real time in the ui, but just swap tabs or sth",
		category = "Repo",
		subcategory = "Custom Repo"
	)
	public void resetCustomRepo() {
		CUSTOM_REPO_USER = "Oxyopiia";
		CUSTOM_REPO_NAME = "doomtowers-repo";
		CUSTOM_REPO_BRANCH = "master";
		CUSTOM_REPO_GLOBAL_FILE_NAME = "all_data.json";
	}

	public DevConfig() {
		super(new File("./config/vice/developerSettings.toml"), "Vice Developer Menu");
	}

	public void init() {
		initialize();
		markDirty();

		THROTTLE_ALL_EVENTS = false;
		STORAGE_MARK_DIRTY = true;
	}
}
