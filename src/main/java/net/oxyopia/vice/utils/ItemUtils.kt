package net.oxyopia.vice.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtElement
import net.minecraft.text.Text
import net.oxyopia.vice.data.Set
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

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

	private val validRods = listOf(
		"Basic Fishing Rod",
		"Reinforced Fishing Rod",
		"Frigid Fishing Rod",
		"Polar Rod",
		"Gilded Fishing Rod",
		"RGB Rod",
		"Wave Tamer",
	)
	// TODO: Convert to NBT detection.
	fun ItemStack.isRod(): Boolean = validRods.contains(cleanName())

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

	fun ItemStack.isPlayerHeadWithArmor(): Boolean {
		return item == Items.PLAYER_HEAD && getAttributeModifiers(EquipmentSlot.HEAD)[EntityAttributes.GENERIC_ARMOR].isNotEmpty()
	}
}
