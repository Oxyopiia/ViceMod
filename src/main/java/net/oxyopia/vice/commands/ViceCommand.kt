package net.oxyopia.vice.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import gg.essential.universal.UScreen
import gg.essential.universal.wrappers.message.UTextComponent
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Items
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.Vice.Companion.EVENT_MANAGER
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.events.ActionBarEvent
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.CommandRegisterEvent
import net.oxyopia.vice.events.SoundEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.ChatUtils
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.ItemUtils
import net.oxyopia.vice.utils.ItemUtils.getNbtString
import net.oxyopia.vice.utils.SoundUtils
import net.oxyopia.vice.utils.Utils
import java.awt.Color

object ViceCommand {
	@SubscribeEvent
	fun register(event: CommandRegisterEvent) {
		event.register(
			ClientCommandManager.literal("vice")
				.executes {
					MinecraftClient.getInstance().send(Runnable { UScreen.displayScreen(Vice.config.gui()) })
					Command.SINGLE_SUCCESS
				}
				.then(ClientCommandManager.literal("config")
					.executes {
						MinecraftClient.getInstance().send(Runnable { UScreen.displayScreen(Vice.config.gui()) })
						Command.SINGLE_SUCCESS
					})
				.then(ClientCommandManager.literal("dev")
					.executes {
						MinecraftClient.getInstance().send(Runnable { UScreen.displayScreen(Vice.devConfig.gui()) })
						Command.SINGLE_SUCCESS
					}
					.addDevSubcommands()
				)
		)
	}

	private fun LiteralArgumentBuilder<FabricClientCommandSource?>.addDevSubcommands(): LiteralArgumentBuilder<FabricClientCommandSource?> {
		return this
			.then(ClientCommandManager.literal("toggle")
				.executes {
					Vice.config.DEVMODE = !Vice.config.DEVMODE
					Vice.config.markDirty()

					val status = if (Vice.config.DEVMODE) "§aenabled" else "§cdisabled"
					ChatUtils.sendViceMessage("§7Dev mode: $status")
					Command.SINGLE_SUCCESS
				}
			)
			.then(ClientCommandManager.literal("converthex")
				.then(ClientCommandManager.argument("hex", StringArgumentType.word())
					.executes {
						val hex = StringArgumentType.getString(it, "hex")
						try {
							var formattedHex = hex
							if (formattedHex.startsWith("#")) formattedHex = formattedHex.replace("#", "")
							if (!formattedHex.startsWith("0x")) formattedHex = "0x$hex"
							val color = Color.decode(formattedHex)

							ChatUtils.sendViceMessage(
								UTextComponent(
									"Converted hex ".toText().append(hex.toText(color))
										.append(" to rgb: ".toText(Colors.ChatColor.Grey))
										.append("Color(${color.red},${color.green},${color.blue})".toText(color))
								)
									.setClick(
										ClickEvent.Action.COPY_TO_CLIPBOARD,
										"(${color.red},${color.green},${color.blue})"
									)
									.setHover(HoverEvent.Action.SHOW_TEXT, "Click to copy color".toText(color))
							)
						} catch (e: NumberFormatException) {
							ChatUtils.sendViceMessage("Invalid hex string: $hex".toText(Colors.ChatColor.Red))
						}
						Command.SINGLE_SUCCESS
					}
				)
			)
			.then(ClientCommandManager.literal("tree")
				.executes {
					val subscribers = EVENT_MANAGER.subscribers
					ChatUtils.sendViceMessage("&&e&&lVICE EVENT TREE")

					subscribers.forEach { (event, listeners) ->
						ChatUtils.sendViceMessage("&&a${event.name.removePrefix("net.oxyopia.")}")

						listeners.forEach { listener ->
							val classText =
								listener.source.javaClass.packageName.removePrefix("net.oxyopia.") + ".&&b" + listener.source.javaClass.simpleName

							ChatUtils.sendViceMessage("&&f\\- &&8${classText}&&8/${listener.target.name}()")
						}
					}

					Command.SINGLE_SUCCESS
				}
			)
			.then(ClientCommandManager.literal("data")
				.executes {
					sendDevData()
					Command.SINGLE_SUCCESS
				}
			)
			.then(ClientCommandManager.literal("message")
				.then(ClientCommandManager.argument("message", StringArgumentType.greedyString())
					.executes {
						val message = StringArgumentType.getString(it, "message")

						EVENT_MANAGER.publish(ChatEvent(Text.of(message)))
						ChatUtils.sendViceMessage("§aSent ChatEvent!")
						Command.SINGLE_SUCCESS
					}
				)
			)
			.then(ClientCommandManager.literal("sound")
				.then(ClientCommandManager.argument("sound", StringArgumentType.word())
					.then(ClientCommandManager.argument("volume", FloatArgumentType.floatArg(0f))
						.then(ClientCommandManager.argument("pitch", FloatArgumentType.floatArg(0f, 2f))
							.executes {
								val soundName = StringArgumentType.getString(it, "sound")
								val pitch = FloatArgumentType.getFloat(it, "pitch")
								val volume = FloatArgumentType.getFloat(it, "volume")
								EVENT_MANAGER.publish(SoundEvent(soundName, pitch, volume))
								SoundUtils.playSound(soundName, pitch, volume)
								ChatUtils.sendViceMessage("§aSent SoundEvent! §7($soundName, §d$pitch, §e$volume§7)")
								Command.SINGLE_SUCCESS
							}
						)
					)
				)
			)
			.then(ClientCommandManager.literal("actionbar")
				.then(ClientCommandManager.argument("text", StringArgumentType.greedyString())
					.executes {
						val message = StringArgumentType.getString(it, "text").toText()
						EVENT_MANAGER.publish(ActionBarEvent(message))
						HudUtils.sendVanillaActionBar(message)
						ChatUtils.sendViceMessage("§aSent ActionBarEvent!")
						Command.SINGLE_SUCCESS
					}
				)
			)

	}

