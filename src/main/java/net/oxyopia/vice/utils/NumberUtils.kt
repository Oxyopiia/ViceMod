package net.oxyopia.vice.utils

object NumberUtils {
	fun Int.clamp(min: Int, max: Int): Int {
		return maxOf(min, minOf(max, this))
	}

	fun Double.clamp(min: Double, max: Double): Double {
		return maxOf(min, minOf(max, this))
	}

	fun Float.clamp(min: Float, max: Float): Float {
		return maxOf(min, minOf(max, this))
	}

	fun Long.clamp(min: Long, max: Long): Long {
		return maxOf(min, minOf(max, this))
	}

	fun Short.clamp(min: Short, max: Short): Short {
		return maxOf(min, minOf(max, this))
	}
}