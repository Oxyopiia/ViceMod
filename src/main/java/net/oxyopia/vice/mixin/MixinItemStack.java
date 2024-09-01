package net.oxyopia.vice.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.oxyopia.vice.events.ItemRenameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(ItemStack.class)
abstract class MixinItemStack {
	@Shadow public abstract ItemStack copy();

	@ModifyReturnValue(
		method = "getName",
		at = @At("RETURN")
	)
	private Text getName(Text original) {
		ItemRenameEvent result = EVENT_MANAGER.publish(new ItemRenameEvent(this.copy(), original.copy()));
		if (result.hasReturnValue()) {
			return result.getReturnValue();
		}

		return original;
	}
}