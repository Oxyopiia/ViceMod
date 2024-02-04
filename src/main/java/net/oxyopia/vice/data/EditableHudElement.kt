package net.oxyopia.vice.data

abstract class EditableHudElement(val displayName: String, val defaultState: Position) {
	abstract fun setPosition(x: Float, y: Float, scale: Float = 1f, centered: Boolean = true)

	open fun draw() {

	}

	fun onClick() {

	}

	fun onDrag() {

	}
}

