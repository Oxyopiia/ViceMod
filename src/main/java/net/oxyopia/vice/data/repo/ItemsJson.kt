package net.oxyopia.vice.data.repo

import com.google.gson.annotations.Expose

data class ItemsJson(
	@Expose val items: List<Item>
)

data class Item(
	@Expose val itemName: String,
	@Expose val cooldown: Double,
	@Expose val soundOnUse: Boolean? = null,
	@Expose val set: String? = null,
	@Expose val setAmount: Int? = null
)