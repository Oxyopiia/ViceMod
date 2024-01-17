package net.oxyopia.vice.events

import net.minecraft.entity.Entity
import net.oxyopia.vice.events.core.Returnable

@Returnable
class EntityGlowEvent(val entity: Entity) : BaseEvent() {
	val entityName: String by lazy { entity.name.string }
}