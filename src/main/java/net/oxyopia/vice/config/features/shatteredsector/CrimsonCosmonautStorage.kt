package net.oxyopia.vice.config.features.shatteredsector

import com.google.gson.annotations.Expose
import net.oxyopia.vice.data.gui.Position

class CrimsonCosmonautStorage {
    @Expose
    var fightTimePos = Position(170f, 300f)

    @Expose
    var lastKnownFightTime: Long = -1L
}