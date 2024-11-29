package net.oxyopia.vice.utils

import net.minecraft.util.math.Box
import net.oxyopia.vice.data.World

object LocationUtils {
	fun Box.isInBounds(world: World? = null): Boolean {
		if (world?.isInWorld() == false) return false

		return contains(Utils.getPlayer()?.pos)
	}
}