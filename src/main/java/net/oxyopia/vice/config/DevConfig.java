package net.oxyopia.vice.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;

import java.awt.*;
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
		type = PropertyType.NUMBER,
		name = "Cursor Cooldown X Offset",
		description = "X offset for cooldown displayed near cursor",
		category = "Experiments",
		subcategory = "Item Cooldown Display",
		min = -100,
		max = 100
	)
	public int ITEMCD_CURSORCD_X_OFFSET = 3;

	@Property(
		type = PropertyType.NUMBER,
		name = "Cursor Cooldown Y Offset",
		description = "Y offset for cooldown displayed near cursor",
		category = "Experiments",
		subcategory = "Item Cooldown Display",
		min = -100,
		max = 100
	)
	public int ITEMCD_CURSORCD_Y_OFFSET = 3;

	@Property(
		type = PropertyType.COLOR,
		name = "Cursor Cooldown Text Color",
		description = "Color text cursor cooldown",
		category = "Experiments",
		subcategory = "Item Cooldown Display",
		allowAlpha = false
	)
	public Color ITEMCD_CURSORCD_COLOR = new Color(0, 236, 255, 255);

	@Property(
		type = PropertyType.SWITCH,
		name = "Cursor Cooldown Text Centered",
		description = "Text center",
		category = "Experiments",
		subcategory = "Item Cooldown Display"
	)
	public boolean ITEMCD_CURSORCD_CENTER_TEXT = false;

	/** BYPASSES */
	@Property(
		type = PropertyType.SWITCH,
		name = "Bypass Scoreboard Checker",
		description = "bypasses inDoomTowers check for scoreboard\nthis will make doomtowers features render anywhere and everywhere",
		category = "Bypasses"
	)
	public boolean BYPASS_INSTANCE_CHECK = false;

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
		name = "Bossbar debugger",
		description = "yep",
		category = "Debugs"
	)
	public boolean BOSSBAR_DEBUGGER = false;

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
		name = "InGameHud mixin",
		description = "Debugger for title data, subtitle data, and more",
		category = "Debugs"
	)
	public boolean INGAMEHUD_MIXIN_DEBUGGER = false;

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

	public DevConfig() {
		super(new File("./config/vice/developerSettings.toml"), "Vice Developer Menu");
	}

	public void init() {
		initialize();
		markDirty();

		setSubcategoryDescription("Experiments", "Item Cooldown Display", "this is here as i haven't made the HUD manager yet, will be removed when that is added");
	}
}
