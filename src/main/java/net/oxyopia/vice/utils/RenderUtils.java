package net.oxyopia.vice.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static net.oxyopia.vice.Vice.client;

public class RenderUtils {
	public static void renderBox(MatrixStack stack, VertexConsumer buffer, double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
		Box aabb = new Box(x1, y1, z1, x2, y2, z2);
		WorldRenderer.drawBox(stack, buffer, aabb, (float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 100);

		DevUtils.sendDebugChat("renderBox x,y,z 1: " + x1 + " " + y1 + " " + z1 + " to " + x2 + " " + y2 + " " + z2, "GAME_RENDERER_DEBUGGER");
	}

	public static void draw3DLine(MatrixStack stack, float x1, float y1, float z1, float x2, float y2, float z2, Color color, float lineWidth) {
		Vec3d pos1 = new Vec3d(x1, y1, z1);
		Vec3d pos2 = new Vec3d(x2, y2, z2);

		draw3DLine(stack, pos1, pos2, color, lineWidth);
	}

	/**
	 * Draw a line between two points.
	 * Adapted from Skyblocker mod under the GNU LESSER GENERAL PUBLIC LICENSE
	 *
	 * does not work yet :(
	 *
	 * @author hysky
	 */
	public static void draw3DLine(MatrixStack stack, Vec3d point1, Vec3d point2, Color color, float lineWidth) {
		if (client.cameraEntity == null) return;

		Vec3d cameraPos = client.cameraEntity.getPos();

//		stack.push();
//		stack.translate(-cameraPos.x, cameraPos.y, -cameraPos.z);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		Matrix3f matrix3f = stack.peek().getNormalMatrix();
		Matrix4f matrix4f = stack.peek().getPositionMatrix();

		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

		RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		RenderSystem.lineWidth(lineWidth);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableCull();
		RenderSystem.enableDepthTest();

		buffer.begin(VertexFormat.DrawMode.LINE_STRIP, VertexFormats.LINES);

		Vec3d normal = point2.subtract(point1).normalize();
		buffer.vertex(matrix4f, (float) point1.x, (float) point1.y, (float) point1.z).color(color.getRGB()).normal(matrix3f, (float) normal.x, (float) normal.y, (float) normal.z).next();
		buffer.vertex(matrix4f, (float) point2.x, (float) point2.y, (float) point2.z).color(color.getRGB()).normal(matrix3f, (float) normal.x, (float) normal.y, (float) normal.z).next();

		tessellator.draw();
//		stack.pop();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		RenderSystem.lineWidth(1f);
		RenderSystem.enableCull();
	}


	public static void fillUIArea(MatrixStack stack, RenderLayer layer, int x1, int y1, int x2, int y2, int z, Color color) {
		fillUIArea(stack, layer, x1, y1, x2, y2, z, color.getRGB());
	}

	public static void fillUIArea(MatrixStack stack, RenderLayer layer, int x1, int y1, int x2, int y2, int z, int color) {
		int i;
		Matrix4f matrix4f = stack.peek().getPositionMatrix();
		if (x1 < x2) {
			i = x1;
			x1 = x2;
			x2 = i;
		}
		if (y1 < y2) {
			i = y1;
			y1 = y2;
			y2 = i;
		}
		float f = (float)ColorHelper.Argb.getAlpha(color) / 255.0f;
		float g = (float)ColorHelper.Argb.getRed(color) / 255.0f;
		float h = (float)ColorHelper.Argb.getGreen(color) / 255.0f;
		float j = (float)ColorHelper.Argb.getBlue(color) / 255.0f;

		VertexConsumerProvider.Immediate vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(layer);

		vertexConsumer.vertex(matrix4f, x1, y1, z).color(g, h, j, f).next();
		vertexConsumer.vertex(matrix4f, x1, y2, z).color(g, h, j, f).next();
		vertexConsumer.vertex(matrix4f, x2, y2, z).color(g, h, j, f).next();
		vertexConsumer.vertex(matrix4f, x2, y1, z).color(g, h, j, f).next();

		RenderSystem.disableDepthTest();
		vertexConsumers.draw(layer);
		RenderSystem.enableDepthTest();
	}

	public static int drawText(MatrixStack stack, TextRenderer textRenderer, @Nullable String text, int x, int y, int color, boolean shadow, boolean centered) {
		if (text == null) {
			return 0;
		}
		VertexConsumerProvider.Immediate vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();

		if (centered) {
			int width = textRenderer.getWidth(text) + (shadow ? 1 : 0);
			x -= (int) (double) (width / 2);
		}

		int i = textRenderer.draw(text, x, y, color, shadow, stack.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0, textRenderer.isRightToLeft());

		RenderSystem.disableDepthTest();
		vertexConsumers.draw();
		RenderSystem.enableDepthTest();
		return i;
	}
}
