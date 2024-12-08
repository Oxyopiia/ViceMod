package net.oxyopia.vice.config.features.worlds

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.utils.hud.HorizontalAlignment

class SummerStorage {


	@Expose
	var fishups = hashMapOf<String, Int>()

	@Expose
	var pufferfishOpened = hashMapOf<String, Int>()

	@Expose
	var violetReset = -1L

	@Expose
	var iceCreamReset = -1L

	@Expose
	var barReset = -1L

	@Expose
	var fishingDropsPos = Position(200f, 10f, alignment = HorizontalAlignment.LEFT)

	@Expose
	var violetInfoPos = Position(240f, 69f)

}