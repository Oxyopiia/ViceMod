package net.oxyopia.vice.utils

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.entry.RegistryEntry
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

	fun ItemStack.getAttributeModifier(modifier: RegistryEntry<EntityAttribute>, slot: AttributeModifierSlot = AttributeModifierSlot.MAINHAND): Double {
		val attributeModifiers = getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT).modifiers()

		for (entry in attributeModifiers) {
			if (entry.attribute() == modifier && entry.slot() == slot) {
				return entry.modifier().value()
			}
		}

		return -1.0
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

	fun ItemStack.isPlayerHeadWithArmor(): Boolean {
		return item == Items.PLAYER_HEAD && getAttributeModifier(EntityAttributes.GENERIC_ARMOR, AttributeModifierSlot.HEAD) >= 4.0
	}
}
