package net.oxyopia.vice.features.cooking

enum class CookingItem(val displayName: String) {
	BREAD("Bread"),
	CHEESE("Cheese"),
	LETTUCE("Lettuce"),
	TOMATO("Tomato"),
	RAW_MEAT("Raw Meat"),
	COOKED_MEAT("Cooked Burger"),
	MYSTIC_BACON("Mystic Bacon"),
	EMPTY_CUP("Empty Cup"),
	SODA("Soda"),
	NONE("");

	companion object {
		fun getByName(displayName: String): CookingItem? {
			return entries.filterNot { it == NONE }.firstOrNull { it.displayName == displayName }
		}
	}
}