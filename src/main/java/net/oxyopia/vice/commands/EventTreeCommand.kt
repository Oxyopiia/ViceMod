package net.oxyopia.vice.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.oxyopia.vice.Vice.Companion.EVENT_MANAGER
import net.oxyopia.vice.utils.Utils

object EventTreeCommand {
	fun register(dispatcher: CommandDispatcher<FabricClientCommandSource?>) {
		dispatcher.register(ClientCommandManager.literal("vicedevtree")
			.executes {

				val subscribers = EVENT_MANAGER.subscribers
				Utils.sendViceMessage("&&e&&lVICE EVENT TREE")

				subscribers.forEach {(event, listeners) ->
					Utils.sendViceMessage("&&a${event.name.removePrefix("net.oxyopia.")}")

					listeners.forEach { listener ->
						val classText = listener.source.javaClass.packageName.removePrefix("net.oxyopia.") + ".&&b" + listener.source.javaClass.simpleName

						Utils.sendViceMessage("&&f\\- &&8${classText}&&8/${listener.target.name}()")
					}
				}

				Command.SINGLE_SUCCESS
			}
		)
	}
}