package net.oxyopia.vice.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.oxyopia.vice.events.WorldChangeEvent;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(DownloadingTerrainScreen.class)
public class MixinDownloadingTerrainScreen {
	@Inject(at = @At("HEAD"), method = "close")
	private void onWorldLoad(CallbackInfo ci) {
		MinecraftClient client = MinecraftClient.getInstance();

		if (client.isOnThread() && Utils.INSTANCE.getInDoomTowers() && client.world != null) {
			EVENT_MANAGER.publish(new WorldChangeEvent(client.world));
		}
	}
}