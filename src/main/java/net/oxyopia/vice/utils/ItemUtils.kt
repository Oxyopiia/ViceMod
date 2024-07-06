package net.oxyopia.vice.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import net.oxyopia.vice.data.Set
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors

object ItemUtils {
	fun ItemStack.cleanName(): String {
		return name.string.cleanName()
	}

	fun String.cleanName(): String {
		return replace("\\s\\(.*\\)".toRegex(), "").replace(" §".toRegex(), "")
	}

	fun getHeldItem(): ItemStack = MinecraftClient.getInstance().player?.mainHandStack ?: ItemStack.EMPTY

	/**
	 * @author Mojang
	 */
	fun ItemStack.getLore(): List<String> {
		val lines = components.get(DataComponentTypes.LORE)?.lines ?: return ArrayList()

		return lines.map { it.string }
	}

	fun ItemStack.getNbtString(): String {
		val stream = components.stream().map { it.toString() }
		return "{" + stream.collect(Collectors.joining(", ")) + "}"
	}


	fun ItemStack.isRod(): Boolean {

		val itemName = this.cleanName()

		return itemName.contains("Basic Fishing Rod") ||
				itemName.contains("Reinforced Fishing Rod") ||
				itemName.contains("Frigid Fishing Rod") ||
				itemName.contains("Polar Rod") ||
				itemName.contains("Gilded Fishing Rod") ||
				itemName.contains("RGB Rod")
	}

	fun ItemStack.isHook(): Boolean {

		val itemName = this.cleanName()

		return itemName.contains("Adventurer's Hook") ||
				itemName.contains("Slime Hook") ||
				itemName.contains("Luminescent Hook") ||
				itemName.contains("Genhook")
	}

	fun ClientPlayerEntity.getEquippedSets(): Map<Set, Int> {
		val setsMap: MutableMap<Set, Int> = EnumMap(Set::class.java)
		val pattern = Pattern.compile("♦ Set: (.*)")

		armorItems?.forEach { itemStack ->
			val lore = itemStack.getLore()

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
}
