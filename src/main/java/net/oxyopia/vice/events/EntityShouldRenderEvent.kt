package net.oxyopia.vice.events

import net.minecraft.entity.Entity

class EntityShouldRenderEvent(val entity: Entity) : ViceEvent.Cancelable<Boolean>()