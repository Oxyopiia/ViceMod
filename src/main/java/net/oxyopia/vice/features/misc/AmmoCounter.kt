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

object AmmoCounter {
    private val ammoRegex = Regex("\uD83D\uDD2B (\\d+)/30 \uD83D\uDD2B")

    @SubscribeEvent
    fun onActionBar(event: ActionBarEvent) {
        ammoRegex.find(event.content.string).apply {
            Vice.storage.misc.ammo = this?.groupValues?.get(1)?.toIntOrNull() ?: return

            Vice.storage.markDirty()
        }
    }

    @SubscribeEvent
    fun onRightClick(event: RightClickEvent) {
        if (!Vice.config.AMMO_RELOAD_TITLE) return

        val stack: ItemStack = MinecraftClient.getInstance().player?.mainHandStack ?: ItemStack.EMPTY
        val name = stack.cleanName()

        if (name == "AK-47" && Vice.storage.misc.ammo <= 0) {
            HudUtils.sendViceTitle("&&6Drop with Q to reload weapon!")
        }
    }

    @SubscribeEvent
    fun onRenderItemSlot(event: RenderHotbarSlotEvent) {
        if (!Vice.config.AMMO_COUNTER) return
        val matrices = event.context.matrices

        matrices.push()
        matrices.translate(9.0f, 9.0f, 200.0f)

        if (event.itemStack.cleanName() == "AK-47") {
            val ammo = Vice.storage.misc.ammo

            var display: String = ammo.toString()
            var color = Color.yellow

            val player = Utils.getPlayer() ?: return

            if (ammo <= 0) {
                if (player.inventory.main.find { itemStack -> itemStack.cleanName() == "AK-47 Magazine" }?.isEmpty == false) {
                    display = "Q"
                    color = Color.green
                } else {
                    display = "0"
                    color = Color.red
                }
            }

            HudUtils.drawText(display, event.x, event.y, event.context, color.rgb, shadow = true, centered = true)
        }

        matrices.pop()
    }
}