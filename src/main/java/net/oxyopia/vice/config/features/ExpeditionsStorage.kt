package net.oxyopia.vice.config.features

import com.google.gson.annotations.Expose

class ExpeditionsStorage {

	@Expose
	var easter: Expedition = Expedition()

	class Expedition {

		@Expose
		var personalBestTime: Long = -1L

	}
}