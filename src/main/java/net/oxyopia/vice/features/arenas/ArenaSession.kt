package net.oxyopia.vice.features.arenas

import net.minecraft.client.world.ClientWorld
import net.minecraft.util.Identifier
import net.oxyopia.vice.Vice.config
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.enums.World
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

	fun begin(world: World) {
		relatedWorld = world
		startTime = System.currentTimeMillis()
		totalMobsKilled = 0
		supplyDropsCollected = 0
		setWave(0)
		active = true
		DevUtils.sendDebugChat("&&dARENAS&&r &&aSession started", "ARENAS_DEBUGGER")
	}

	fun dispose() {
		active = false
		DevUtils.sendDebugChat("&&dARENAS&&r &&cSession disposed", "ARENAS_DEBUGGER")
	}

	fun setWave(n: Int) {
		waveNumber = n
		waveMobsKilled = 0
		waveStartTime = System.currentTimeMillis()
		DevUtils.sendDebugChat("&&dARENAS&&r Wave Updated to &&a$n &&r($waveStartTime)", "ARENAS_DEBUGGER")

		if (config.ARENAS_MOB_EFFECT_NOTIFICATION) {
			when (n) {
				10 -> Utils.sendViceMessage("Mobs now have &&bSpeed I")
				15 -> Utils.sendViceMessage("Mobs now have &&bSpeed I&&r & &&aResistance I")
				20 -> Utils.sendViceMessage("Mobs now have &&bSpeed I&&r & &&aResistance II")
				25 -> Utils.sendViceMessage("Mobs now have &&bSpeed I&&r & &&aResistance III")
				30 -> Utils.sendViceMessage("Mobs now have &&bSpeed II&&r & &&aResistance III")
				40 -> Utils.sendViceMessage("Mobs now have &&bSpeed II&&r & &&aResistance III&&r & &&4Strength I")
				50 -> Utils.sendViceMessage("Mobs now have &&bSpeed II&&r & &&aResistance IV&&r & &&4Strength II")
				75 -> Utils.sendViceMessage("Mobs now have &&bSpeed II&&r & &&aResistance IV&&r & &&4Strength III")
				else -> return
			}

			Utils.playSound(Identifier("minecraft", "block.note_block.pling"), 2f, 1f)
		}
	}

	fun addKill() {
		totalMobsKilled += 1
		waveMobsKilled += 1
		DevUtils.sendDebugChat("&&dARENAS&&r Kills Updated to &&b$waveMobsKilled &&r(total $totalMobsKilled)","ARENAS_DEBUGGER")
	}

	fun addSupplyDrop() {
		supplyDropsCollected += 1
		DevUtils.sendDebugChat("&&dARENAS&&r Supply Drops Updated to &&e$supplyDropsCollected", "ARENAS_DEBUGGER")
	}

	// Getters

	fun isBossWave(): Boolean {
		return waveNumber % 5 == 0
	}

	fun totalTimeElapsed(): Long {
		return System.currentTimeMillis() - startTime
	}

	fun waveTimeElapsed(): Long {
		return System.currentTimeMillis() - waveStartTime
	}

	fun totalWaveMobs(): Int {
		// First round has 7 mobs, adding 2 each round until round 20, where it caps at 43
		return if (isBossWave()) 1 else {
			(5 + (waveNumber * 2)).coerceAtMost(43)
		}
	}

	fun calculateCommonDrops(): Int {
		return round(waveNumber * 1.25).toInt().coerceAtMost(64)
	}

	fun calculateUniqueDropChance(): Double {
		return (waveNumber * 0.25).coerceAtMost(100.0)
	}


	fun onWorldChange(world: ClientWorld?) {
		if (Utils.inDoomTowers()) {
			val worldPath: String? = world?.registryKey?.value?.path
			val world2: World? = worldPath?.let { World.getById(it) }

			// I'm gonna create a WorldChangeEvent for handling this in the future, for now it'll be here, because im lazy as hell:
			if (world2 == null) {
				DevUtils.sendWarningMessage("Unable to match world &&b$worldPath &&eto a DoomTowers World. Please report this!")
			}

			if (!active && world2?.type == World.WorldType.ARENA) {
				begin(world2)
			} else if (active && world2?.type != World.WorldType.ARENA) {
				dispose()
			}
		}
	}
}