	private fun sendDevData() {
		ChatUtils.sendViceMessage("inDoomTowers: &&a${Utils.inDoomTowers}")

		val heldItem = ItemUtils.getHeldItem()

		if (heldItem.components != null) {
			ChatUtils.sendViceMessage("")
			ChatUtils.sendViceMessage(heldItem.name)

			val nbtData = heldItem.getNbtString()

			ChatUtils.sendViceMessage(
				UTextComponent("§a&l ● Click to copy the entire set of components.§r")
					.setClick(ClickEvent.Action.COPY_TO_CLIPBOARD, nbtData)
					.setHover(HoverEvent.Action.SHOW_ITEM, HoverEvent.ItemStackContent(heldItem))
			)

			heldItem.components.forEach { component ->
				ChatUtils.sendViceMessage(
					UTextComponent("§e ○ Click to copy the &a${component.type} &ecomponent.§r")
						.setClick(ClickEvent.Action.COPY_TO_CLIPBOARD, component.toString())
						.setHover(HoverEvent.Action.SHOW_TEXT, Text.of(component.toString()))
				)
			}

			if (heldItem.isOf(Items.PLAYER_HEAD)) {
				val nbtString = heldItem.getNbtString()

				val textureRegex = Regex("""Property\[name=textures, value=([\w+=/]+)""")
				val match = textureRegex.find(nbtString) ?: return

				val textureValue = match.groupValues[1]

				ChatUtils.sendViceMessage(
					UTextComponent("§a&l ● Click to copy the &aPlayer Head Texture ID.§r")
						.setClick(ClickEvent.Action.COPY_TO_CLIPBOARD, textureValue)
						.setHover(HoverEvent.Action.SHOW_TEXT, Text.of(textureValue))
				)
			}
		} else {
			ChatUtils.sendViceMessage("§cNo NBT data found in held tool.")
		}
	}
}
