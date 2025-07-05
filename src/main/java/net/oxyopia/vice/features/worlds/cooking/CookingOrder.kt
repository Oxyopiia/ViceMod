package net.oxyopia.vice.features.worlds.cooking

enum class CookingOrder(val displayName: String, val recipe: List<CookingItem>, val isBossOrder: Boolean = false) {
	HAMBURGER("Hamburger", listOf(CookingItem.BREAD, CookingItem.COOKED_MEAT, CookingItem.BREAD)),
	CHEESEBURGER(
		"Cheeseburger",
		listOf(CookingItem.BREAD, CookingItem.COOKED_MEAT, CookingItem.CHEESE, CookingItem.BREAD)
	),
	DOUBLE_CHEESEBURGER(
		"Double Cheeseburger",
		listOf(
			CookingItem.BREAD,
			CookingItem.COOKED_MEAT,
			CookingItem.CHEESE,
			CookingItem.BREAD,
			CookingItem.COOKED_MEAT,
			CookingItem.CHEESE,
			CookingItem.BREAD
		)
	),
	ULTIMEATIUM(
		"Ultimeatium",
		listOf(
			CookingItem.BREAD,
			CookingItem.COOKED_MEAT,
			CookingItem.CHEESE,
			CookingItem.LETTUCE,
			CookingItem.TOMATO,
			CookingItem.BREAD
		)
	),
	OBESE_VICE_BURGER(
		"Obese Vice",
		listOf(
			CookingItem.BREAD,
			CookingItem.COOKED_MEAT,
			CookingItem.CHEESE,
			CookingItem.BREAD,
			CookingItem.COOKED_MEAT,
			CookingItem.MYSTIC_BACON,
			CookingItem.CHEESE,
			CookingItem.BREAD,
			CookingItem.COOKED_MEAT,
			CookingItem.CHEESE,
			CookingItem.BREAD
		),
		isBossOrder = true
	),
	XCAINR(
		"XCAINR",
		listOf(
			CookingItem.BREAD,
			CookingItem.COOKED_MEAT,
			CookingItem.COOKED_MEAT,
			CookingItem.COOKED_MEAT,
			CookingItem.COOKED_MEAT,
			CookingItem.COOKED_MEAT,
			CookingItem.COOKED_MEAT,
			CookingItem.COOKED_MEAT,
			CookingItem.COOKED_MEAT,
			CookingItem.COOKED_MEAT,
			CookingItem.COOKED_MEAT,
			CookingItem.CHEESE,
			CookingItem.BREAD
		),
		isBossOrder = true
	),
	NONE("", emptyList());

	companion object {
		fun getByName(displayName: String): CookingOrder? {
			return entries.filterNot { it == NONE }.firstOrNull { it.displayName.lowercase() == displayName.lowercase() }
		}

		fun getById(id: String): CookingOrder? = entries.firstOrNull { it.name.lowercase() == id.lowercase() }
	}
}