package net.oxyopia.vice.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import gg.essential.universal.UScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.oxyopia.vice.Vice.*;

public class ViceCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommandManager.literal("vice")
			.executes(context -> {
				client.send(() -> UScreen.displayScreen(config.gui()));

				return Command.SINGLE_SUCCESS;
			})
		);
    }
}