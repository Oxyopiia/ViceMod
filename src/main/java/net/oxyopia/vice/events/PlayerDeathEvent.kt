package net.oxyopia.vice.events

import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket
import net.oxyopia.vice.utils.Utils

class PlayerDeathEvent(packet: DeathMessageS2CPacket) : ViceEvent() {
	val entity by lazy {
		Utils.getWorld()?.getEntityById(packet.playerId)
	}
}