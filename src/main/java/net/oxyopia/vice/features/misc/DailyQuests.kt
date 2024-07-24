package net.oxyopia.vice.features.expeditions

import com.sun.jna.platform.win32.DBT.DEV_BROADCAST_VOLUME
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.config.features.worlds.DailyQuestsStorage
import net.oxyopia.vice.config.features.worlds.OysterStorage
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.*
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.features.misc.OysterQuestTracker
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils.drawStrings
import net.oxyopia.vice.utils.ItemUtils
import net.oxyopia.vice.utils.ItemUtils.cleanName
import net.oxyopia.vice.utils.ItemUtils.getLore
import net.oxyopia.vice.utils.Utils
import kotlin.math.floor


object DailyQuests: HudElement("Daily Quests", Vice.storage.daily.questTrackerPos){

    private val daily: DailyQuestsStorage get() = Vice.storage.daily

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (MinecraftClient.getInstance().player == null) return

        val list: MutableList<String> = mutableListOf()

        val quest = daily.quests[0]
        val quest2 = daily.quests[1]
        val quest3 = daily.quests[2]

        if (quest.isEmpty) return
        if (quest2.isEmpty) return
        if (quest3.isEmpty) return

        var name = quest.cleanName()
        var name2 = quest2.cleanName()
        var name3 = quest3.cleanName()

        if (name == "Quest on Cooldown") name = quest.getLore()[0]
        if (name2 == "Quest on Cooldown") name2 = quest2.getLore()[0]
        if (name3 == "Quest on Cooldown") name3 = quest3.getLore()[0]

        val amountRegex = Regex("\\d+")

        list.add("&&eDaily Quests:")
        if (quest.item == Items.CLOCK) {
            //list.add("&&c$name")
        } else {
            list.add("${amountRegex.find(name)?.value?.toIntOrNull()?.let { checkColor(daily.progress[0], it) }}$name (${daily.progress[0]}/${amountRegex.find(name)?.value})")
        }
        if (quest2.item == Items.CLOCK) {
            //list.add("&&c$name2")
        } else {
            list.add("${amountRegex.find(name2)?.value?.toIntOrNull()?.let { checkColor(daily.progress[1], it) }}$name2 (${daily.progress[1]}/${amountRegex.find(name2)?.value})")
        }
        if (quest3.item == Items.CLOCK) {
            //list.add("&&c$name3")
        } else {
            list.add("${amountRegex.find(name3)?.value?.toIntOrNull()?.let { checkColor(daily.progress[2], it) }}$name3 (${daily.progress[2]}/${amountRegex.find(name3)?.value})")
        }

