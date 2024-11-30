package net.oxyopia.vice.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import net.oxyopia.vice.data.Debugger;
import net.oxyopia.vice.events.BossBarEvents;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Debug(export=true)
@Mixin(BossBarHud.class)
public abstract class MixinBossBarHud {
	@Shadow protected abstract void renderBossBar(DrawContext context, int x, int y, BossBar bossBar);

	@Shadow @Final private MinecraftClient client;
	@Unique private String lastReportedTitle = "";

	@Redirect(
		at = @At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/ClientBossBar;getName()Lnet/minecraft/text/Text;"),
		method = "render"
	)
	private Text getBossbarName(ClientBossBar instance) {
		if (!Utils.INSTANCE.getInDoomTowers()) return instance.getName();

		if (!Objects.equals(lastReportedTitle, instance.getName().getString())) {
			Debugger.BOSSBAR.debug("Updated to: " + instance.getName().getString());
			lastReportedTitle = instance.getName().getString();
		}

		EVENT_MANAGER.publish(new BossBarEvents.Read(instance, instance.getName()));
		BossBarEvents.Override result = EVENT_MANAGER.publish(new BossBarEvents.Override(instance, instance.getName()));

		if (result.hasReturnValue()) {
			return result.getReturnValue();
		}

		return instance.getName();
	}

	@Redirect(
		at = @At(value="INVOKE", target="Ljava/util/Map;isEmpty()Z"),
		method = "render"
	)
	private boolean cancelChecks(Map<UUID, ClientBossBar> instance) {
		if (!Utils.INSTANCE.getInDoomTowers()) return instance.isEmpty();
		return false;
	}

	@Inject(
		at = @At(value="INVOKE", target="Ljava/util/Collection;iterator()Ljava/util/Iterator;"),
		method = "render"
	)
	private void addBossbarEntries(DrawContext context, CallbackInfo ci, @Local(ordinal = 1) LocalIntRef jRef) {
		if (!Utils.INSTANCE.getInDoomTowers()) return;

		int i = context.getScaledWindowWidth();
		int j = jRef.get();

		BossBarEvents.Insert result = EVENT_MANAGER.publish(new BossBarEvents.Insert());
		if (result.getReturnValue() == null || result.getReturnValue().isEmpty()) return;

		for (ClientBossBar clientBossBar : result.getReturnValue().values()) {
			int k = i / 2 - 91;
			int l = j;
			renderBossBar(context, k, l, clientBossBar);
			Text text = clientBossBar.getName();
			int m = client.textRenderer.getWidth(text);
			int n = i / 2 - m / 2;
			int o = l - 9;
			context.drawTextWithShadow(client.textRenderer, text, n, o, 0xFFFFFF);
			if ((j += 10 + client.textRenderer.fontHeight) < context.getScaledWindowHeight() / 3) continue;
			break;
		}

		jRef.set(j);
	}
}