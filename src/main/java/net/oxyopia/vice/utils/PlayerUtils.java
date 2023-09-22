package net.oxyopia.vice.utils;

import gg.essential.universal.UChat;
import net.oxyopia.vice.Vice;

import static net.oxyopia.vice.Vice.client;


public class PlayerUtils {
	public static String getWorld() {
		return client.world != null ? client.world.getRegistryKey().getValue().getPath() : null;
	}

	public static void sendViceMessage(String msg) {
		UChat.chat(Vice.chatPrefix + msg.replaceAll("&&", "§"));
	}
}
