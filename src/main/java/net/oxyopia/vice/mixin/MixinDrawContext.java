package net.oxyopia.vice.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.regex.Pattern;

import static net.oxyopia.vice.Vice.config;

@Mixin(DrawContext.class)
public class MixinDrawContext {

	@ModifyVariable(
		argsOnly = true,
		method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
		at = @At(value="INVOKE", target="Lnet/minecraft/client/util/math/MatrixStack;push()V"),
		ordinal = 0
	)
	private String towerBeaconFeatures(String countOverride, TextRenderer textRenderer, ItemStack stack, int x, int y) {
		Screen currentScreen = MinecraftClient.getInstance().currentScreen;

		if (Utils.INSTANCE.getInDoomTowers() && config.BETTER_WARP_MENU && currentScreen != null && currentScreen.getTitle().contains(Text.of("Warp List"))) {
			String itemName = stack.getName().getString();
			Pattern pattern = Pattern.compile("Floor \\d");

			if (pattern.matcher(itemName).find()) return itemName.substring(itemName.length() - 1);
		}

		return countOverride;
	}
}
