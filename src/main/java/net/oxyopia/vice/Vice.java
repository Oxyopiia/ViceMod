package net.oxyopia.vice;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.oxyopia.vice.commands.ViceCommand;
import net.oxyopia.vice.commands.TestCommand;
import net.oxyopia.vice.config.*;
import org.slf4j.Logger;

public class Vice implements ClientModInitializer {
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final Config config = new Config();
	public static final DevConfig devConfig = new DevConfig();
	public static MinecraftClient client;

	public static final String chatPrefix = "§bVice §7|§r ";
	public static final String errorPrefix = "§cVice §cERROR §7|§c ";
	public static final String devPrefix = "§9Vice §7(Dev) |§r ";

	@Override
	public void onInitializeClient() {
		config.init();
		devConfig.init();

		client = MinecraftClient.getInstance();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			ViceCommand.register(dispatcher);
			TestCommand.register(dispatcher);
		});
	}
}

