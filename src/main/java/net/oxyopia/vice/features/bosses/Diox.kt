package net.oxyopia.vice.features.bosses

import gg.essential.elementa.svg.data.minus
import net.minecraft.entity.boss.BossBar
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.*
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.TimeUtils.formatTimer
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import kotlin.time.Duration.Companion.seconds

object Diox : Boss(
    World.Diox,
    Regex("(?:TRUE )?Diox\\s*-\\s*(\\d+(?:\\.\\d+)?/\\d+(?:\\.\\d+)?|∞)\\s*❤\\s*\\[PHASE\\s*(\\d+)]\$"),
    phaseTimesSec = listOf(5 * 60, 3 * 60, 5 * 60, 5 * 60, 85)
){
    private const val PHASE_1_MAX_TIME = 5 * 60

    private var portalCount = 0
    var bossPhase = 0
    var lastSpawnedDiox = -0L

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        val content = event.string
        if (World.Diox.isInWorld()) {

            when {
                content.contains("Diox: ITS NICE FOR US TO FINALLY MEET FACE TO FACE.") -> {
                    bossPhase = 0
                }
                content.contains("Diox: BUT SADLY, YOUR JOURNEY ENDS HERE.") -> {
                    bossPhase = 1
                    portalCount = 3
                    lastSpawnedDiox = System.currentTimeMillis()
                }
                content.contains("Diox: THE STRONGEST WEAPON IS ONE'S MIND.") -> {
                    bossPhase = 2
                }
                content.contains("Diox: YOUR DEMISE IS INEVITABLE.") -> {
                    bossPhase = 3
                }
                content.contains("Diox: SAY HELLO TO YOUR FRIEND, UNFORTUNATELY MINDLESS UNDER MY CONTROL.") -> {
                    bossPhase = 4
                }
            }
        }
    }

    @SubscribeEvent
    fun onSubtitle(event: SubtitleEvent) {
        if(event.subtitle.equals("TERMINATED")) {
            bossPhase = 0 // reset
        }
    }

    @SubscribeEvent
    fun onEntityDeath(event: EntityDeathEvent) {
        val entityName = event.entity.customName?.string ?: return
        if (World.Diox.isInWorld()) {

            if(entityName.contains("WatchTower")) {
                portalCount--
            }
        }
    }

    @SubscribeEvent
    fun onBossbarAfter(event: BossBarEvents.Insert) {
        if (!World.Diox.isInWorld()) return

        if(bossPhase == 1) {
            val diff = lastSpawnedDiox.timeDelta()

            val timer = diff.formatTimer(PHASE_1_MAX_TIME.seconds)

            if (portalCount != 0) {
                event.add(
                    Text.translatable("Diox - $portalCount/3 Portals ")
                        .formatted(Formatting.GREEN)
                        .formatted(Formatting.BOLD)
                        .append(Text.translatable("♥")
                            .formatted(Formatting.RESET)
                            .formatted(Formatting.GREEN))
                        .append(Text.translatable(" [PHASE 1]$timer")
                            .formatted(Formatting.GREEN)
                            .formatted(Formatting.BOLD)),
                    ((1.0 / 3) * portalCount).toFloat(),
                    BossBar.Color.GREEN,
                    BossBar.Style.NOTCHED_10
                )
            }
        }
    }
}