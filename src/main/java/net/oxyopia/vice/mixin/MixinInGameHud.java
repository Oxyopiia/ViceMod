package net.oxyopia.vice.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.text.Text;
import net.oxyopia.vice.utils.DevUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.client;
import static net.oxyopia.vice.Vice.config;

@Mixin(InGameHud.class)
public class MixinInGameHud {

	@Inject(at = @At("HEAD"), method = "setSubtitle")
	private void hideRevolverBlindness(Text title, CallbackInfo ci) {
		DevUtils.sendDebugChat(title.getString(), "INGAMEHUD_MIXIN_DEBUGGER");
		if (config.HIDE_REVOLVER_BLINDNESS && title.getString().contains("Left click to fire") && client.player != null) {
			if (client.player.hasStatusEffect(StatusEffects.BLINDNESS)) {
				client.player.removeStatusEffect(StatusEffects.BLINDNESS);
			} else if (client.player.hasStatusEffect(StatusEffects.DARKNESS)) {
				client.player.removeStatusEffect(StatusEffects.DARKNESS);
			}
		}
	}
}
