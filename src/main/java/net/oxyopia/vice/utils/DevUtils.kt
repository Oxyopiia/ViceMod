package net.oxyopia.vice.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.config.DevConfig
import net.oxyopia.vice.utils.Utils.convertFormatting

object DevUtils {
	fun sendDebugChat(msg: String) {
		if (!Vice.config.DEVMODE) return
		MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.of(Vice.DEV_PREFIX + msg.convertFormatting()))
	}

	@JvmStatic
	fun sendDebugChat(msg: String, fieldName: String) {
		if (!Vice.config.DEVMODE) return

		try {
			val field = DevConfig::class.java.getDeclaredField(fieldName) ?: return
			field.isAccessible = true
			val value = field.getBoolean(Vice.devConfig)

			if (value) {
				sendDebugChat(msg)
			}
		} catch (e: NoSuchFieldException) {
			sendErrorMessage(e, "An error occurred sending a debug chat (really meta i know)!")
		} catch (e: IllegalAccessException) {
			sendErrorMessage(e, "An error occurred sending a debug chat (really meta i know)!")
		}
	}

	fun sendErrorMessage(throwable: Throwable, msg: String) {
		val stackTrace = throwable.stackTrace
		val stackTraceString = StringBuilder(throwable.message)

		for (element in stackTrace) {
			stackTraceString.append("\n\t").append(element.toString())
		}

		val joinedStackTrace = stackTraceString.toString()

		val errorChat: Text = Text.literal(Vice.ERROR_PREFIX + msg + "§7 Click to copy the error to clipboard!")
			.setStyle(
				Style.EMPTY
					.withClickEvent(ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, joinedStackTrace))
					.withHoverEvent(
						HoverEvent(
							HoverEvent.Action.SHOW_TEXT,
							Text.literal("§7Click to copy the error to your clipboard!\n§c$msg")
						)
					)
			)

		Vice.logger.error(throwable.message, throwable)
		MinecraftClient.getInstance().inGameHud.chatHud.addMessage(errorChat)
	}

	fun sendWarningMessage(msg: String) {
		MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.of(Vice.WARNING_PREFIX + msg.convertFormatting()))
	}

	fun sendWarningMessage(msg: String, copyableData: String?) {
		val warningChat: Text = Text.literal(Vice.WARNING_PREFIX + msg.convertFormatting())
			.setStyle(
				Style.EMPTY
					.withClickEvent(ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyableData))
					.withHoverEvent(
						HoverEvent(
							HoverEvent.Action.SHOW_TEXT,
							Text.literal("§eClick to copy some useful data to your clipboard!")
						)
					)
			)

		MinecraftClient.getInstance().inGameHud.chatHud.addMessage(warningChat)
	}
}
