package net.oxyopia.vice.utils

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.time.Duration

object TimeUtils {
	/**
	 * Formats a duration as dd:hh:MM:SS
	 * @param ms Time in Milliseconds
	 */
	fun formatDuration(ms: Long, showMs: Boolean = false): String {
		val hours = floor(ms.toDouble() / (1000 * 60 * 60)).toLong()
		val mins = floor((ms / (1000 * 60)).toDouble() % 60).toLong()
		val secs = floor((ms / 1000).toDouble() % 60).toLong()
		val millis = ms % 1000

		return buildString {
			if (hours > 0) append(String.format("%02d:", hours))
			append(String.format("%02d:%02d", mins, secs))
			if (showMs) append(String.format(".%03d", millis))
		}
	}

	fun formatDuration(seconds: Float): String {
		return formatDuration((seconds).toLong())
	}

	fun formatDuration(seconds: Long): String {
		return formatDuration(seconds * 1000, false)
	}

	/**
	 * Takes a time delta in milliseconds and formats to a duration in seconds.
	 */
	fun Long.formatTimer(timeLimit: Int): String {
		return " \uD83D\uDD51 " + formatDuration(ceil(timeLimit - (this / 1000f)))
	}

	fun Long.timeDelta(): Long = System.currentTimeMillis() - this
	fun Long.timeDeltaWithin(duration: Duration): Boolean = (timeDelta() <= duration.inWholeMilliseconds)

	fun Duration.ms() = this.inWholeMilliseconds
	fun Int.ms() = this * 1000
}