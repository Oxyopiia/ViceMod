package net.oxyopia.vice.utils.enums;

public enum Worlds {
	RealityPeak("realitypeak"),
	Desert("desertedDunes"),
	Space("spaceescape"),
	Burger("spaceescape"),
	Temple("losttemple"),
	ArcticAssault("arcticassault"),
	MagmaHeights("magmaheights"),
	Volume("volume"),
	Glimpse("glimpse"),
	Arcade("arcade"),
	ArcadeVirtualWorld("tutorial"),
	Showdown("showdown"),

	Vice("viceboss"),
	Wasteyard("wasteyard"),
	Gelato("corruptedvice"),
	PPP("fakeplayer"),
	Minehut("minehutboss"),
	DarkVice("darkvice"),
	ShadowGelato("shadowgelato"),

	Floor2Arena("f2arenas"),
	Floor3Arena("f3arenas"),
	Floor4Arena("f4arenas"),

	Tower("world");

	private final String worldName;

	Worlds(String worldName) {
		this.worldName = worldName;
	}

	public String getId() {
		return worldName;
	}
}