        DailyQuests.position.drawStrings(list, event.context)
    }

    fun checkColor(progress: Int, needed: Int): String {
        if (progress >= needed) return "&&a"
        return "&&e"
    }

    override fun storePosition(position: Position) {
        Vice.storage.daily.questTrackerPos = position
        Vice.storage.markDirty()
    }

    override fun shouldDraw(): Boolean = true

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
        val list = listOf(
            "&&eDaily Quests:",
            "&&equest",
            "&&equest",
            "&&equest",
        )

        return DailyQuests.position.drawStrings(list, context)
    }

    @SubscribeEvent
    fun onInventoryOpen(event: ChestRenderEvent) {
        if (!World.Tower.isInWorld()) return
        if (!event.chestName.contains("DAILY QUESTS")) return

        daily.quests[0] = event.slots[21].stack
        daily.quests[1] = event.slots[22].stack
        daily.quests[2] = event.slots[23].stack

        val amountRegex = Regex("\\d+/\\d+")

        val progress = amountRegex.find(event.slots[21].stack.getLore()[2])?.value.toString().split("/")[0].toInt()
        val progress2 = amountRegex.find(event.slots[22].stack.getLore()[2])?.value.toString().split("/")[0].toInt()
        val progress3 = amountRegex.find(event.slots[23].stack.getLore()[2])?.value.toString().split("/")[0].toInt()



        if (progress == 0) {
            daily.progress[0] = 0
        }
        if (progress2 == 0) {
            daily.progress[1] = 0
        }
        if (progress3 == 0) {
            daily.progress[2] = 0
        }

        Vice.storage.markDirty()
    }

    @SubscribeEvent
    fun onMobKill(event: EntityDeathEvent) {
        if (World.Showdown.isInWorld()) {
            if ((event.entity.customName?.contains(Text.of("Basic Thief")) == true)) {
                DevUtils.sendDebugChat("You Killed Basic Thief")
                for (q in daily.quests) {
                    if (q.cleanName().contains("Basic Thief")) {
                        daily.progress[daily.quests.indexOf(q)]++
                        Vice.storage.markDirty()
                    }
                }
            }

            if ((event.entity.customName?.contains(Text.of("Mineshaft Bandit")) == true)) {
                DevUtils.sendDebugChat("You Killed Mineshaft Bandit")
                for (q in daily.quests) {
                    if (q.cleanName().contains("Mineshaft Bandit")) {
                        daily.progress[daily.quests.indexOf(q)]++
                        Vice.storage.markDirty()
                    }
                }
            }
        }

        if (World.ArcadeVirtualWorld.isInWorld()) {
            if ((event.entity.customName?.contains(Text.of("Corrupted Ghoul")) == true)) {
                DevUtils.sendDebugChat("You Killed Corrupted Ghoul")
                for (q in daily.quests) {
                    if (q.cleanName().contains("Corrupted Ghoul")) {
                        daily.progress[daily.quests.indexOf(q)]++
                        Vice.storage.markDirty()
                    }
                }
            }
        }

        if (World.ArcticAssault.isInWorld()) {
            if ((event.entity.customName?.contains(Text.of("Polar Bear")) == true)) {
                DevUtils.sendDebugChat("You Killed Polar Bear")
                for (q in daily.quests) {
                    if (q.cleanName().contains("Polar Bear")) {
                        daily.progress[daily.quests.indexOf(q)]++
                        Vice.storage.markDirty()
                    }
                }
            }

            if ((event.entity.customName?.contains(Text.of("Ice Breacher")) == true)) {
                DevUtils.sendDebugChat("You Killed Ice Breacher")
                for (q in daily.quests) {
                    if (q.cleanName().contains("Ice Breacher")) {
                        daily.progress[daily.quests.indexOf(q)]++
                        Vice.storage.markDirty()
                    }
                }
            }
        }

        if (World.Space.isInWorld()) {
            if ((event.entity.customName?.contains(Text.of("Abducted Cow")) == true)) {
                DevUtils.sendDebugChat("You Killed Abducted Cow")
                for (q in daily.quests) {
                    if (q.cleanName().contains("Abducted Cow")) {
                        daily.progress[daily.quests.indexOf(q)]++
                        Vice.storage.markDirty()
                    }
                }
            }
        }

        if (World.Desert.isInWorld()) {
            if ((event.entity.customName?.contains(Text.of("Desert Dweller")) == true)) {
                DevUtils.sendDebugChat("You Killed Desert Dweller")
                for (q in daily.quests) {
                    if (q.cleanName().contains("Desert Dweller")) {
                        daily.progress[daily.quests.indexOf(q)]++
                        Vice.storage.markDirty()
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun onBlockUpdate(event: BlockUpdateEvent) {
        val original = Utils.getWorld()?.getBlockState(event.pos) ?: return
        if (World.Space.isInWorld()) {

            if (original.block == Blocks.WAXED_EXPOSED_COPPER && event.new.block == Blocks.BEDROCK) {
                DevUtils.sendDebugChat("You Mined Scrap Metal")
                for (q in daily.quests) {
                    if (q.cleanName().contains("Scrap Metal")) {
                        daily.progress[daily.quests.indexOf(q)]++
                        Vice.storage.markDirty()
                    }
                }
            }

            if (original.block == Blocks.AMETHYST_CLUSTER && event.new.block == Blocks.AIR) {
                DevUtils.sendDebugChat("You Mined Amethyst Crystal")
                for (q in daily.quests) {
                    if (q.cleanName().contains("Amethyst Crystal")) {
                        daily.progress[daily.quests.indexOf(q)]++
                        Vice.storage.markDirty()
                    }
                }
            }
        }
        if (World.Glimpse.isInWorld()) {

            if (original.block == Blocks.GRAY_WOOL && event.new.block == Blocks.STONE) {
                DevUtils.sendDebugChat("You Mined Carpet")
                for (q in daily.quests) {
                    if (q.cleanName().contains("Carpet")) {
                        daily.progress[daily.quests.indexOf(q)]++
                        Vice.storage.markDirty()
                    }
                }
            }

            if (original.block == Blocks.SEA_LANTERN && event.new.block == Blocks.PRISMARINE_BRICKS) {
                DevUtils.sendDebugChat("You Mined Office Light")
                for (q in daily.quests) {
                    if (q.cleanName().contains("Office Light")) {
                        daily.progress[daily.quests.indexOf(q)]++
                        Vice.storage.markDirty()
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun onSlotClick(event: SlotClickEvent) {
        if (!World.Tower.isInWorld() || !event.chestName.contains("DAILY QUESTS")) return
        //if (!merchantSlots.contains(event.slotId)) return


    }
}
