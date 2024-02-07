package net.oxyopia.vice.config.features.worlds

import com.google.gson.annotations.Expose

class ArenaStorage {

	@Expose
	var startTimes: HashMap<String, Long> = HashMap()

}
