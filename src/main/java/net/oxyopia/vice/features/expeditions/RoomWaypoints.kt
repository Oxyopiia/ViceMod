package net.oxyopia.vice.features.expeditions

import net.minecraft.util.math.Vec3d
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.ChatColor
import net.oxyopia.vice.events.WorldRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.expeditions.ExpeditionAPI.rooms
import net.oxyopia.vice.utils.RenderUtils.drawString

object RoomWaypoints {
	@SubscribeEvent
	fun onRenderWorldLast(event: WorldRenderEvent.Last) {
		if (!Vice.config.EXPEDITION_ROOM_WAYPOINTS) return
		if (!ExpeditionAPI.isInExpedition()) return

		val currentRoom = ExpeditionAPI.getRoomByZ() ?: return
		if (currentRoom.type == ExpeditionAPI.RoomType.BOSS) return

		if (currentRoom.id != 0) {
			val lastRoom = rooms[currentRoom.id - 1]
			event.drawRoomLabels(lastRoom, currentRoom.minZ)
		}

		if (currentRoom.id + 1 >= rooms.size) return
		val nextRoom = rooms[currentRoom.id + 1]
		event.drawRoomLabels(nextRoom)
	}
	
	private fun WorldRenderEvent.Last.drawRoomLabels(room: ExpeditionAPI.Room, zPos: Double = room.minZ) {
		drawString(Vec3d(0.5, 72.0, zPos), room.id.toString(), ChatColor.AQUA.color, size = 2f, shadow = false)
		drawString(Vec3d(0.5, 71.5, zPos), room.name, room.type.color, size = 2f, shadow = false)

		if (room.hasMerchant()) {
			drawString(Vec3d(0.5, 71.0, zPos), "Merchant", ChatColor.GREEN.color, size = 2f, shadow = false)
		}
	}
}