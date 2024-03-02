package net.oxyopia.vice.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import net.oxyopia.vice.events.DrawHoverTooltipEvent;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(HandledScreen.class)
public class MixinHandledScreen {
	@ModifyArg(
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V"),
		method = "drawMouseoverTooltip",
		index = 1
	)
	private List<? extends Text> onDrawMouseoverTooltip(List<Text> text) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			DrawHoverTooltipEvent result = EVENT_MANAGER.publish(new DrawHoverTooltipEvent(text));

			if (result.hasReturnValue()) {
				return result.getReturnValue();
			}
		}

		return text;
	}
}