package net.oxyopia.vice;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.oxyopia.vice.commands.ViceCommand;
import net.oxyopia.vice.commands.TestCommand;
import net.oxyopia.vice.config.Config;
import org.slf4j.Logger;

// Silence spawns every 5 minutes, and lasts for 1 minute exclusiove
// So silence is despawned for 4 of the 5 minutes
// And alive for 1 of the 5

public class Vice implements ClientModInitializer {
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final Config config = new Config();
	public static MinecraftClient client;

	public static String chatPrefix = "§bVice §7|§r ";
	public static String devPrefix = "§9Vice §7(Dev) |§r ";

	@Override
	public void onInitializeClient() {
		config.init();
		client = MinecraftClient.getInstance();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			ViceCommand.register(dispatcher);
			TestCommand.register(dispatcher);
		});
	}
}