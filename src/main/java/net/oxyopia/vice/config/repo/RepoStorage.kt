package net.oxyopia.vice.config.repo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import net.oxyopia.vice.data.repo.ItemsJson

data class RepoStorage(
	@Expose val schema: Int,
	@Expose val sha: String,
	@Expose @SerializedName("Items.json") val itemsJson: ItemsJson
)