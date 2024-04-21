package net.oxyopia.vice.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.oxyopia.vice.events.BlockUpdateEvent;
import net.oxyopia.vice.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(WorldChunk.class)
public class MixinWorldChunk {
	@Inject(at = @At("HEAD"), method = "setBlockState")
	private void onBlockUpdate(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
		if (Utils.INSTANCE.getInDoomTowers()) {
			EVENT_MANAGER.publish(new BlockUpdateEvent(pos, state));
		}
	}
}