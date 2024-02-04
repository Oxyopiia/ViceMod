package net.oxyopia.vice.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Position @JvmOverloads constructor(
	@Expose @SerializedName("x") var x: Float,
	@Expose @SerializedName("y") var y: Float,
	@Expose @SerializedName("scale") var scale: Float = 1f,
	@Expose @SerializedName("centered") var centered: Boolean = true
)