package net.oxyopia.vice.utils;

import gg.essential.universal.UChat;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.oxyopia.vice.Vice;

import java.util.Collection;

import static net.oxyopia.vice.Vice.*;

public class Utils {
	/**
	 * Use inDoomTowers() method instead, this is so dev mode bypassing is available
	 */
	public static boolean inDoomTowers = false;
	public static Collection<ScoreboardPlayerScore> scoreboardData;

	public static boolean inDoomTowers() {
		return inDoomTowers || (config.DEVMODE && devConfig.BYPASS_INSTANCE_CHECK);
	}

	public static String getWorld() {
		return client.world != null ? client.world.getRegistryKey().getValue().getPath() : null;
	}

	public static void sendViceMessage(String msg) {
		UChat.chat(Vice.chatPrefix + msg.replaceAll("&&", "§"));
	}
	public static void sendViceMessage(UTextComponent msg) {
		msg.setText(Vice.chatPrefix + msg.getString());
		UChat.chat(msg);
	}
}
