package net.oxyopia.vice.utils

import kotlin.math.ceil
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object TimeUtils {
	private const val PADDED_CLOCK_ICON = " \uD83D\uDD51 "

	fun Duration.formatDuration(showMs: Boolean = false): String {
		val hours = inWholeHours
		val mins = inWholeMinutes % 60
		val secs = inWholeSeconds % 60
		val millis = inWholeMilliseconds % 1000

		return buildString {
			if (hours > 0) append(String.format("%02d:", hours))
			append(String.format("%02d:%02d", mins, secs))
			if (showMs) append(String.format(".%03d", millis))
		}
	}

	fun Duration.formatTimer(timeLimit: Duration, showClock: Boolean = true): String {
		return (if (showClock) PADDED_CLOCK_ICON else "") + ceil((timeLimit.inWholeMilliseconds - this.inWholeMilliseconds) / 1000.0).seconds.formatDuration()
	}

	fun Duration.formatShortDuration() = String.format("%.1f", this.inWholeMilliseconds / 1000f)

	fun Long.timeDelta(): Duration = System.currentTimeMillis().milliseconds - this.milliseconds
	fun Long.timeDeltaUntil(): Duration = this.milliseconds - System.currentTimeMillis().milliseconds
	fun Long.timeDeltaWithin(duration: Duration): Boolean = timeDelta() <= duration
	fun Long.isInFuture(): Boolean = this.milliseconds > System.currentTimeMillis().milliseconds

	fun Duration.ms() = this.inWholeMilliseconds
}