package net.oxyopia.vice.config;

import com.google.gson.annotations.Expose;
import net.oxyopia.vice.Vice;
import net.oxyopia.vice.config.features.MiscStorage;
import net.oxyopia.vice.config.features.worlds.ArenaStorage;
import net.oxyopia.vice.config.features.worlds.CookingStorage;
import net.oxyopia.vice.config.features.worlds.ShowdownStorage;

import java.io.File;

public class Storage extends PersistentSave {
	/*
	EXPECTED EXAMPLE RESULT

	{
		"isFirstUse": true,
		"lastVersion": "1.0.0",
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
			"currentOrderPos": {
				"x": 0.5,
				"y": 0.05,
				"scale": 1.0,
				"centered": true
			}
		},
		"showdown": {
			"lastKnownTrainSpawn": -1,
			"trainTimerPos": {
				"x": 0.5,
				"y": 0.8,
				"scale": 1.0,
				"centered": true
			}
		},
		"misc": {
			"lastDailyReward": -1
		}
	}
	 */

	@Expose
	public boolean isFirstUse = true;

	@Expose
	public String lastVersion = Vice.Companion.getVersion().getFriendlyString();

	@Expose
	public ArenaStorage arenas = new ArenaStorage();

	@Expose
	public CookingStorage cooking = new CookingStorage();

	@Expose
	public ShowdownStorage showdown = new ShowdownStorage();

	@Expose
	public MiscStorage misc = new MiscStorage();

	public Storage() {
		super(new File("./config/vice/storage.json"));
	}
}