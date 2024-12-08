package net.oxyopia.vice.data.gui

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import net.oxyopia.vice.utils.hud.HorizontalAlignment

data class Position @JvmOverloads constructor(
	@Expose @SerializedName("x") var x: Float,
	@Expose @SerializedName("y") var y: Float,
	@Expose @SerializedName("scale") var scale: Float = 1f,
	@Expose @SerializedName("alignment") var alignment: HorizontalAlignment = HorizontalAlignment.CENTER,
)