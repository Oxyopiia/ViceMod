package net.oxyopia.vice.config.features

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position

class MiscStorage {

    @Expose
	var lastDailyReward: Long = -1L

	@Expose
	var backpackNames: HashMap<String, String> = HashMap()

	@Expose
	var playerStatsPos: Position = Position(175f, 150f)

}
