package net.oxyopia.vice.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
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

	/**
	 * Converts any two consecutive ampersands in a string to a section character, useful for formatting Minecraft Texts
	 */
	fun String.convertFormatting(): String {
		return this.replace("&&", "§")
	}
}
