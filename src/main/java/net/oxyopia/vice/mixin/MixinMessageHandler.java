package net.oxyopia.vice.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import net.oxyopia.vice.events.ActionBarEvent;
import net.oxyopia.vice.events.ServerChatMessageEvent;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(MessageHandler.class)
public class MixinMessageHandler {
	@Shadow @Final private MinecraftClient client;

	@Inject(at = @At("HEAD"), method = "onGameMessage")
	private void onActionBar(Text message, boolean overlay, CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers() && overlay) {
			EVENT_MANAGER.publish(new ActionBarEvent(message));
		}
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V"), method = "onGameMessage")
	private void onGameMessageOverride(ChatHud instance, Text message) {
		Object result = EVENT_MANAGER.publish(new ServerChatMessageEvent(message));

		if (result instanceof Boolean && !((boolean) result)) {
			(this).client.inGameHud.getChatHud().addMessage(message);
		}
	}
}