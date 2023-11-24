package net.oxyopia.vice.utils;

import gg.essential.universal.UChat;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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

	public static void playSound(Identifier identifier, float pitch, float volume) {
		try {
			client.getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(identifier), pitch, volume));
		} catch (Exception err) {
			DevUtils.sendErrorMessage(err, "An error occurred attempting to play a sound");
		}
	}

	public static void sendVanillaTitle(String title, String subtitle, Float stayTime, Float fadeinout) {
		client.inGameHud.setSubtitle(Text.of(subtitle.replaceAll("&&", "§")));
		client.inGameHud.setTitle(Text.of(title.replaceAll("&&", "§")));
		client.inGameHud.setTitleTicks((int) (20 * fadeinout), (int) (20 * stayTime), (int) (20 * fadeinout));
	}
}
