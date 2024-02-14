package net.oxyopia.vice.events

import net.oxyopia.vice.features.itemabilities.ItemAbility

class AbilityUseEvent(val ability: ItemAbility) : ViceEvent()