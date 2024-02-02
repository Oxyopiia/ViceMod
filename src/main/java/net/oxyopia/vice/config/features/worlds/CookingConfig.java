package net.oxyopia.vice.config.features.worlds;

import com.google.gson.annotations.Expose;
import net.oxyopia.vice.config.HudManager;

import java.util.HashMap;

public class CookingConfig {

	@Expose
	public String currentOrder = "";

	@Expose
	public int stock = -10000;

	@Expose
	public HashMap<String, Integer> totalBurgerRequests = new HashMap<>();

	@Expose
	public HashMap<String, Integer> totalBurgersComplete = new HashMap<>();

	@Expose
	public HudManager.Position currentOrderPos = new HudManager.Position(0.5f, 0.05f);

}