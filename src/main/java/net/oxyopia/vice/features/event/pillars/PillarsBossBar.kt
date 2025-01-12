package net.oxyopia.vice.features.event.pillars

import net.minecraft.entity.boss.BossBar
import net.minecraft.util.math.Box
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.World
import net.oxyopia.vice.events.BossBarEvents
import net.oxyopia.vice.events.ChatEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.HudUtils.toText
import net.oxyopia.vice.utils.LocationUtils.isInBounds
import net.oxyopia.vice.utils.NumberUtils.clamp
import net.oxyopia.vice.utils.TimeUtils.formatDuration
import net.oxyopia.vice.utils.TimeUtils.formatShortDuration
import net.oxyopia.vice.utils.TimeUtils.timeDelta
import net.oxyopia.vice.utils.Utils.concatenate
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object PillarsBossBar {

    private var gameDuration = -1L
    private var failedDuration = -1L
    private var itemDuration = -1L

    private val PILLARS_HUB_BOUNDS = Box(-70.0, 41.0, 74.0, -20.0, 73.0, 47.0)
    private val PILLARS_TIMER = 2.minutes
    private val PILLARS_JOIN_TIME = 15.seconds
    private val PILLARS_FAILED_TIME = 30.seconds
    private val PILLARS_ITEMS_TIME = 5.seconds
    private val PILLARS_GAME_START = 3.seconds
    private val PILLARS_BORDERED = 90.seconds
    private val PILLARS_BORDER_ENDED = 60.seconds

    private var gameStarted = false

    private const val PILLARS_START_MESSAGE = "The Pillars are starting! Click [HERE] to enter."
    private const val PILLARS_MATCH_CANCELED = "Not enough players entered The Pillars! Matchmaking cancelled."

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        val content = event.string

        if(content.contains(PILLARS_MATCH_CANCELED)) {
            gameDuration = -1
            failedDuration = System.currentTimeMillis()
        }

        if(content.contains(PILLARS_START_MESSAGE)) {
            gameDuration = System.currentTimeMillis()
            gameStarted = false
        }
    }

    @SubscribeEvent
    fun onBossbarAfter(event: BossBarEvents.Insert) {
        if(!World.Pillar.isInWorld()) {
            if (!PILLARS_HUB_BOUNDS.isInBounds(World.Tower)) return
        }

        val timeElapsed = gameDuration.timeDelta()
        val itemTimeElapsed = itemDuration.timeDelta()
        val failedTimeElapsed = failedDuration.timeDelta()

        when {
            failedTimeElapsed <= PILLARS_FAILED_TIME -> {
                val percentageComplete = (failedTimeElapsed / PILLARS_FAILED_TIME).clamp(0.0, 1.0)
                val formattedTime = (PILLARS_FAILED_TIME - failedTimeElapsed).formatShortDuration()

                val text = listOf(
                    "Pillars Rematch on Cooldown in ${formattedTime}s".toText(Colors.ChatColor.LightPurple, bold = true)
                ).concatenate()

                event.add(
                    text,
                    (1 - percentageComplete).toFloat(),
                    BossBar.Color.PINK,
                    BossBar.Style.NOTCHED_10
                )
            }

            timeElapsed <= PILLARS_JOIN_TIME -> {
                val percentageComplete = (timeElapsed / PILLARS_JOIN_TIME).clamp(0.0, 1.0)
                val formattedTime = (PILLARS_JOIN_TIME - timeElapsed).formatShortDuration()

                val text = listOf(
                    "Pillars Starts in ${formattedTime}s".toText(Colors.ChatColor.Yellow, bold = true)
                ).concatenate()

                event.add(
                    text,
                    (1 - percentageComplete).toFloat(),
                    BossBar.Color.YELLOW,
                    BossBar.Style.NOTCHED_10
                )
            }

            World.Pillar.isInWorld() && timeElapsed <= (PILLARS_JOIN_TIME + PILLARS_GAME_START) -> {
                val phaseTimeElapsed = timeElapsed - PILLARS_JOIN_TIME
                val percentageComplete = (phaseTimeElapsed / PILLARS_GAME_START).clamp(0.0, 1.0)
                val formattedTime = (PILLARS_GAME_START - phaseTimeElapsed).formatShortDuration()

                val text = listOf(
                    "Game Starts in ${formattedTime}s".toText(Colors.ChatColor.Green, bold = true)
                ).concatenate()

                event.add(
                    text,
                    (1 - percentageComplete).toFloat(),
                    BossBar.Color.GREEN,
                    BossBar.Style.NOTCHED_10
                )
            }

            World.Pillar.isInWorld() && timeElapsed <= (PILLARS_JOIN_TIME + PILLARS_GAME_START + PILLARS_BORDERED) -> {
                val phaseTimeElapsed = timeElapsed - (PILLARS_JOIN_TIME + PILLARS_GAME_START)
                val percentageComplete = (phaseTimeElapsed / PILLARS_BORDERED).clamp(0.0, 1.0)
                val formattedTime = (PILLARS_BORDERED - phaseTimeElapsed).formatDuration()

                val text = listOf(
                    "Pillars Borders in $formattedTime".toText(Colors.ChatColor.Red, bold = true)
                ).concatenate()

                event.add(
                    text,
                    (1 - percentageComplete).toFloat(),
                    BossBar.Color.RED,
                    BossBar.Style.NOTCHED_10
                )

                if(!gameStarted) {
                    gameStarted = true
                    itemDuration = System.currentTimeMillis()
                }
            }

            World.Pillar.isInWorld() && timeElapsed <= (PILLARS_JOIN_TIME + PILLARS_GAME_START + PILLARS_BORDERED + PILLARS_BORDER_ENDED) -> {
                val phaseTimeElapsed = (timeElapsed - (PILLARS_JOIN_TIME + PILLARS_GAME_START + PILLARS_BORDERED)).coerceAtLeast(0.seconds)
                val percentageComplete = (phaseTimeElapsed / PILLARS_BORDER_ENDED).clamp(0.0, 1.0)
                val formattedTime = (PILLARS_BORDER_ENDED - phaseTimeElapsed).formatShortDuration()

                val text = listOf(
                    "Pillars Ends in ${formattedTime}s".toText(Colors.ChatColor.Red, bold = true)
                ).concatenate()

                event.add(
                    text,
                    (1 - percentageComplete).toFloat(),
                    BossBar.Color.RED,
                    BossBar.Style.NOTCHED_10
                )
            }


            else -> gameStarted = false
        }

        if(gameStarted) {
            when {
                World.Pillar.isInWorld() && itemTimeElapsed <= PILLARS_ITEMS_TIME -> {
                    val percentageComplete = (itemTimeElapsed / PILLARS_ITEMS_TIME).clamp(0.0, 1.0)
                    val formattedTime = (PILLARS_ITEMS_TIME - itemTimeElapsed).formatShortDuration()

                    val text = listOf(
                        "Pillars Next Item in ${formattedTime}s".toText(Colors.ChatColor.Aqua, bold = true)
                    ).concatenate()

                    event.add(
                        text,
                        (1 - percentageComplete).toFloat(),
                        BossBar.Color.BLUE,
                        BossBar.Style.NOTCHED_10
                    )
                }

                else -> itemDuration = System.currentTimeMillis()
            }
        }

        if (PILLARS_HUB_BOUNDS.isInBounds(World.Tower) && timeElapsed <= PILLARS_TIMER) {
            val percentageComplete = (timeElapsed / PILLARS_TIMER).clamp(0.0, 1.0)
            val formattedTime = (PILLARS_TIMER - timeElapsed).formatDuration()

            val text = listOf(
                "Pillars on Cooldown of $formattedTime".toText(Colors.ChatColor.DarkPurple, bold = true)
            ).concatenate()

            event.add(
                text,
                (1 - percentageComplete).toFloat(),
                BossBar.Color.PURPLE,
                BossBar.Style.NOTCHED_10
            )
        }
    }
}