package net.oxyopia.vice.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import gg.essential.api.EssentialAPI
import gg.essential.universal.wrappers.message.UTextComponent
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.HoverEvent.ItemStackContent
import net.oxyopia.vice.utils.ItemUtils.getHeldItem
import net.oxyopia.vice.utils.Utils.inDoomTowers
import net.oxyopia.vice.utils.ChatUtils.sendViceMessage

object DevDataCommand {
	fun register(dispatcher: CommandDispatcher<FabricClientCommandSource?>) {
		dispatcher.register(ClientCommandManager.literal("vicedevdata")
			.executes {

				EssentialAPI.getNotifications().pushWithDurationAndAction("Vice", "This is a notification test", 4f) {
					sendViceMessage("Notification clicked")
				}
				sendViceMessage("inDoomTowers: &&a$inDoomTowers")

				val heldItem = getHeldItem()

				if (heldItem.nbt != null) {
					sendViceMessage(
						UTextComponent("§eClick to copy your held item's NBT.§r")
							.setClick(ClickEvent.Action.COPY_TO_CLIPBOARD, heldItem.nbt!!.asString())
							.setHover(HoverEvent.Action.SHOW_ITEM, ItemStackContent(heldItem))
					)
				}
				Command.SINGLE_SUCCESS
			}
		)
	}
}