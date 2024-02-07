package net.oxyopia.vice.config.features.worlds;

import com.google.gson.annotations.Expose;
import net.oxyopia.vice.data.gui.Position;

import java.util.HashMap;

public class CookingStorage {

	@Expose
	public String currentOrder = "";

	@Expose
	public int stock = -10000;

	@Expose
	public HashMap<String, Integer> totalBurgerRequests = new HashMap<>();

	@Expose
	public HashMap<String, Integer> totalBurgersComplete = new HashMap<>();

	@Expose
	public Position currentOrderPos = new Position(175f, 10f);

}