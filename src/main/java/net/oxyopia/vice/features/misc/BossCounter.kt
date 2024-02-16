package net.oxyopia.vice.features.misc

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.entity.EntityType
import net.minecraft.text.Text
import net.oxyopia.vice.Vice
import net.oxyopia.vice.config.features.MiscStorage
import net.oxyopia.vice.data.World
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.*
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.drawStrings


object BossCounter: HudElement("Boss Counter", Vice.storage.misc.bossCounterPos){

    private val viceTimeRegex = Regex("You ran out of time to defeat Vice\\. \\(\\d+m\\)")
    private val gelatoTimeRegex = Regex("You ran out of time to defeat \"Corrupted Vice\" Phase 3\\. \\(\\d+m\\)")
    private val pppTimeRegex = Regex("You ran out of time to defeat Percentage Player Percentage. \\(\\d+m\\)")
    private val minehutTimeRegex = Regex("You ran out of time to defeat \\?\\?\\?\\. \\(\\d+m\\)")
    private val shadowTimeRegex = Regex("You ran out of time to defeat Phase 3 of Shadow Gelato. \\(\\d+m\\)")

    private val abyssalWonRegex = Regex("Abyssal Vice: No\\.\\. This- This Can't Be\\?!")

    private var lastBossKilled: Text = Text.of("")

    private val misc: MiscStorage get() = Vice.storage.misc

    private val viceBoss get() = misc.viceBoss
    private val abyssalViceBoss get() = misc.abyssalViceBoss
    private val wastelandBoss get() = misc.wastelandBoss
    private val gelatoBoss get() = misc.gelatoBoss
    private val pppBoss get() = misc.pppBoss
    private val minehutBoss get() = misc.minehutBoss
    private val shadowGelatoBoss get() = misc.shadowGelatoBoss

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        val content = event.string

        if (World.Vice.isInWorld()) {
            if (content.contains(viceTimeRegex)) misc.viceBoss--
            Vice.storage.markDirty()
        }

        if (World.Gelato.isInWorld()) {
            if (content.contains(gelatoTimeRegex)) misc.gelatoBoss--
            Vice.storage.markDirty()
        }

        if(World.PPP.isInWorld()) {
            if (content.contains(pppTimeRegex)) {
                if(lastBossKilled.contains(Text.of("Monster"))) misc.pppBoss--
                Vice.storage.markDirty()
            }
        }

        if (World.Minehut.isInWorld()) {
            if (content.contains(minehutTimeRegex)) misc.minehutBoss--
            Vice.storage.markDirty()
        }

        if(World.DarkVice.isInWorld()) {
            if(content.contains(abyssalWonRegex)) misc.abyssalViceBoss++
            Vice.storage.markDirty()
        }

        if(World.ShadowGelato.isInWorld()) {
            if (content.contains(shadowTimeRegex)) misc.shadowGelatoBoss--
            Vice.storage.markDirty()
        }
    }

    @SubscribeEvent
    fun onBossKill(event: EntityDeathEvent) {
        if(World.Vice.isInWorld()) {
            lastBossKilled = event.entity.customName!!
            if (event.entity.type == EntityType.WITHER_SKELETON && event.entity.customName?.contains(Text.of("Vice")) == true) misc.viceBoss++
            Vice.storage.markDirty()
        }

        if(World.Gelato.isInWorld()) {
            lastBossKilled = event.entity.customName!!
            if (event.entity.type == EntityType.WITHER_SKELETON && event.entity.customName?.contains(Text.of("True Gelato")) == true) misc.gelatoBoss++
            Vice.storage.markDirty()
        }

        if(World.PPP.isInWorld()) {
            lastBossKilled = event.entity.customName!!
            if (event.entity.type == EntityType.WITHER_SKELETON && event.entity.customName?.contains(Text.of("Monster")) == true) misc.pppBoss++
            Vice.storage.markDirty()
        }

        if(World.Minehut.isInWorld()) {
            lastBossKilled = event.entity.customName!!
            if (event.entity.type == EntityType.WITHER_SKELETON && event.entity.customName?.contains(Text.of("TheOwner")) == true) misc.minehutBoss++
            Vice.storage.markDirty()
        }

        if(World.ShadowGelato.isInWorld()) {
            lastBossKilled = event.entity.customName!!
            if (event.entity.type == EntityType.WITHER_SKELETON && event.entity.customName?.contains(Text.of("True Shadow Gelato")) == true) misc.shadowGelatoBoss++
            Vice.storage.markDirty()
        }
    }

    @SubscribeEvent
    fun onSound(event: SoundEvent) {
        if(World.Wasteyard.isInWorld()) {
            if (event.soundName == "ui.toast.challenge_complete" && event.pitch == 1f && event.volume == 9999f) misc.wastelandBoss++
            Vice.storage.markDirty()
        }
    }

    @SubscribeEvent
    fun onHudRender(event: HudRenderEvent) {
        if (MinecraftClient.getInstance().player == null) return

        if(!Vice.config.BOSS_COUNTER) return

        val list: MutableList<String> = mutableListOf()

        list.add("&&5Vice: $viceBoss")
        list.add("&&4Wasteland: $wastelandBoss")
        list.add("&&0Abyssal Vice: $abyssalViceBoss")
        list.add("&&aEl Gelato: $gelatoBoss")
        list.add("&&cPPP: $pppBoss")
        list.add("&&bLifesteal Box SMP: $minehutBoss")
        list.add("&&5Shadow Gelato: $shadowGelatoBoss")

        position.drawStrings(list, event.context)
    }

    override fun storePosition(position: Position) {
        Vice.storage.misc.bossCounterPos = position
        Vice.storage.markDirty()
    }

    override fun shouldDraw(): Boolean = Vice.config.BOSS_COUNTER

    override fun Position.drawPreview(context: DrawContext): Pair<Float, Float> {
        val list = listOf(
            "&&5Vice: 8",
            "&&4Wasteland: 20",
            "&&0Abyssal Vice: 1",
            "&&aEl Gelato: 15",
            "&&cPPP: 100",
            "&&bLifesteal Box SMP: 5",
            "&&5Shadow Gelato: 0"
        )

        return position.drawStrings(list, context)
    }
}
