package net.oxyopia.vice.config.features.worlds

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position

class AuxiliaryStorage {

	@Expose
	var city = CityStorage()

	class CityStorage {

		@Expose
		var powerBoxTimerPos: Position = Position(27f, 10f)

	}
}