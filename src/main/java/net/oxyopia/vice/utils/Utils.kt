package net.oxyopia.vice.utils

import com.mojang.brigadier.exceptions.CommandSyntaxException
import gg.essential.universal.UChat
import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.world.ClientWorld
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.StringNbtReader
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.oxyopia.vice.Vice
import net.oxyopia.vice.utils.enums.Set
import java.util.*
import java.util.regex.Pattern

import kotlin.math.floor

object Utils {
	var inDoomTowers = false
		get() = field || (Vice.config.DEVMODE && Vice.devConfig.BYPASS_INSTANCE_CHECK)

	private val client: MinecraftClient = MinecraftClient.getInstance()

	fun getWorld(): ClientWorld? = client.world

	fun getWorldString(): String? = client.world?.registryKey?.value?.path

	fun sendViceMessage(msg: String) {
		UChat.chat("${Vice.CHAT_PREFIX}${msg.replaceFormatting()}")
	}

	fun sendViceMessage(msg: UTextComponent) {
		msg.text = "${Vice.CHAT_PREFIX}${msg.text}"
		UChat.chat(msg)
	}

	fun playSound(identifier: Identifier, pitch: Float, volume: Float) {
		try {
			client.soundManager.play(PositionedSoundInstance.master(SoundEvent.of(identifier), pitch, volume))
		} catch (err: Exception) {
			DevUtils.sendErrorMessage(err, "An error occurred attempting to play a sound")
		}
	}

	fun ClientPlayerEntity.getEquippedSets(): Map<Set, Int> {
		val setsMap: MutableMap<Set, Int> = EnumMap(Set::class.java)
		val pattern = Pattern.compile("♦ Set: (.*)")

		armorItems?.forEach { itemStack ->
			val lore = ItemUtils.getLore(itemStack)

			lore.forEach { string ->
				val matcher = pattern.matcher(string)

				if (matcher.find()) {
					val set = Set.getByName(matcher.group(1))
					setsMap[set] = setsMap.getOrDefault(set, 0) + 1
				}
			}
		}
		return setsMap
	}

	/**
	 * Formats a duration as dd:hh:MM:SS
	 * @param ms Time in Milliseconds
	 */
	fun formatDuration(ms: Long, showMs: Boolean): String {
		val hours = floor(ms.toDouble() / (1000 * 60 * 60)).toLong()
		val mins = floor((ms / (1000 * 60)).toDouble() % 60).toLong()
		val secs = floor((ms / 1000).toDouble() % 60).toLong()
		val millis = ms % 1000

		return buildString {
			if (hours > 0) append(String.format("%02d:", hours))
			append(String.format("%02d:%02d", mins, secs))
			if (showMs) append(String.format(".%03d", millis))
		}
	}

	fun parseNbt(nbt: String): NbtCompound? {
		try {
			return StringNbtReader.parse(nbt)
		} catch (e: CommandSyntaxException) {
			DevUtils.sendErrorMessage(e, "An error occurred parsing an item's NBT!")
		}
		return null
	}

	/**
	 * Converts any two consecutive ampersands in a string to a section character, useful for formatting Minecraft Texts
	 */
	fun String.replaceFormatting(): String {
		return this.replace("&&", "§")
	}
}
