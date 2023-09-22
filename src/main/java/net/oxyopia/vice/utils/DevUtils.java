package net.oxyopia.vice.utils;

import gg.essential.universal.UChat;
import net.oxyopia.vice.Vice;


public class DevUtils {
	public static void sendDebugChat(String msg) {
		if (Vice.config.DEVMODE) {
			UChat.chat(Vice.devPrefix + msg.replaceAll("&&", "§"));
		}
	}
}
