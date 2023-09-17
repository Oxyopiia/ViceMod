package net.oxyopia.vice.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import gg.essential.universal.UChat;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.oxyopia.vice.utils.PlayerUtils;

public class WorldCommand {
	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommandManager.literal("world")
			.executes(context -> {
				UChat.chat(PlayerUtils.getWorld());

				return Command.SINGLE_SUCCESS;
			})
		);
	}
}