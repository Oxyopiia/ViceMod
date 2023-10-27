package net.oxyopia.vice.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;

import static net.oxyopia.vice.Vice.client;

public class ItemUtils {
	public static String getNameWithoutEnchants(ItemStack itemStack) {
		return getNameWithoutEnchants(itemStack.getName().getString());
	}

	public static String getNameWithoutEnchants(String string) {
		return string.replaceAll("\\s\\(.*\\)", "");
	}

	public static ItemStack getHeldItem() {
		return client.player != null ? client.player.getMainHandStack() : ItemStack.EMPTY;
	}

	/**
	 * @author Mojang
	 */
	public static List<String> getLore(ItemStack stack) {
		List<String> lore = new ArrayList<>();

		if (stack.hasNbt()) {
			if (stack.getNbt().contains(ItemStack.DISPLAY_KEY, NbtElement.COMPOUND_TYPE)) {
				NbtCompound nbtCompound = stack.getNbt().getCompound(ItemStack.DISPLAY_KEY);

				if (nbtCompound.getType(ItemStack.LORE_KEY) == NbtElement.LIST_TYPE) {
					NbtList nbtList = nbtCompound.getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE);

					for (int i = 0; i < nbtList.size(); ++i) {
						String lineLore = nbtList.getString(i);

						lore.add(lineLore);
					}
				}
			}
		}

		return lore;
	}
}
