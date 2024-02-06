package net.oxyopia.vice.data.gui

data class Quad(
	var minX: Float,
	var minY: Float,
	var maxX: Float,
	var maxY: Float
) {
	fun addPadding(padding: Float): Quad {
		minX -= padding
		minY -= padding

		maxX += padding
		maxY += padding

		return this
	}

	fun width() = maxX - minX
	fun height() = maxY - minY
}