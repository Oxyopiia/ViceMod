package net.oxyopia.vice.features.expeditions

import com.google.gson.annotations.Expose

data class ExpeditionRun(
	@Expose var startTime: Long,
	@Expose var gameState: Int = 0,
	@Expose var totalCredits: Int = 0,
	@Expose var barrelsCollected: Int = 0,
	@Expose var endTime: Long? = null
) {
	fun isActive(): Boolean = endTime != null && startTime >= 0
	fun roomIsCompleteAndWaiting(): Boolean = gameState % 2 == 1
}