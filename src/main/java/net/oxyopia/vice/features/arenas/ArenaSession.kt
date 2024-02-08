package net.oxyopia.vice.features.arenas

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.oxyopia.vice.Vice.Companion.EVENT_MANAGER
import net.oxyopia.vice.events.ArenaWaveChangeEvent
import net.oxyopia.vice.events.EntityDeathEvent
import net.oxyopia.vice.events.TitleEvent
import net.oxyopia.vice.events.WorldChangeEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.ArenaStartEvent
import java.util.regex.Pattern
import kotlin.math.round

object ArenaSession {
	lateinit var relatedWorld: World
	var active: Boolean = false
	var startTime: Long = 0
	var waveNumber: Int = 0
	var waveStartTime: Long = 0
	var waveMobsKilled: Int = 0
	var totalMobsKilled: Int = 0
	var supplyDropsCollected: Int = 0

	// Setters

	private fun begin(world: World) {
		relatedWorld = world
		startTime = System.currentTimeMillis()
		totalMobsKilled = 0
		supplyDropsCollected = 0
		setWave(0)
		active = true

		EVENT_MANAGER.publish(ArenaStartEvent(world, startTime))
		DevUtils.sendDebugChat("&&dARENAS&&r &&aSession started", "ARENAS_DEBUGGER")
	}

	private fun dispose() {
		active = false
		DevUtils.sendDebugChat("&&dARENAS&&r &&cSession disposed", "ARENAS_DEBUGGER")
	}

	private fun setWave(n: Int) {
		waveNumber = n
		waveMobsKilled = if (n != 1) -1 else 0 // This is done because DoomTowers starts the next wave in the same tick as the death event
		waveStartTime = System.currentTimeMillis()
		EVENT_MANAGER.publish(ArenaWaveChangeEvent(n))
		DevUtils.sendDebugChat("&&dARENAS&&r Wave Updated to &&a$n &&r($waveStartTime)", "ARENAS_DEBUGGER")
	}

	private fun addKill() {
		totalMobsKilled += 1
		waveMobsKilled += 1
		DevUtils.sendDebugChat("&&dARENAS&&r Kills Updated to &&b$waveMobsKilled &&r(total $totalMobsKilled)","ARENAS_DEBUGGER")
	}

	// Getters

	private fun isBossWave(): Boolean {
		return waveNumber % 5 == 0
	}

	private val totalBossWaves get() = waveNumber / 5

	val mobsRemaining get() = if (isBossWave()) 1 else totalWaveMobs() - waveMobsKilled

	fun totalWaveMobs(): Int {
		return if (isBossWave()) 1 else {
			(3 + (waveNumber * 2) - (totalBossWaves * 2)).coerceAtMost(35)
		}
	}

	fun calcCommonDrops(): Int {
		return round(waveNumber * 1.25).toInt().coerceAtMost(64)
	}

	fun calcUniqueDropChance(): Double {
		return (waveNumber * 0.25).coerceAtMost(100.0)
	}

	@SubscribeEvent
	fun onWorldChange(event: WorldChangeEvent) {
		val worldPath: String? = event.world.registryKey?.value?.path
		val world2: World? = worldPath?.let { World.getById(it) }

		if (!active && world2?.type == World.WorldType.ARENA) {
			begin(world2)
		} else if (active && world2?.type != World.WorldType.ARENA) {
			dispose()
		}
	}

	@SubscribeEvent
	fun onTitle(event: TitleEvent) {
		val pattern = Pattern.compile("WAVE\\s(\\d+)")
		val matcher = pattern.matcher(event.title)
		if (matcher.matches() && active) {
			try {
				val waveNumber = matcher.group(1).toInt()
				setWave(waveNumber)
			} catch (err: NumberFormatException) {
				DevUtils.sendErrorMessage(err, "An error occurred parsing Wave Number regex!")
			}
		} else if (event.title.contains("MINIBOSS")) {
			setWave(waveNumber + 1)
		}
	}

	@SubscribeEvent
	fun onEntityDeath(event: EntityDeathEvent) {
		if (!active || !relatedWorld.isInWorld()) return
		if (event.entity !is LivingEntity || event.entity is PlayerEntity) return

		addKill()
	}
}
