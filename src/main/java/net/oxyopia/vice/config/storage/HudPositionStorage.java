package net.oxyopia.vice.config.storage;

import com.google.gson.annotations.Expose;
import net.oxyopia.vice.config.HudManager.Position;

public class HudPositionStorage {

	@Expose
	public Cooking cooking = new Cooking();

	public static class Cooking {

		@Expose
		public Position currentOrderDisplay = new Position(0.5f, 0.05f);

	}
}
