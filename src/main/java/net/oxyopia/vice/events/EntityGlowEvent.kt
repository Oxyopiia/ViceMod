package net.oxyopia.vice.events

import net.minecraft.entity.Entity

// TODO - Make event Returnable with Boolean Type
class EntityGlowEvent(val entity: Entity) : ViceEvent() {
	val entityName: String by lazy { entity.name.string }
}