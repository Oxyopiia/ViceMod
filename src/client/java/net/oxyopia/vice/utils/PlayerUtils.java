package net.oxyopia.vice.utils;

import net.minecraft.client.MinecraftClient;

public class PlayerUtils {
	public static String getWorld() {
		MinecraftClient client = MinecraftClient.getInstance();

		assert client.world != null;
		return client.world.getRegistryKey().getValue().getPath();
	}
}
