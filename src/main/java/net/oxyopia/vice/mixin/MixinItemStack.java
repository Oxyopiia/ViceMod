package net.oxyopia.vice.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.oxyopia.vice.events.ItemRenameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.oxyopia.vice.Vice.EVENT_MANAGER;

@Mixin(ItemStack.class)
abstract class MixinItemStack {
	@Shadow public abstract ItemStack copy();

	@Redirect(
		at = @At(value="INVOKE", target = "Lnet/minecraft/text/Text$Serialization;fromJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"),
		method = "getName"
	)
	private MutableText getName(String json) {
		MutableText name = Text.Serialization.fromJson(json);

		if (name != null) {
			ItemStack newStack = this.copy();

			ItemRenameEvent result = EVENT_MANAGER.publish(new ItemRenameEvent(newStack, name));
			if (result.hasReturnValue()) {
				return result.getReturnValue();
			}
		}

		return name;
	}
}