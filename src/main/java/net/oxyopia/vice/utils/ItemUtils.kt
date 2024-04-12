package net.oxyopia.vice.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text
import net.oxyopia.vice.data.Set
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

object ItemUtils {
	fun ItemStack.nameWithoutEnchants(): String {
		return name.string.nameWithoutEnchants()
	}

	fun String.nameWithoutEnchants(): String {
		return replace("\\s\\(.*\\)".toRegex(), "").replace(" §".toRegex(), "")
	}

	fun getHeldItem(): ItemStack = MinecraftClient.getInstance().player?.mainHandStack ?: ItemStack.EMPTY

	/**
	 * @author Mojang
	 */
	fun ItemStack.getLore(): List<String> {
		val lore: MutableList<String> = ArrayList()

		if (this.hasNbt()) {
			if (this.nbt!!.contains(ItemStack.DISPLAY_KEY, NbtElement.COMPOUND_TYPE.toInt())) {
				val nbtCompound = this.nbt!!.getCompound(ItemStack.DISPLAY_KEY)

				if (nbtCompound.getType(ItemStack.LORE_KEY) == NbtElement.LIST_TYPE) {
					val nbtList = nbtCompound.getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE.toInt())

					for (i in nbtList.indices) {
						val lineLore: Text? = Text.Serialization.fromJson(nbtList.getString(i))

						if (lineLore != null) lore.add(lineLore.string)
					}
				}
			}
		}

		return lore
	}

	fun ItemStack.isRod(): Boolean {

		val itemName = this.nameWithoutEnchants()

		return itemName.contains("Basic Fishing Rod") ||
				itemName.contains("Reinforced Fishing Rod") ||
				itemName.contains("Frigid Fishing Rod") ||
				itemName.contains("Polar Rod") ||
				itemName.contains("Gilded Fishing Rod") ||
				itemName.contains("RGB Rod")
	}

	fun ItemStack.isHook(): Boolean {

		val itemName = this.nameWithoutEnchants()

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
