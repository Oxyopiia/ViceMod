package net.oxyopia.vice.data

enum class Set(val text: String) {
    CHEF("Chef"),
    DEMOLITIONIST("Demolitionist"),
    FLESHCRAWLER("Fleshcrawler"),
    DIGITAL("Digital"),
    HEAVY("Heavy"),
    NONE("");

    companion object {
        fun getByName(name: String) = entries.firstOrNull { it.text == name } ?: NONE
    }
}
