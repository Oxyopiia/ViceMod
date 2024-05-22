package net.oxyopia.vice.features.expeditions

import com.google.gson.annotations.Expose
import net.minecraft.client.network.AbstractClientPlayerEntity

data class ExpeditionRun(
	@Expose var startTime: Long,
	@Expose var gameState: Int = 0,
	@Expose var players: MutableList<AbstractClientPlayerEntity> = mutableListOf(),
	@Expose var totalCredits: Int = 0,
	@Expose var barrelsCollected: Int = 0,
	@Expose var endTime: Long? = null
) {
	fun isActive(): Boolean = endTime == null && startTime > 0 && ExpeditionAPI.isInExpedition()
	fun roomIsCompleteAndWaiting(): Boolean = gameState % 2 == 1
}