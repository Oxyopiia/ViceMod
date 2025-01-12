package net.oxyopia.vice.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.oxyopia.vice.data.Debugger;
import net.oxyopia.vice.events.ChestRenderEvent;
import net.oxyopia.vice.events.ContainerRenderSlotEvent;
import net.oxyopia.vice.events.ItemDropEvent;
import net.oxyopia.vice.events.SlotClickEvent;
import net.oxyopia.vice.events.ViceEvent;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen<T extends ScreenHandler> extends MixinScreen {
	@Shadow @Final protected T handler;

	@Shadow private ItemStack touchDragStack;

	@Unique private int id = -1;

	@Inject(
		method = "render",
		at = @At("HEAD")
	)
	private void onRenderScreen(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			ItemStack cursorStack = touchDragStack.isEmpty() ? handler.getCursorStack() : touchDragStack;
			if (id == -1) {
				id = (int) (Math.random() * 25000);
				Debugger.HUD.debug("Assigned ID §b" + id +" §fto this HandledScreen", "SCREEN");
			}

			EVENT_MANAGER.publish(new ChestRenderEvent(title.getString(), handler.slots, cursorStack, id, context));
		}
	}

	@Inject(at = @At("HEAD"), method = "drawSlot")
	private void onSlotRender(DrawContext context, Slot slot, CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new ContainerRenderSlotEvent(slot, title.getString(), context));
		}
	}

	@Inject(at = @At("HEAD"), method = "close")
	private void onClose(CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			id = -1;
			Debugger.HUD.debug("Reset ID of closed HandledScreen", "SCREEN");
		}
	}

	@Redirect(
		method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;clickSlot(IIILnet/minecraft/screen/slot/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)V")
	)
	private void onAttemptMoveItem(ClientPlayerInteractionManager instance, int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			Debugger.SLOTCLICK.debug("§fFiring on syncId §d" + syncId + "§f to slotId §b" + slotId + "§f with button §c " + button + " §fwith type §e" + actionType + "§f with title §a" + title.getString());

			ViceEvent.Cancelable<Boolean> result = null;

			if (actionType == SlotActionType.THROW) {
				MinecraftClient client = MinecraftClient.getInstance();
				try {
					ItemStack item = client.player != null ? client.player.currentScreenHandler.getSlot(slotId).getStack() : null;

					if (item != null) {
						result = EVENT_MANAGER.publish(new ItemDropEvent(item));
					}

				} catch (Exception error) {
					LogUtils.getLogger().warn("An error occurred getting an ItemStack in a slot thrown event! slotId {} button {} syncId {} actionType {} title {}", slotId, button, syncId, actionType, title.getString(), error);
				}
			}

			if (result == null) {
				result = EVENT_MANAGER.publish(new SlotClickEvent(title.getString(), syncId, slotId, button, actionType));
			}

			if (result.isCanceled()) {
				Debugger.SLOTCLICK.debug("§cCANCEL");
				return;
			}

			Debugger.SLOTCLICK.debug("§ePASS");
		}

		instance.clickSlot(syncId, slotId, button, actionType, player);
	}
}

@Mixin(Screen.class)
class MixinScreen {
	@Shadow @Final protected Text title;
}