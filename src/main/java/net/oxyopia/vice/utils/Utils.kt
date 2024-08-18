package net.oxyopia.vice.utils

import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.client.world.ClientWorld
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.StringNbtReader
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.World

object Utils {
	var inDoomTowers = false
		get() = field || (Vice.config.DEVMODE && Vice.devConfig.BYPASS_INSTANCE_CHECK)

	private val client = MinecraftClient.getInstance()

	fun getClient(): MinecraftClient = MinecraftClient.getInstance()

	fun getPlayer(): ClientPlayerEntity? = client.player

	fun getWorld(): ClientWorld? = client.world

	fun getWorldString(): String? = client.world?.name()

	fun getDTWorld(): World? = World.getById(getWorldString().toString())

	fun net.minecraft.world.World.name(): String = registryKey.value.path

	fun playSound(identifier: Identifier, pitch: Float = 1f, volume: Float = 1f) {
		try {
			client.soundManager.play(PositionedSoundInstance.master(SoundEvent.of(identifier), pitch, volume))
		} catch (err: Exception) {
			DevUtils.sendErrorMessage(err, "An error occurred attempting to play a sound")
		}
	}

	fun playSound(string: String, pitch: Float = 1f, volume: Float = 1f) {
		playSound(Identifier("minecraft", string), pitch, volume)
	}

	fun playDing() {
		playSound("entity.arrow.hit_player")
	}

	fun playFail() {
		playSound("entity.arrow.hit_player", 0.2f)
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
	fun String.convertFormatting(): String {
		return this.replace("&&", "§")
	}

	/**
	 * Removes any two consecutive ampersands, or sections signs, which are followed by a character in a string
	 */
	fun String.removeFormatting(): String {
		return this.replace("(&&|§)[0-9a-fk-or]", "", ignoreCase = true)
	}
}
