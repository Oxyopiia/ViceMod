package net.oxyopia.vice.mixin;

import gg.essential.lib.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.oxyopia.vice.features.itemabilities.ItemAbilityCooldown;
import net.oxyopia.vice.utils.DevUtils;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static net.oxyopia.vice.Vice.client;
import static net.oxyopia.vice.Vice.config;

@Mixin(InGameHud.class)
public class MixinInGameHud {

	@Shadow private int scaledWidth;
	@Shadow private int scaledHeight;

	@Inject(at = @At("HEAD"), method = "renderScoreboardSidebar")
	private void onRenderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
		if (objective.getDisplayName().getString().contains("DoomTowers")) {
			Utils.inDoomTowers = true;
			Utils.scoreboardData = objective.getScoreboard().getAllPlayerScores(objective);
		} else {
			Utils.inDoomTowers = false;
			if (Utils.scoreboardData != null) Utils.scoreboardData.clear();
		}
	}

	@Inject(at = @At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawContext;drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;II)V"), method = "renderHotbarItem")
	private void onRenderHotbarItem(DrawContext context, int x, int y, float f, PlayerEntity player, ItemStack stack, int seed, CallbackInfo ci) {
		ItemAbilityCooldown.Companion.onRenderItemSlot(client.textRenderer, stack, x, y);
	}
	
	@Inject(at = @At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/SubtitlesHud;render(Lnet/minecraft/client/gui/DrawContext;)V"), method = "render")
	private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
		int x = (this.scaledWidth) / 2;
		int y = (this.scaledHeight) / 2;

		ItemAbilityCooldown.Companion.onRenderInGameHud(x-1, y-1);
	}

	@Inject(at = @At("HEAD"), method = "setSubtitle", cancellable = true)
	private void onSubtitle(Text title, CallbackInfo ci) {
		DevUtils.sendDebugChat(title.getString(), "INGAMEHUD_MIXIN_DEBUGGER");
		if (Utils.inDoomTowers() && config.HIDE_REVOLVER_BLINDNESS && title.getString().contains("Left click to fire") && client.player != null) {
			if (client.player.hasStatusEffect(StatusEffects.BLINDNESS)) {
				client.player.removeStatusEffect(StatusEffects.BLINDNESS);
			} else if (client.player.hasStatusEffect(StatusEffects.DARKNESS)) {
				client.player.removeStatusEffect(StatusEffects.DARKNESS);
			}
		}

		ItemAbilityCooldown.Companion.onSubtitle(title, ci);
	}

	@Inject(at = @At("HEAD"), method = "render")
	private void drawLiveArenasUI(DrawContext context, float tickDelta, CallbackInfo ci) {
		if (config.LIVE_ARENA_TOGGLE && Objects.requireNonNull(Utils.getWorld()).contains("arenas")) {
			// add soon thx
		}
	}
}
