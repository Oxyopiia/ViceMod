package net.oxyopia.vice.config.features

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position

class MiscStorage {

    @Expose
    var playerStatsPos: Position = Position(175f, 150f)

    @Expose
	var lastDailyReward: Long = -1L

}
