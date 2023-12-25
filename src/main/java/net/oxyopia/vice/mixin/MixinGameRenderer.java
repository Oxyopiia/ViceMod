package net.oxyopia.vice.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.oxyopia.vice.events.WorldRenderEvent;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.*;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {

	@Inject(at = @At("HEAD"), method = "renderWorld")
	private void renderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci) {
		if (client.isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new WorldRenderEvent(tickDelta, limitTime, matrices));
		}
	}
}