package net.oxyopia.vice.utils

import gg.essential.universal.UChat
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.utils.Utils.convertFormatting

object ChatUtils {
	private val client = MinecraftClient.getInstance()

	fun sendViceMessage(msg: Text) {
		val prefix = Text.literal(Vice.CHAT_PREFIX)
		client.inGameHud.chatHud.addMessage(prefix.append(msg))
	}

	fun sendViceMessage(msg: String) {
		UChat.chat("${Vice.CHAT_PREFIX}${msg.convertFormatting()}")
	}

	fun sendViceMessage(msg: UTextComponent) {
		msg.text = "${Vice.CHAT_PREFIX}${msg.text}"
		UChat.chat(msg)
	}
}