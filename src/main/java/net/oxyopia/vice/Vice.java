package net.oxyopia.vice;

import net.fabricmc.api.ClientModInitializer;

public class Vice implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		System.out.println("Vice ClientMod Loaded!");
	}
}