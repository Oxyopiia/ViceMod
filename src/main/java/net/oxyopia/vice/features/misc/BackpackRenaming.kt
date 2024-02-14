package net.oxyopia.vice.features.misc

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ItemRenameEvent
import net.oxyopia.vice.events.ModifyChestNameEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.ItemUtils.getLore
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.Utils.convertFormatting

object BackpackRenaming {
	private const val COMMAND_NAME = "vicerenamebackpack"

	private val storage get() = Vice.storage
	private val idRegex = Regex("ID: (\\d+)")
	private val acceptedBackpacks = listOf(
		"Small Backpack",
		"Medium Backpack",
		"Big Backpack",
		"Titanic Backpack",
		"Barrel Backpack",
		"Fleshpack"
	)

	@SubscribeEvent
	fun onItemRename(event: ItemRenameEvent) {
		if (!Vice.config.BACKPACK_RENAMING || !acceptedBackpacks.contains(event.itemName.string)) return

		val id = event.item.getBackpackId()

		if (storage.misc.backpackNames.containsKey(id)) {
			event.setReturnValue(Text.of("§f${storage.misc.backpackNames[id]}").copy())
		}
	}

	@SubscribeEvent
	fun onChestRename(event: ModifyChestNameEvent) {
		if (!Vice.config.BACKPACK_RENAMING || !event.original.string.endsWith("Backpack")) return

		val stack = Utils.getPlayer()?.mainHandStack ?: return
		val id = stack.getBackpackId() ?: return

		if (storage.misc.backpackNames.containsKey(id)) {
			event.setReturnValue(Text.of("§8${storage.misc.backpackNames[id]}"))
		}
	}

	private fun handleCommand(context: CommandContext<FabricClientCommandSource>) {
		val stack = Utils.getPlayer()?.mainHandStack ?: return warnEmpty()

		val id = stack.getBackpackId() ?: return warnEmpty()
		val argument = context.getArgument("name", String::class.java)

		if (argument.lowercase() == "reset") {
			storage.misc.backpackNames.remove(id)
			storage.markDirty()

			Utils.sendViceMessage("&&cReset the name of Backpack &&f$id&&c!")
			return
		}

		storage.misc.backpackNames[id] = argument.replace(Regex("&(.)"), "&&$1").convertFormatting()
		storage.markDirty()

		Utils.sendViceMessage("&&aChanged the name of Backpack &&f$id &&ato &&f$argument&&a!")
		Utils.playSound("random.orb", volume = 3f)
	}

	private fun warnEmpty() = DevUtils.sendWarningMessage("You are not holding a backpack!", false)

	fun registerCommand(dispatcher: CommandDispatcher<FabricClientCommandSource?>) {
		dispatcher.register(
			literal(COMMAND_NAME)
				.then(argument("name", greedyString())
					.executes { context ->
						handleCommand(context)
						Command.SINGLE_SUCCESS
					}
				)

				.executes { _ ->
					DevUtils.sendWarningMessage("&&f/$COMMAND_NAME (name|reset)", false)
					Command.SINGLE_SUCCESS
				}
		)
	}

	private fun ItemStack.getBackpackId(): String? {
		return getLore().firstNotNullOfOrNull { idRegex.find(it)?.groupValues?.get(1) }
	}
}