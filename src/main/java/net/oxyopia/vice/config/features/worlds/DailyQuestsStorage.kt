package net.oxyopia.vice.config.features.worlds

import com.google.gson.annotations.Expose
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.oxyopia.vice.data.gui.Position

class DailyQuestsStorage {

    @Expose
    var questTrackerPos: Position = Position(175f, 150f)

    @Expose
    var quests: MutableList<ItemStack> = mutableListOf(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY)

    @Expose
    var progress: MutableList<Int> = mutableListOf(0, 0, 0)


}