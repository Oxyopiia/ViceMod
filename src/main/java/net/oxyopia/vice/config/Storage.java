package net.oxyopia.vice.config;

import com.google.gson.annotations.Expose;
import net.oxyopia.vice.Vice;
import net.oxyopia.vice.config.storage.HudPositionStorage;

import java.io.File;
import java.util.HashMap;

public class Storage extends PersistentSave {
	/*
	{
		"isFirstUse": true,
		"lastVersion": "1.0.0",
		"hud": {
			"cooking": {
				"currentOrderDisplay": {
					"x": 505,
					"y": 30,
					"scale": 1.0,
					"centered": true
				}
			}
		},
		"arenas": {
			"startTimes": {
				"f2arenas": 1792578383625134
				"f3arenas": 1792578383637216
				"f4arenas": 1792578384012349
			}
		},
		"cooking": {
			"currentOrder": "",
			"stock": -10000,
			"totalBurgerRequests": {
				"HAMBURGER": 72,
				"CHEESEBURGER": 74,
				"DOUBLE_CHEESEBURGER": 65,
				"ULTIMEATIUM": 79,
				"OBESE_VICE_BURGER": 4
			},
			"totalBurgersComplete": {
				"HAMBURGER": 70,
				"CHEESEBURGER": 71,
				"DOUBLE_CHEESEBURGER": 42,
				"ULTIMEATIUM": 62,
				"OBESE_VICE_BURGER": 4
			},
		},
		"showdown": {
			"lastKnownTrainSpawn": -1
		}
	}
	 */

	@Expose
	public boolean isFirstUse = true;

	@Expose
	public String lastVersion = Vice.Companion.getVersion().getFriendlyString();

	@Expose
	public HudPositionStorage hud = new HudPositionStorage();

	@Expose
	public WorldData worlds = new WorldData();

	public static class WorldData {

		@Expose
		public ArenaStorage arenas = new ArenaStorage();

		public static class ArenaStorage {

			@Expose
			public HashMap<String, Long> startTimes = new HashMap<>();

		}

		@Expose
		public CookingStorage cooking = new CookingStorage();

		public static class CookingStorage {

			@Expose
			public String currentOrder = "";

			@Expose
			public int stock = -10000;

			@Expose
			public HashMap<String, Integer> totalBurgerRequests = new HashMap<>();

			@Expose
			public HashMap<String, Integer> totalBurgersComplete = new HashMap<>();

		}

		@Expose
		public ShowdownStorage showdown = new ShowdownStorage();

		public static class ShowdownStorage {

			@Expose
			public long lastKnownTrainSpawn = -1L;

		}
	}

	public Storage() {
		super(new File("./config/vice/storage.json"));
	}
}