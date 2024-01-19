package net.oxyopia.vice.mixin;

import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.text.Text;
import net.oxyopia.vice.events.ModifyBossBarEvent;
import net.oxyopia.vice.utils.DevUtils;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(BossBarHud.class)
public class MixinBossBarHud {
	@Unique private String lastReportedTitle = "";

	@Redirect(
		at = @At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/ClientBossBar;getName()Lnet/minecraft/text/Text;"),
		method = "render"
	)
	private Text getBossbarName(ClientBossBar instance) {
		if (!Utils.INSTANCE.getInDoomTowers()) return instance.getName();

		if (!Objects.equals(lastReportedTitle, instance.getName().getString())) {
			DevUtils.sendDebugChat("&&5BOSSBAR &&rUpdated to: " + instance.getName().getString(), "BOSSBAR_DEBUGGER");
			lastReportedTitle = instance.getName().getString();
		}

		ModifyBossBarEvent result = EVENT_MANAGER.publish(new ModifyBossBarEvent(instance, instance.getName()));

		if (result.hasReturnValue()) {
			return result.getReturnValue();
		}

		return instance.getName();
	}
}