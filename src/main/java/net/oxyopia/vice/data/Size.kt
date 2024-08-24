package net.oxyopia.vice.data

data class Size(
	val width: Float,
	val height: Float,
) {
	companion object {
		const val DEFAULT_TEXT_HEIGHT = 8
	}
}