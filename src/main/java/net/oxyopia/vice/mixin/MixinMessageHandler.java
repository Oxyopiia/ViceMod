package net.oxyopia.vice.mixin;

import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import net.oxyopia.vice.events.ActionBarEvent;
import net.oxyopia.vice.events.ServerChatMessageEvent;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(MessageHandler.class)
public class MixinMessageHandler {
	@Inject(at = @At("HEAD"), method = "onGameMessage")
	private void onGameMessage(Text message, boolean overlay, CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			if (!overlay) EVENT_MANAGER.publish(new ServerChatMessageEvent(message));
			else EVENT_MANAGER.publish(new ActionBarEvent(message));
		}
	}
}