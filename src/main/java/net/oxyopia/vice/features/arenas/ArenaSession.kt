package net.oxyopia.vice.features.arenas

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.oxyopia.vice.Vice.Companion.EVENT_MANAGER
import net.oxyopia.vice.data.Debugger
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
	var waveNumber: Int = 0
	var waveStartTime: Long = 0
	private var startTime: Long = 0
	private var waveMobsKilled: Int = 0
	private var totalMobsKilled: Int = 0
	private var supplyDropsCollected: Int = 0

	// Setters

	private fun begin(world: World) {
		relatedWorld = world
		startTime = System.currentTimeMillis()
		totalMobsKilled = 0
		supplyDropsCollected = 0
		setWave(0)
		active = true

		EVENT_MANAGER.publish(ArenaStartEvent(world, startTime))
		Debugger.ARENA.debug("§aSession started")
	}

	private fun dispose() {
		active = false
		Debugger.ARENA.debug("§cSession disposed")
	}

	private fun setWave(n: Int) {
		waveNumber = n
		waveMobsKilled = if (n != 1) -1 else 0 // This is done because DoomTowers starts the next wave in the same tick as the death event
		waveStartTime = System.currentTimeMillis()
		EVENT_MANAGER.publish(ArenaWaveChangeEvent(n))
		Debugger.ARENA.debug("Wave Updated to §a$n §r($waveStartTime)")
	}

	private fun addKill() {
		totalMobsKilled += 1
		waveMobsKilled += 1
		Debugger.ARENA.debug("Kills Updated to §b$waveMobsKilled §r(total $totalMobsKilled)")
	}

	// Getters

	private fun isBossWave(): Boolean {
		return waveNumber % 5 == 0
	}

	private val totalBossWaves get() = waveNumber / 5

	val mobsRemaining get() = if (isBossWave()) 1 else totalWaveMobs() - waveMobsKilled

	private fun totalWaveMobs(): Int {
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
		worldPath?.let {
			val world = World.getById(it) ?: return

			if (!active && world.properties.contains(World.WorldProperty.ARENA)) {
				begin(world)
			} else if (active && world.properties.contains(World.WorldProperty.ARENA)) {
				dispose()
			}
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
		} else if (event.title.contains("MINIBOSS") || event.title.contains("MERRY CHRISTMAS!") || event.title.contains("TIDES UP!")) {
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
