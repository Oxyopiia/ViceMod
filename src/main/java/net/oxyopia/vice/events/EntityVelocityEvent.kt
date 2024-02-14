package net.oxyopia.vice.events

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket
import net.minecraft.util.math.Vec3d

class EntityVelocityEvent(packet: EntityVelocityUpdateS2CPacket) : ViceEvent() {
	val velocityX = packet.velocityX
	val velocityY = packet.velocityY
	val velocityZ = packet.velocityZ
	val entityId = packet.id

	val trueVelocity = Vec3d(
		packet.velocityX / 8000.0,
		packet.velocityY / 8000.0,
		packet.velocityZ / 8000.0
	)
}