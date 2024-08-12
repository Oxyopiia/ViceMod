package net.oxyopia.vice.config.features.worlds

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position

class SummerStorage {

	@Expose
	var fishups = hashMapOf<String, Int>()

	@Expose
	var pufferfishOpened = hashMapOf<String, Int>()

	@Expose
	var violetReset = -1L

	@Expose
	var fishingDropsPos = Position(200f, 10f, centered = false)

	@Expose
	var violetInfoPos = Position(240f, 69f)

}