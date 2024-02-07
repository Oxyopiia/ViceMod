package net.oxyopia.vice.config.features.worlds

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position

class ShowdownStorage {

	@Expose
	var lastKnownTrainSpawn: Long = -1L

	@Expose
	var trainTimerPos: Position = Position(175f, 150f)

}