package net.oxyopia.vice.config.features.worlds;

import com.google.gson.annotations.Expose;

import java.util.HashMap;

public class ArenaStorage {

	@Expose
	public HashMap<String, Long> startTimes = new HashMap<>();

}
