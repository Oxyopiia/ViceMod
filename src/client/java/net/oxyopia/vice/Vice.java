package net.oxyopia.vice;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.oxyopia.vice.commands.ViceCommand;
import net.oxyopia.vice.commands.WorldCommand;
import net.oxyopia.vice.config.Config;
import org.slf4j.Logger;

public class Vice implements ClientModInitializer {
	private static final Logger LOGGER = LogUtils.getLogger();
	public static Config config = new Config();

	@Override
	public void onInitializeClient() {
		config.init();

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			ViceCommand.register(dispatcher);
			WorldCommand.register(dispatcher);
		});
	}
}