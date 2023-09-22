package net.oxyopia.vice.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import gg.essential.api.EssentialAPI;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.oxyopia.vice.utils.PlayerUtils;

public class TestCommand {
	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommandManager.literal("test")
			.executes(context -> {
				EssentialAPI.getNotifications().push("Vice", "This is a notification test", 4f, () -> {
					PlayerUtils.sendViceMessage("Notification clicked");
					return null;
				});


				return Command.SINGLE_SUCCESS;
			})
		);
	}
}