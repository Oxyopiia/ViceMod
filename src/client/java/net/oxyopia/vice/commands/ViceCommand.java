package net.oxyopia.vice.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import gg.essential.universal.UScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import static net.oxyopia.vice.Vice.config;

public class ViceCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommandManager.literal("vice")
			.executes(context -> {
				MinecraftClient.getInstance().send(() -> UScreen.displayScreen(config.gui()));

				return Command.SINGLE_SUCCESS;
			})
		);
    }
}