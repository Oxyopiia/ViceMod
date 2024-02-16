package net.oxyopia.vice.config.features

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.features.hud.ForgeTimers

class MiscStorage {

	@Expose
	var showAllHudEditorElements: Boolean = true

    @Expose
	var lastDailyReward: Long = -1L

    @Expose
    var lastEvanQuiz: Long = -1L

	@Expose
	var backpackNames: HashMap<String, String> = HashMap()

	@Expose
	var protectedItems: MutableList<String> = mutableListOf()

	@Expose
	var forgeList: MutableList<ForgeTimers.ForgeItem> = mutableListOf()

	@Expose
	var forgeTimersPos: Position = Position(128f, 95f)

	@Expose
	var playerStatsPos: Position = Position(175f, 150f)

    // Boss Counter

    @Expose
    var bossCounterPos: Position = Position(175f, 150f)

    @Expose
    var viceBoss: Int = 0

    @Expose
    var abyssalViceBoss: Int = 0

    @Expose
    var wastelandBoss: Int = 0

    @Expose
    var gelatoBoss: Int = 0

    @Expose
    var pppBoss: Int = 0

    @Expose
    var minehutBoss: Int = 0

    @Expose
    var shadowGelatoBoss: Int = 0


}
