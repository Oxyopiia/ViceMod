package net.oxyopia.vice.events

import net.oxyopia.vice.data.World

class ArenaStartEvent(val world: World, val timestamp: Long) : ViceEvent()