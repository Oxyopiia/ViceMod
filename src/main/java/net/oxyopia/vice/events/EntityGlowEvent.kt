package net.oxyopia.vice.events

import net.minecraft.entity.Entity

class EntityGlowEvent(val entity: Entity) : ViceEvent.Cancelable<Boolean>() {
	val entityName: String by lazy { entity.name.string }
}