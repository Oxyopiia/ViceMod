package net.oxyopia.vice.config.features

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position

class MiscStorage {

    @Expose
	var lastDailyReward: Long = -1L

	@Expose
	var backpackNames: HashMap<String, String> = HashMap()

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
