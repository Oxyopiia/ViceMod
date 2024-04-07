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
		if (!Vice.config.DEVMODE) return;

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
		StringBuilder stackTraceString = new StringBuilder(throwable.getMessage());

		for (StackTraceElement element : stackTrace) {
			stackTraceString.append("\n\t").append(element.toString());
		}

		String joinedStackTrace = stackTraceString.toString();

		Text errorChat = Text.literal(ERROR_PREFIX + msg + "§7 Click to copy the error to clipboard!")
			.setStyle(Style.EMPTY
				.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, joinedStackTrace))
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("§7Click to copy the error to your clipboard!\n§c"+msg))));

		Vice.Companion.getLogger().error(throwable.getMessage(), throwable);
		if (MinecraftClient.getInstance().inGameHud.getChatHud() != null) {
			MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(errorChat);
		}
	}

	public static void sendWarningMessage(String msg) {
		msg = msg.replaceAll("&&", "§");
		MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of(WARNING_PREFIX + msg));
	}

	public static void sendWarningMessage(String msg, String copyableData) {
		msg = msg.replaceAll("&&", "§");

		Text warningChat = Text.literal(WARNING_PREFIX + msg)
			.setStyle(Style.EMPTY
				.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyableData))
				.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("§eClick to copy some useful data to your clipboard!")))
			);

		MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(warningChat);
	}
}
