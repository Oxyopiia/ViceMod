package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.ActionBarEvent
import net.oxyopia.vice.events.RenderHotbarSlotEvent
import net.oxyopia.vice.events.RightClickEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.Utils
import java.awt.Color

object GunAmmos {

    val ammosRegex = Regex("\uD83D\uDD2B (\\d+)/30 \uD83D\uDD2B")

    @SubscribeEvent
    fun onActionBar(event: ActionBarEvent) {
        ammosRegex.find(event.content.string).apply {
            Vice.storage.misc.ammos = this?.groupValues?.get(1)?.toIntOrNull() ?: return

            Vice.storage.markDirty()
        }
    }

    @SubscribeEvent
    fun onRightClick(event: RightClickEvent) {
        val stack: ItemStack = MinecraftClient.getInstance().player?.mainHandStack ?: ItemStack.EMPTY
        val name = stack.cleanName()

        if(name == "AK-47" && Vice.storage.misc.ammos <= 0) HudUtils.sendViceTitle("&&6Drop with Q to reload weapon!")
    }

    @SubscribeEvent
    fun onRenderItemSlot(event: RenderHotbarSlotEvent) {

        val matrices = event.context.matrices

        matrices.push()
        matrices.translate(9.0f, 9.0f, 200.0f)

        if(event.itemStack.cleanName() == "AK-47") {
            var display: String
            val color: Color
            val ammos = Vice.storage.misc.ammos

            val player = Utils.getPlayer() ?: return

            if(ammos <= 0) {
                display = "Q"
                if (player.inventory.main.find { itemStack -> itemStack.cleanName() == "AK-47 Magazine" }?.isEmpty == false) {
                    color = Color.green
                } else {
                    display = "0"
                    color = Color.red
                }
            }
            else {
                display = ammos.toString()
                color = Color.yellow
            }
            HudUtils.drawText(display, event.x, event.y, event.context, color.rgb, shadow = true, centered = true)
        }

        matrices.pop()
    }
}