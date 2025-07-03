package net.oxyopia.vice.config.features

import com.google.gson.annotations.Expose
import net.oxyopia.vice.config.features.shatteredsector.CrimsonCosmonautStorage

class ShatteredSectorStorage {
    @Expose
    var crimsonCosmonaut: CrimsonCosmonautStorage = CrimsonCosmonautStorage()
}