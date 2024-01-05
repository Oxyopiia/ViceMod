package net.oxyopia.vice.utils.enums

enum class Set(val text: String) {
    CHEF("Chef"),
    DEMOLITIONIST("Demolitionist"),
    FLESHCRAWLER("Fleshcrawler"),
    DIGITAL("Digital"),
    HEAVY("Heavy"),
    NONE("");

    companion object {
        fun getByName(name: String): Set {
            // Returns NONE if unknown, or if new set i suppose, maybe i'll make a failsafe soon idk?
            return entries.firstOrNull { it.text == name } ?: NONE
        }
    }
}
