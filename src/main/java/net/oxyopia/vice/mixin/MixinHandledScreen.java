package net.oxyopia.vice.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.oxyopia.vice.events.ChestRenderEvent;
import net.oxyopia.vice.events.SlotClickEvent;
import net.oxyopia.vice.utils.DevUtils;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen<T extends ScreenHandler> extends MixinScreen {
	@Shadow @Final protected T handler;

	@Shadow private ItemStack touchDragStack;

	//	@ModifyArg(
//		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V"),
//		method = "drawMouseoverTooltip",
//		index = 1
//	)
//	private List<? extends Text> onDrawMouseoverTooltip(List<Text> text) {
//		if (Utils.INSTANCE.getInDoomTowers()) {
//			DrawHoverTooltipEvent result = EVENT_MANAGER.publish(new DrawHoverTooltipEvent(text));
//
//			if (result.hasReturnValue()) {
//				return result.getReturnValue();
//			}
//		}
//
//		return text;
//	}
	@Inject(
		method = "render",
		at = @At("HEAD")
	)
	private void onRenderScreen(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			ItemStack cursorStack = touchDragStack.isEmpty() ? handler.getCursorStack() : touchDragStack;
			ChestRenderEvent.Slots result = EVENT_MANAGER.publish(new ChestRenderEvent.Slots(title.getString(), handler.slots, cursorStack));
		}
	}

	@Redirect(
		method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickSlot(IIILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V")
	)
	private void onAttemptMoveItem(ClientPlayerInteractionManager instance, int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			DevUtils.sendDebugChat("&&fFiring SlotClickEvent on syncId &&d" + syncId + "&&f to slotId &&b" + slotId + "&&f with button &&c " + button + " &&fwith type &&e" + actionType + "&&f with title &&a" + title.getString(), "SLOT_CLICK_DEBUGGER");
			SlotClickEvent result = EVENT_MANAGER.publish(new SlotClickEvent(title.getString(), syncId, slotId, button, actionType));

			if (result.isCanceled()) {
				DevUtils.sendDebugChat("SlotClickEvent &&cCANCEL", "SLOT_CLICK_DEBUGGER");
				return;
			}

			DevUtils.sendDebugChat("SlotClickEvent &&ePASS", "SLOT_CLICK_DEBUGGER");
		}

		instance.clickSlot(syncId, slotId, button, actionType, player);
	}
}

@Mixin(Screen.class)
class MixinScreen {
	@Shadow @Final protected Text title;
}