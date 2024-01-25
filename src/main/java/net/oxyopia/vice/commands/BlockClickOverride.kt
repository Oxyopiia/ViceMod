package net.oxyopia.vice.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.timeDelta
import java.util.concurrent.TimeUnit

object BlockClickOverride {
	private var blockClickTime = 0L

	fun isActive(): Boolean {
		return blockClickTime.timeDelta() <= TimeUnit.SECONDS.toMillis(10)
	}

	fun register(dispatcher: CommandDispatcher<FabricClientCommandSource?>) {
		dispatcher.register(ClientCommandManager.literal("viceclickoverride")
			.executes { _: CommandContext<FabricClientCommandSource?>? ->

				blockClickTime = System.currentTimeMillis()
				Utils.sendViceMessage("Blocked Clicks are overridden for the next 10 seconds!")
				Utils.playSound("block.note_block.pling")

				Command.SINGLE_SUCCESS
			}
		)
	}
}