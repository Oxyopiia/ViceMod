package net.oxyopia.vice.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import gg.essential.universal.wrappers.message.UTextComponent
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.HoverEvent.ItemStackContent
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.utils.ItemUtils.getHeldItem
import net.oxyopia.vice.utils.ItemUtils.getNbtString
import net.oxyopia.vice.utils.Utils.inDoomTowers
import net.oxyopia.vice.utils.Utils.sendViceMessage

object DevDataCommand {
	fun register(dispatcher: CommandDispatcher<FabricClientCommandSource?>) {
		dispatcher.register(ClientCommandManager.literal("vicedevdata")
			.executes {

				sendViceMessage("inDoomTowers: &&a$inDoomTowers")

				val heldItem = getHeldItem()

				if (Vice.devConfig.COMMAND_SHOW_ITEM_DATA && heldItem.components != null) {
					sendViceMessage("")
					sendViceMessage(heldItem.name)

					val nbtData = heldItem.getNbtString()

					sendViceMessage(
						UTextComponent("§a&l ● Click to copy the entire set of components.§r")
							.setClick(ClickEvent.Action.COPY_TO_CLIPBOARD, nbtData)
							.setHover(HoverEvent.Action.SHOW_ITEM, ItemStackContent(heldItem))
					)

					heldItem.components.forEach { component ->
						sendViceMessage(
							UTextComponent("§e ○ Click to copy the &a${component.type} &ecomponent.§r")
								.setClick(ClickEvent.Action.COPY_TO_CLIPBOARD, component.toString())
								.setHover(HoverEvent.Action.SHOW_TEXT, Text.of(component.toString()))
						)
					}
				}

				Command.SINGLE_SUCCESS
			}
		)
	}
}