package net.oxyopia.vice.config.features

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position

class BossStorage {

	@Expose
	val vice = Boss()

	@Expose
	val wasteyard = Boss()

	@Expose
	val gelato = Boss()

	@Expose
	val ppp = Boss()

	@Expose
	val minehut = Boss()

	@Expose
	val diox = Boss()

	@Expose
	val dioxEasy = Boss()

	@Expose
	val shadowGelato = Boss()

	@Expose
	val abyssalVice = Boss()

	open class Boss {
		@Expose
		open var completions: Int = 0
	}

	@Expose
	var bossCounterPos: Position = Position(175f, 150f)

}