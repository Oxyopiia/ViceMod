package net.oxyopia.vice.utils.enums

import net.oxyopia.vice.utils.Utils

enum class World(val id: String, val displayName: String, val type: WorldType = WorldType.NORMAL) {
	RealityPeak("realitypeak", "Reality Peak"),
	Desert("deserteddunes", "Deserted Dunes"),
	Space("spaceescape", "World 3 or 4"),
	Burger("spaceescape", "World 3 or 4"),
	Temple("losttemple", "Lost Temple"),
	ArcticAssault("arcticassault", "Arctic Assault"),
	MagmaHeights("magmaheights", "Magma Heights"),
	Volume("volume", "Volume"),
	Glimpse("glimpse", "Glimpse"),
	Arcade("arcade", "The Arcade"),
	ArcadeVirtualWorld("tutorial", "The Arcade: Virtual World"),
	Showdown("showdown", "Showdown"),

	Vice("viceboss", "Vice", type = WorldType.BOSS),
	Wasteyard("wasteyard", "Wasteyard", type = WorldType.BOSS),
	Gelato("corruptedvice", "El Gelato", type = WorldType.BOSS),
	PPP("fakeplayer", "PPP", type = WorldType.BOSS),
	Minehut("minehutboss", "Minehut Boss", type = WorldType.BOSS),
	DarkVice("darkvice", "Abyssal Vice", type = WorldType.BOSS),
	ShadowGelato("shadowgelato", "Shadow Gelato", type = WorldType.BOSS),

	Floor2Arena("f2arenas", "Void Voyage", type = WorldType.ARENA),
	Floor3Arena("f3arenas", "Cryonic Caverns", type = WorldType.ARENA),
	Floor4Arena("f4arenas", "Tidal Zone", type = WorldType.ARENA),

	Tower("overworld", "The Tower", type = WorldType.TOWER);

	fun isInWorld(): Boolean {
		Utils.getWorldString()?.let {
			return Utils.inDoomTowers && this == World.getById(it)
		}

		return false
	}

	enum class WorldType {
		NORMAL,
		BOSS,
		ARENA,
		TOWER
	}

	companion object {
		fun getById(id: String): World? {
			return entries.firstOrNull { it.id == id }
		}
	}
}