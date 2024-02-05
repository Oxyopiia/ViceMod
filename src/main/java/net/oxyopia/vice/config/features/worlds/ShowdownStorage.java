package net.oxyopia.vice.config.features.worlds;

import com.google.gson.annotations.Expose;
import net.oxyopia.vice.data.gui.Position;

public class ShowdownStorage {

	@Expose
	public long lastKnownTrainSpawn = -1L;

	@Expose
	public Position trainTimerPos = new Position(175f, 150f);

}