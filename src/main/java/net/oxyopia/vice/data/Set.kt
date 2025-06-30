package net.oxyopia.vice.data

import java.awt.Color

enum class Set(val text: String, val color: Color = Color.white) {
    CHEF("Chef", Color(255, 208, 75)),
    DEMOLITIONIST("Demolitionist", Color(92, 255, 135)),
    FLESHCRAWLER("Fleshcrawler", Color(140, 34, 18)),
    DIGITAL("Digital", Color(231, 65, 152)),
    HEAVY("Heavy", Color(214, 214, 214)),
    TRICKSTER("Trickster", Color(105, 12, 214)),
    NONE("");

    companion object {
        fun getByName(name: String) = entries.firstOrNull { it.text == name } ?: NONE
    }
}
