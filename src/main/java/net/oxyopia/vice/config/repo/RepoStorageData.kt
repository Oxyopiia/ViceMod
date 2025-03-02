package net.oxyopia.vice.config.repo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import net.oxyopia.vice.data.repo.ItemsJson

data class RepoStorageData(
	@Expose val schema: Int,
	@Expose val schemaDeprecationMessage: String, // Used in case of breaking API changes
	@Expose val sha: String,
	@Expose @SerializedName("Items") val items: ItemsJson
)