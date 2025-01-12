package net.oxyopia.vice.data

import net.oxyopia.vice.utils.Utils
import java.awt.Color

enum class World(val id: String, val displayName: String, val properties: List<WorldProperty> = emptyList(), val displayColor: Color = Color.white) {
	RealityPeak("realitypeak", "Reality Peak"),
	Desert("deserteddunes", "Deserted Dunes"),
	Space("spaceescape", "World 3 or 4"),
	Burger("spaceescape", "World 3 or 4"),
	Supermarket("supermarket", "Supermarket"),
	Temple("losttemple", "Lost Temple"),
	ArcticAssault("arcticassault", "Arctic Assault"),
	MagmaHeights("magmaheights", "Magma Heights"),
	Volume("volume", "Volume"),
	Glimpse("glimpse", "Glimpse"),
	Arcade("arcade", "The Arcade"),
	ArcadeVirtualWorld("tutorial", "The Arcade: Virtual World"),
	Showdown("showdown", "Showdown"),
	GlitchHQ("glitchhq", "Journey to the Glitch HQ"),
	Warehouse("testingzone", "Warehouse"),
	SoulswiftSands("soulswiftsands", "Soulswift Sands"),
	TimelessTastes("timelesstastes", "Timeless Tastes"),
	StarryStreets("starrystreets", "Starry Streets"),

	Vice("viceboss", "Vice", properties = listOf(WorldProperty.BOSS, WorldProperty.MASTERABLE)),
	Wasteyard("wasteyard", "Wasteyard", properties = listOf(WorldProperty.BOSS, WorldProperty.MASTERABLE)),
	Gelato("corruptedvice", "El Gelato", properties = listOf(WorldProperty.BOSS, WorldProperty.MASTERABLE)),
	PPP("fakeplayer", "PPP", properties = listOf(WorldProperty.BOSS, WorldProperty.MASTERABLE)),
	Minehut("minehutboss", "Minehut Boss", properties = listOf(WorldProperty.BOSS, WorldProperty.MASTERABLE)),
	AbyssalVice("darkvice", "Abyssal Vice", properties = listOf(WorldProperty.BOSS)),
	ShadowGelato("shadowgelato", "Shadow Gelato", properties = listOf(WorldProperty.BOSS)),
	Diox("dioxarena", "Diox", properties = listOf(WorldProperty.BOSS)),
	Elderpork("elderpork", "Elderpork", properties = listOf(WorldProperty.BOSS, WorldProperty.MASTERABLE)),

	Floor2Arena("f2arenas", "Void Voyage", properties = listOf(WorldProperty.ARENA), displayColor = Colors.ChatColor.Green),
	Floor3Arena("f3arenas", "Cryonic Caverns", properties = listOf(WorldProperty.ARENA), displayColor = Colors.ChatColor.Blue),
	Floor4Arena("f4arenas", "Tidal Zone", properties = listOf(WorldProperty.ARENA), displayColor = Colors.ChatColor.Cyan),

	Exonitas("bigcity", "Exonitas", properties = listOf(WorldProperty.AUXILIARY)),

	Expeditions("expeditions", "Expeditions", properties = listOf(WorldProperty.EXPEDITION)),
	Pillars("pillars", "Pillars"),

	Summer("summer", "Summer", properties = listOf(WorldProperty.EVENT)),

	Tower("overworld", "The Tower", properties = listOf(WorldProperty.TOWER));

	fun isInWorld(): Boolean {
		Utils.getWorldString()?.let {
			return Utils.inDoomTowers && this.id == it
		}

		return false
	}

	enum class WorldProperty {
		BOSS,
		MASTERABLE,
		ARENA,
		AUXILIARY,
		EXPEDITION,
		EVENT,
		TOWER
	}

	companion object {
		fun getById(id: String) = entries.firstOrNull { it.id == id }
	}
}