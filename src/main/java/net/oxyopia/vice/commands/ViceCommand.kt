package net.oxyopia.vice.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import gg.essential.universal.UScreen
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.oxyopia.vice.Vice

object ViceCommand {
	fun register(dispatcher: CommandDispatcher<FabricClientCommandSource?>) {
		dispatcher.register(ClientCommandManager.literal("vice")
			.executes {
				MinecraftClient.getInstance().send(Runnable { UScreen.displayScreen(Vice.config.gui()) })
				Command.SINGLE_SUCCESS
			}
		)
	}
}