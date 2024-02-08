package net.oxyopia.vice.config.features.worlds

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position

class ArenaStorage {

	@Expose
	var startTimes: HashMap<String, Long> = HashMap()

	@Expose
	var liveArenaPos: Position = Position(100f, 100f)

}
