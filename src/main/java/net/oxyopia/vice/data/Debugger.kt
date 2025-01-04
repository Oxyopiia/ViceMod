package net.oxyopia.vice.data

import net.oxyopia.vice.Vice.Companion.devConfig
import net.oxyopia.vice.utils.DevUtils

enum class Debugger(private val isEnabled: () -> Boolean, private val prefix: String) {
	ARENA({ devConfig.ARENAS_DEBUGGER}, "§dARENAS"),
	BOSS({ devConfig.BOSS_DETECTION_INFO}, "§9BOSS CHANGE"),
	BOSSBAR({ devConfig.BOSSBAR_DEBUGGER }, "§5BOSSBAR"),
	COOKING({ devConfig.COOKING_DEBUGGER }, "§6COOKING"),
	EXPEDITIONS({ devConfig.EXPEDITION_DEBUGGER }, "§aEXPEDITIONS"),
	ITEMABILITY({ devConfig.ITEM_ABILITY_DEBUGGER }, "§bITEMABILITY"),
	HUD({ devConfig.INGAMEHUD_MIXIN_DEBUGGER }, "§dHUD"),
	MASTERY({ devConfig.MASTERY_DEBUGGER }, "§6MASTERY"),
	MOB({ devConfig.MOB_SPAWN_DEBUGGER }, "§2MOB"),
	SLOTCLICK({ devConfig.SLOT_CLICK_DEBUGGER}, "§4SlotClickEvent"),
	SOUND({ devConfig.SEND_SOUND_INFO }, "§bSOUND"),
	;

	fun debug(string: String) {
		if (!isEnabled()) return
		DevUtils.sendDebugChat("$prefix §f$string")

	}

	fun debug(string: String, flavor: String = "") {
		if (!isEnabled()) return
		DevUtils.sendDebugChat("$prefix $flavor §f$string")
	}

	/**
	 * Only checks for Dev Mode enabled.
	 */
	fun warn(string: String) {
		DevUtils.sendDebugChat("$prefix §c$string")
	}
}