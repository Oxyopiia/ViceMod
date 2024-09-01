package net.oxyopia.vice.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.VertexConsumerProvider;
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
	@Inject(
		method = "render",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderEntity(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V")
	)
	private void beforeEntities(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci, @Local MatrixStack matrices, @Local VertexConsumerProvider.Immediate vertexConsumers) {
		if (MinecraftClient.getInstance().isOnThread() && Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new WorldRenderEvent(matrices, tickCounter, camera, gameRenderer, vertexConsumers));
		}
	}
}