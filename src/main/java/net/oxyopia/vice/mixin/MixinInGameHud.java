package net.oxyopia.vice.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.oxyopia.vice.utils.DevUtils;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static net.oxyopia.vice.Vice.client;
import static net.oxyopia.vice.Vice.config;

@Mixin(InGameHud.class)
public class MixinInGameHud {

	@Inject(at = @At("HEAD"), method = "renderScoreboardSidebar")
	private void checkInDoomTowers(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
		if (objective.getDisplayName().getString().contains("DoomTowers")) {
			Utils.inDoomTowers = true;
			Utils.scoreboardData = objective.getScoreboard().getAllPlayerScores(objective);
		} else {
			Utils.inDoomTowers = false;
			if (Utils.scoreboardData != null) Utils.scoreboardData.clear();
		}
	}

	@Inject(at = @At("HEAD"), method = "setSubtitle")
	private void hideRevolverBlindness(Text title, CallbackInfo ci) {
		DevUtils.sendDebugChat(title.getString(), "INGAMEHUD_MIXIN_DEBUGGER");
		if (Utils.inDoomTowers() && config.HIDE_REVOLVER_BLINDNESS && title.getString().contains("Left click to fire") && client.player != null) {
			if (client.player.hasStatusEffect(StatusEffects.BLINDNESS)) {
				client.player.removeStatusEffect(StatusEffects.BLINDNESS);
			} else if (client.player.hasStatusEffect(StatusEffects.DARKNESS)) {
				client.player.removeStatusEffect(StatusEffects.DARKNESS);
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "render")
	private void drawLiveArenasUI(DrawContext context, float tickDelta, CallbackInfo ci) {
		if (config.LIVE_ARENA_TOGGLE && Objects.requireNonNull(Utils.getWorld()).contains("arenas")) {
			// add soon thx
		}
	}
}
