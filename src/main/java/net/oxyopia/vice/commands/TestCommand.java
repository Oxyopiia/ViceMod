package net.oxyopia.vice.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import gg.essential.api.EssentialAPI;
import gg.essential.universal.wrappers.UPlayer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.oxyopia.vice.utils.Utils;

public class TestCommand {
	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommandManager.literal("test")
			.executes(context -> {
				EssentialAPI.getNotifications().push("Vice", "This is a notification test", 4f, () -> {
					Utils.sendViceMessage("Notification clicked");
					return null;
				});

				Utils.sendViceMessage("inDoomTowers: &&a " + Utils.inDoomTowers());
				Utils.sendViceMessage(UPlayer.getPosX() + " " + UPlayer.getPosY() + " " + UPlayer.getPosZ());

				return Command.SINGLE_SUCCESS;
			})
		);
	}
}