package net.oxyopia.vice.config.features.worlds;

import com.google.gson.annotations.Expose;
import net.oxyopia.vice.config.HudManager;

public class ShowdownConfig {

	@Expose
	public long lastKnownTrainSpawn = -1L;

	@Expose
	public HudManager.Position trainTimerPos = new HudManager.Position(0.5f, 0.8f);

}