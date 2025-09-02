package net.oxyopia.vice.config.features

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position
//import net.oxyopia.vice.features.worlds.magmaheights.ForgeTimers

class MiscStorage {

	@Expose
	var showAllHudEditorElements: Boolean = true

    @Expose
    var lastEvanQuiz: Long = -1L

	@Expose
	var ammo: Int = 0

	@Expose
	var backpackNames: HashMap<String, String> = HashMap()

	@Expose
	var protectedItems: MutableList<String> = mutableListOf()

	/*@Expose
	var forgeList: MutableList<ForgeTimers.ForgeItem> = mutableListOf()

	@Expose
	var forgeTimersPos: Position = Position(128f, 95f)*/

	@Expose
	var playerStatsPos: Position = Position(175f, 150f)

	@Expose
	var fishingBrewTimerPos: Position = Position(200f, 0f)

	@Expose
	var boxtownTimerPos: Position = Position(200f, 0f)

	@Expose
	var lastKnownBoxtown: Long = 0L

}
