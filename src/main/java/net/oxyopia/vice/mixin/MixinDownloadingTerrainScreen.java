package net.oxyopia.vice.mixin;

import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.oxyopia.vice.features.arenas.ArenasHelperFunctions;
import net.oxyopia.vice.utils.Utils;
import net.oxyopia.vice.utils.enums.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.oxyopia.vice.Vice.client;

@Mixin(DownloadingTerrainScreen.class)
public class MixinDownloadingTerrainScreen {
	@Inject(at = @At("HEAD"), method = "close")
	private void onWorldLoad(CallbackInfo ci) {
		String worldId = client.world != null ? client.world.getRegistryKey().getValue().getPath() : "";

		if (client.isOnThread() && Utils.inDoomTowers() && World.Companion.getById(worldId) != null) {
			ArenasHelperFunctions.INSTANCE.parseWorldChange(client.world);
		}
	}
}