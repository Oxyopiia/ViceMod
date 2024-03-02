package net.oxyopia.vice.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import gg.essential.api.EssentialAPI;
import gg.essential.universal.wrappers.message.UTextComponent;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.oxyopia.vice.utils.DevUtils;
import net.oxyopia.vice.utils.ItemUtils;
import net.oxyopia.vice.utils.Utils;

public class DevDataCommand {
	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		dispatcher.register(ClientCommandManager.literal("vicedevdata")
			.executes(context -> {
				EssentialAPI.getNotifications().push("Vice", "This is a notification test", 4f, () -> {
					Utils.INSTANCE.sendViceMessage("Notification clicked");
					return null;
				});

				Utils.INSTANCE.sendViceMessage("inDoomTowers: &&a" + Utils.INSTANCE.getInDoomTowers());

				ItemStack heldItem = ItemUtils.INSTANCE.getHeldItem();

				if (heldItem.getNbt() != null) {
					Utils.INSTANCE.sendViceMessage(new UTextComponent("§eClick to copy your held item's NBT.§r")
						.setClick(ClickEvent.Action.COPY_TO_CLIPBOARD, heldItem.getNbt().asString())
						.setHover(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackContent(heldItem)));
				}

				return Command.SINGLE_SUCCESS;
			})
		);
	}
}