package net.oxyopia.vice.config.features

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.Position

class BossStorage {

	@Expose
	val vice = MasterableBoss()

	@Expose
	val wasteyard = MasterableBoss()

	@Expose
	val gelato = MasterableBoss()

	@Expose
	val ppp = MasterableBoss()

	@Expose
	val minehut = MasterableBoss()

	@Expose
	var diox = DioxBossData()

	class DioxBossData : Boss() {
		@Expose
		var easyCompletions: Int = 0

		@Expose
		var normalCompletions: Int = 0
	}

	@Expose
	val elderpork = MasterableBoss()

	@Expose
	val vatican = Boss()

	@Expose
	val shadowGelato = Boss()

	@Expose
	val abyssalVice = Boss()

	open class Boss {
		@Expose
		open var completions: Int = 0
	}

	class MasterableBoss : Boss() {
		@Expose
		var masteryCompletions: Int = 0

		@Expose
		var claimedTiers = mutableListOf<Int>()

		@Expose
		var hasOpened = false
	}

	@Expose
	var bossCounterPos: Position = Position(175f, 150f)

	@Expose
	var masteryTrackerPos: Position = Position(125f, 180f)

	@Expose
	var wasteyardTimerPos: Position = Position(120f, 90f)

	@Expose
	var mostRecentMasterableBoss: World? = null

}