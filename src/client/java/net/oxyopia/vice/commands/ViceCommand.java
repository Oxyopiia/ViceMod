package net.oxyopia.vice.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import gg.essential.universal.UScreen;
import gg.essential.vigilance.gui.SettingsGui;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.oxyopia.vice.config.Config;

public class ViceCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommandManager.literal("vice")
			.executes(context -> {
				SettingsGui ConfigUI = new Config().gui();

				MinecraftClient.getInstance().send(() -> UScreen.displayScreen(ConfigUI));

				return Command.SINGLE_SUCCESS;
			})
		);
    }
}