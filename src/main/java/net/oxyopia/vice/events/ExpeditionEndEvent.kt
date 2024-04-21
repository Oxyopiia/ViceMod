package net.oxyopia.vice.events

import net.oxyopia.vice.features.expeditions.ExpeditionRun

class ExpeditionEndEvent(val session: ExpeditionRun, val completionTime: Long, val isNewPB: Boolean) : ViceEvent()