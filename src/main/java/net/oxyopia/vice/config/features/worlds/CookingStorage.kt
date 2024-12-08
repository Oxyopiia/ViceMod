package net.oxyopia.vice.config.features.worlds

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.utils.hud.HorizontalAlignment

class CookingStorage {

	@Expose
	var currentOrder: String = ""

	@Expose
	var currentOrderProgress: Int = 0

	@Expose
	var stock: Int = -10000

	@Expose
	var totalBurgerRequests: HashMap<String, Int> = HashMap()

	@Expose
	var totalBurgersComplete: HashMap<String, Int> = HashMap()

	@Expose
	var currentOrderPos: Position = Position(175f, 10f)

	@Expose
	var orderTrackerPos: Position = Position(10f, 50f, alignment = HorizontalAlignment.LEFT)

}