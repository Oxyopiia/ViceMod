package net.oxyopia.vice.config.features

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position

class ExpeditionsStorage {

	@Expose
	var easter: Expedition = Expedition()

	class Expedition {

		@Expose
		var personalBestTime: Long = -1L

	}

	@Expose
	var merchantOverlayPos: Position = Position(40f, 170f, scale = 0.7f)
}