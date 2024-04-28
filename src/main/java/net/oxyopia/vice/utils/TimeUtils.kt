package net.oxyopia.vice.utils

import kotlin.math.ceil
import kotlin.time.Duration
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

	/**
	 * Takes a time delta in milliseconds and formats to a duration in seconds.
	 */
	fun Long.formatTimer(timeLimit: Int): String {
		return " \uD83D\uDD51 " + ceil(timeLimit - (this / 1000f)).toInt().seconds.formatDuration()
	}

	fun Long.timeDelta(): Long {
		return System.currentTimeMillis() - this
	}

	fun Long.timeDeltaWithin(duration: Duration): Boolean = (timeDelta() <= duration.inWholeMilliseconds)

	fun Duration.ms() = this.inWholeMilliseconds
	fun Int.ms() = this * 1000
}