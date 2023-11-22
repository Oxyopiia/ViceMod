package net.oxyopia.vice.features.arenas

import net.minecraft.client.world.ClientWorld
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.enums.World

object ArenasHelperFunctions {
	fun parseWorldChange(world: ClientWorld?) {
		if (Utils.inDoomTowers()) {
			val worldPath: String? = world?.registryKey?.value?.path
			val world2: World? = worldPath?.let { World.getById(it) }

			// I'm gonna create a WorldChangeEvent for handling this in the future, for now it'll be here, because im lazy as hell:
			if (world2 == null) {
				DevUtils.sendWarningMessage("Unable to match world &&b" + worldPath + "&&e to a DoomTowers World. Please report this!")
			}

			if (!ArenaSession.active && world2?.type == World.WorldType.ARENA) {
				ArenaSession.begin(world2)
			} else if (ArenaSession.active && world2?.type != World.WorldType.ARENA) {
				ArenaSession.dispose()
			}
		}
	}
}