package net.oxyopia.vice.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.oxyopia.vice.events.WorldRenderEvent;
import net.oxyopia.vice.utils.Utils;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.*;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
	@Inject(at = @At(value = "INVOKE", target="Lnet/minecraft/client/render/DimensionEffects;isDarkened()Z"), method = "render")
	private void afterBlocks(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
		if (MinecraftClient.getInstance().isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new WorldRenderEvent.AfterBlocks(matrices, tickDelta, camera, gameRenderer, projectionMatrix));
		}
	}

	@Inject(at = @At("TAIL"), method = "render")
	private void afterEntities(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
		if (MinecraftClient.getInstance().isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new WorldRenderEvent.AfterEntities(matrices, tickDelta, camera, gameRenderer, projectionMatrix));
		}
	}
}