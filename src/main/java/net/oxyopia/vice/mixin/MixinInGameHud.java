package net.oxyopia.vice.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.oxyopia.vice.config.HudEditor;
import net.oxyopia.vice.data.Debugger;
import net.oxyopia.vice.events.RenderHotbarSlotEvent;
import net.oxyopia.vice.events.HudRenderEvent;
import net.oxyopia.vice.events.SubtitleEvent;
import net.oxyopia.vice.events.TitleEvent;
import net.oxyopia.vice.utils.Utils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.*;

@Mixin(InGameHud.class)
public class MixinInGameHud {

	@Shadow private @Nullable Text title;

	@Inject(at = @At("HEAD"), method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V")
	private void onRenderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
		Utils.INSTANCE.setInDoomTowers(objective.getDisplayName().getString().contains("DoomTowers"));
	}

	@Inject(at = @At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawContext;drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;II)V"), method = "renderHotbarItem")
	private void onRenderHotbarItem(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new RenderHotbarSlotEvent(context, stack, x, y));
		}
	}
	
	@Inject(at = @At(value="INVOKE", target="Lnet/minecraft/client/gui/LayeredDrawer;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"), method = "render")
	private void hudRenderEvent(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			if (Utils.INSTANCE.getClient().currentScreen != HudEditor.INSTANCE) {
				EVENT_MANAGER.publish(new HudRenderEvent(context, context.getScaledWindowWidth(), context.getScaledWindowHeight()));
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "setSubtitle", cancellable = true)
	private void onSubtitle(Text subtitle, CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new SubtitleEvent(subtitle, title != null ? title : Text.empty(), ci));
			Debugger.HUD.debug(subtitle.getString(), "SUBTITLE");
		}
	}

	@Inject(at = @At("HEAD"), method = "setTitle")
	private void onTitle(Text title, CallbackInfo ci) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new TitleEvent(title.getString(), ci));
			Debugger.HUD.debug(title.getString(), "TITLE");
		}
	}
}
