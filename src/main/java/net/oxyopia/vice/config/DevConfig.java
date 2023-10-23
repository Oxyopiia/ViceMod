package net.oxyopia.vice.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;

import java.io.File;

public class DevConfig extends Vigilant {

	@Property(
		type = PropertyType.SWITCH,
		name = "Bypass Scoreboard Checker",
		description = "bypasses inDoomTowers check for scoreboard",
		category = "Bypasses"
	)
	public boolean BYPASS_INSTANCE_CHECK = false;

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
		description = "title displays",
		category = "Debugs"
	)
	public boolean INGAMEHUD_MIXIN_DEBUGGER = false;

	@Property(
	        type = PropertyType.SWITCH,
	        name = "shadow gelato amethyst rendering debugger",
	        description = "yes",
	        category = "Debugs"
	)
	public boolean SHADOW_GELATO_AMETHYST = false;

	@Property(
		type = PropertyType.SWITCH,
		name = "Utils info",
		description = "utisl info",
		category = "Debugs"
	)
	public boolean UTILS_INFO = false;

	public DevConfig() {
		super(new File("./config/vice/developerSettings.toml"), "Vice Developer Menu");
	}

	public void init() {
		initialize();
		markDirty();
	}
}
