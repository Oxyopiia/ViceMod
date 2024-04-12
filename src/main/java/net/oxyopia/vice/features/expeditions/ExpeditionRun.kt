package net.oxyopia.vice.features.expeditions

import com.google.gson.annotations.Expose

data class ExpeditionRun(
	@Expose val startTime: Long,
	@Expose val teammates: List<String>,
	@Expose var totalCredits: Int = 0,
	@Expose var barrelsCollected: Int = 0,
	@Expose var endTime: Long? = null
) {
	fun isActive(): Boolean = startTime >= 0L && endTime == null
}