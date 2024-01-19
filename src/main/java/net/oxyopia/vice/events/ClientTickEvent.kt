package net.oxyopia.vice.events

/**
 * Adapted from SkyHanni under the GNU Lesser General Public License v2.1.
 *
 * @link https://github.com/hannibal002/SkyHanni/blob/beta/src/main/java/at/hannibal2/skyhanni/events/LorenzTickEvent.kt
 * @link https://github.com/hannibal002/SkyHanni/blob/beta/LICENSE
 * @author hannibal002
 */
class ClientTickEvent(val tick: Int) : ViceEvent() {
	private fun isMod(i: Int) = tick % i == 0

	fun repeatSeconds(i: Int) = isMod(20 * i)
}