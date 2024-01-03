package net.oxyopia.vice.utils;

import gg.essential.universal.UChat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.oxyopia.vice.Vice;
import net.oxyopia.vice.config.DevConfig;
import java.lang.reflect.Field;

import static net.oxyopia.vice.Vice.*;

public class DevUtils {
	public static void sendDebugChat(String msg) {
		if (Vice.config.DEVMODE) {
			UChat.chat(Vice.DEV_PREFIX + msg.replaceAll("&&", "§"));
		}
	}

	public static void sendDebugChat(String msg, String fieldName) {
		try {
			Field field = DevConfig.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			boolean value = (boolean) field.get(devConfig);

			if (value) {
				sendDebugChat(msg);
			}
		} catch (NoSuchFieldException | IllegalAccessException e) {
			sendErrorMessage(e, "An error occurred sending a debug chat (really meta i know)!");
		}
	}

	public static void sendErrorMessage(Throwable throwable, String msg) {
		StackTraceElement[] stackTrace = throwable.getStackTrace();
		StringBuilder stackTraceString = new StringBuilder();

		for (StackTraceElement element : stackTrace) {
			stackTraceString.append(element.toString()).append("\n");
		}

		String joinedStackTrace = stackTraceString.toString();

		Text errorChat = Text.literal(ERROR_PREFIX + msg + "§e Click to copy the error to clipboard!")
			.setStyle(Style.EMPTY
				.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, joinedStackTrace))
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("§eClick to copy the error to your clipboard!\n§7§o"+msg))));

		MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(errorChat);
	}

	public static void sendWarningMessage(String msg) {
		Text warningChat = Text.literal(WARNING_PREFIX + msg)
			.setStyle(Style.EMPTY
				.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, msg))
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("§eClick to copy the warning to your clipboard!\n§7§o"+msg))));


		MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(warningChat);
	}


}
