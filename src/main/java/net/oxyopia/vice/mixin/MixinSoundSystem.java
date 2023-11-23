package net.oxyopia.vice.mixin;

import gg.essential.lib.mixinextras.sugar.Local;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import net.oxyopia.vice.features.itemabilities.AbilitySoundHider;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(SoundSystem.class)
public abstract class MixinSoundSystem {
	@Shadow protected abstract float getAdjustedVolume(float volume, SoundCategory category);

	@Redirect(
		at = @At(value="INVOKE", target="Lnet/minecraft/client/sound/SoundSystem;getAdjustedVolume(FLnet/minecraft/sound/SoundCategory;)F"),
		method="play(Lnet/minecraft/client/sound/SoundInstance;)V"
	)
	private float getAdjustedVolume(SoundSystem instance, float volume, SoundCategory category, @Local(ordinal=0) SoundInstance sound2) {
		if (!Utils.inDoomTowers()) return this.getAdjustedVolume(volume, category);

		float multiplier = AbilitySoundHider.INSTANCE.onSound(sound2);
		return (multiplier == -1f ? this.getAdjustedVolume(volume, category) : multiplier);
	}
}
