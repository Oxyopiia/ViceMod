package net.oxyopia.vice.features.misc

import com.mojang.brigadier.Command
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.screen.slot.SlotActionType
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.CommandRegisterEvent
import net.oxyopia.vice.events.SlotClickEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.ChatUtils
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.ItemUtils.isPlayerHeadWithArmor
import net.oxyopia.vice.utils.Utils
import org.lwjgl.glfw.GLFW

object ItemProtection {
	private val defaultProtectedItems = listOf(
		"Abyssal Core",
		"Anime Figure in a Jar",
		"Arctic Scroll",
		"Bartender's Glove",
		"Bit",
		"Bluesteel",
		"Cheese",
		"Cooked Worm Patty",
		"Corrupted Data",
		"Dark Scythe",
		"Drill of The Jungle",
		"Dynamite Barrel",
		"Encrypted Data",
		"Eternity Fish",
		"Gateway Fragment",
		"Grapplebow",
		"Heart of the Sea",
		"Lobotomy",
		"Mineral Cake",
		"Mystic Clam",
		"Mystic Kelp",
		"Overclocked Graphics Card",
		"Pufferfish Barrel",
		"Pufferfish Gauntlet",
		"Sheeper Fluid",
		"Sheeper Sticks",
		"Soul Shard",
		"Soul Shards",
		"Sourdough",
		"Spirit of the Tower",
		"Steel",
		"The MIXER",
		"The Phantasm",
		"The Synthflesh",
		"Tower Beacon",
		"Voidaxe",
		"Wave Pulser",
		"Worm Burger",
		"Zip Bomb",
		"Zephyr's Tablet Shard",
		Items.LEATHER_HORSE_ARMOR,
		Items.IRON_HORSE_ARMOR,
		Items.GOLDEN_HORSE_ARMOR,
		Items.DIAMOND_HORSE_ARMOR,
		Items.IRON_SWORD,
		Items.GOLDEN_SWORD,
		Items.DIAMOND_SWORD,
		Items.NETHERITE_SWORD,
		Items.GOLDEN_AXE,
		Items.NETHERITE_AXE,
		Items.BOW,
		Items.FISHING_ROD,
		Items.LEATHER_CHESTPLATE,
		Items.LEATHER_LEGGINGS,
		Items.LEATHER_BOOTS
	)

	@SubscribeEvent
	fun onItemMove(event: SlotClickEvent) {
		if (!Vice.config.TRASH_PROTECTION || !event.chestName.contains("Disposal") || ctrlPressed()) return
		if (event.slotId > 72 || event.slotId < 0) return // for some reason it sometimes breaks, so yeah, here you go

		val item = getItem(event) ?: return

		val isProtectedItem = defaultProtectedItems.contains(item.cleanName()) || defaultProtectedItems.contains(item.item)
		val isFavoriteItem = Vice.storage.misc.protectedItems.contains(item.cleanName())

		if (isProtectedItem || item.isPlayerHeadWithArmor() || isFavoriteItem) {
			val cause = when {
				isProtectedItem -> "DefaultProtectedItem"
				item.isPlayerHeadWithArmor() -> "ArmorValue"
				else -> "FavoriteItem"
			}

			ChatUtils.sendViceMessage("§cStopped you disposing that item! §7($cause)")
			event.cancel()
		}
	}

	@SubscribeEvent
	fun registerCommand(event: CommandRegisterEvent) {
		event.register(
			ClientCommandManager.literal("viceprotectitem")
				.executes {
					handleCommand()
					Command.SINGLE_SUCCESS
				}
		)
	}

	private fun handleCommand() {
		val favoriteItems = Vice.storage.misc.protectedItems
		val item = Utils.getPlayer()?.mainHandStack ?: return DevUtils.sendWarningMessage("You are not holding an item to protect!")
		val itemName = item.cleanName()

		if (item.isEmpty) return DevUtils.sendWarningMessage("You are not holding an item to protect!")
		if (defaultProtectedItems.contains(item.item) || defaultProtectedItems.contains(itemName)) {
			ChatUtils.sendViceMessage("§eThis item is protected by default!")
			ChatUtils.sendViceMessage("§7Hold LCONTROL while clicking to bypass this!")
			return
		}

		if (favoriteItems.remove(itemName)) {
			ChatUtils.sendViceMessage("§cYour $itemName §cwill no longer be protected!")
		} else {
			favoriteItems.add(itemName)
			ChatUtils.sendViceMessage("§aYour $itemName §ais now protected!")
		}

		Vice.storage.markDirty()
	}

	private fun getItem(event: SlotClickEvent): ItemStack? {
		return when (event.actionType) {
			SlotActionType.SWAP -> MinecraftClient.getInstance().player?.inventory?.getStack(event.button)
			else -> MinecraftClient.getInstance().player?.currentScreenHandler?.getSlot(event.slotId)?.stack
		}
	}

	private fun ctrlPressed() = InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle, GLFW.GLFW_KEY_LEFT_CONTROL)
}