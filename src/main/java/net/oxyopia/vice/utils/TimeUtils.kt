package net.oxyopia.vice.utils

import kotlin.math.ceil
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object TimeUtils {
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

	fun Duration.formatTimer(timeLimit: Duration): String {
		return " \uD83D\uDD51 " + ceil((timeLimit.inWholeMilliseconds - this.inWholeMilliseconds) / 1000.0).seconds.formatDuration()
	}

	fun Long.formatShortDuration() = String.format("%.2f", this / 1000f)

	fun Long.timeDelta(): Long = System.currentTimeMillis() - this
	fun Long.timeDeltaDuration(): Duration = System.currentTimeMillis().milliseconds - this.milliseconds
	fun Long.timeDeltaUntil(): Long = this - System.currentTimeMillis()
	fun Long.timeDeltaWithin(duration: Duration): Boolean = (timeDelta() <= duration.inWholeMilliseconds)

	fun Duration.ms() = this.inWholeMilliseconds
}