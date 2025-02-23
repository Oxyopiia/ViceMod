package net.oxyopia.vice.commands

import com.mojang.brigadier.Command
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.oxyopia.vice.config.repo.RepoManager
import net.oxyopia.vice.events.CommandRegisterEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.ChatUtils

object RepoTester {
	@SubscribeEvent
	fun register(event: CommandRegisterEvent) {
		event.register(
			ClientCommandManager.literal("repotest")
				.executes {
					val itemsJson = RepoManager.data?.items ?: return@executes Command.SINGLE_SUCCESS
					itemsJson.items.forEach { item ->
						ChatUtils.sendViceMessage("${item.itemName}, ${item.cooldown}")
					}
					Command.SINGLE_SUCCESS
				}
		)
	}
}