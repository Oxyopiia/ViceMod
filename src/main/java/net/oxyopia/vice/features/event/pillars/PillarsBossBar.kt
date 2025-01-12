package net.oxyopia.vice.features.event.pillars

import net.minecraft.entity.boss.BossBar
import net.minecraft.util.math.Box
import net.oxyopia.vice.Vice
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
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object PillarsBossBar {
    private const val PILLARS_START_MESSAGE = "The Pillars are starting! Click [HERE] to enter."
    private const val PILLARS_MATCH_CANCELED = "Not enough players entered The Pillars! Matchmaking cancelled."

    private val PILLARS_HUB_BOUNDS = Box(-70.0, 41.0, 74.0, -20.0, 73.0, 47.0)
    private val PILLARS_TIMER = 2.minutes
    private val PILLARS_JOIN_TIME = 15.seconds
    private val PILLARS_FAILED_TIME = 30.seconds
    private val PILLARS_ITEMS_TIME = 5.seconds
    private val PILLARS_GAME_START = 3.seconds
    private val PILLARS_BORDERED = 90.seconds
    private val PILLARS_BORDER_ENDED = 60.seconds

    private var gameDuration = -1L
    private var failedDuration = -1L
    private var itemDuration = -1L
    private var gameStarted = false

    @SubscribeEvent
    fun onChatMessage(event: ChatEvent) {
        when {
            event.string.contains(PILLARS_MATCH_CANCELED) -> {
                gameDuration = -1
                failedDuration = System.currentTimeMillis()
            }

            event.string.contains(PILLARS_START_MESSAGE) -> {
                gameDuration = System.currentTimeMillis()
                gameStarted = false
            }
        }
    }

    @SubscribeEvent
    fun onBossbarAfter(event: BossBarEvents.Insert) {
        if(!World.Pillars.isInWorld() && !PILLARS_HUB_BOUNDS.isInBounds(World.Tower)) return
        val config = Vice.config
        if (!config.PILLARS_EVENT_TIMERS && !config.PILLARS_NEXT_ITEM_BOSSBAR) return

        val itemTimeElapsed = itemDuration.timeDelta()
        if (config.PILLARS_NEXT_ITEM_BOSSBAR && gameStarted) {
            when {
                World.Pillars.isInWorld() && itemTimeElapsed <= PILLARS_ITEMS_TIME -> {
                    val percentageComplete = (itemTimeElapsed / PILLARS_ITEMS_TIME).clamp(0.0, 1.0)
                    val formattedTime = (PILLARS_ITEMS_TIME - itemTimeElapsed).formatShortDuration()

                    val text = "Next Item in ${formattedTime}s".toText(Colors.ChatColor.Aqua, bold = true)

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

        if (!config.PILLARS_EVENT_TIMERS) return

        val timeElapsed = gameDuration.timeDelta()
        val failedTimeElapsed = failedDuration.timeDelta()

        when {
            failedTimeElapsed <= PILLARS_FAILED_TIME -> {
                val percentageComplete = (failedTimeElapsed / PILLARS_FAILED_TIME).clamp(0.0, 1.0)
                val formattedTime = (PILLARS_FAILED_TIME - failedTimeElapsed).formatShortDuration()

                val text = "Pillars ready to start in ${formattedTime}s".toText(Colors.ChatColor.LightPurple, bold = true)

                event.add(
                    text,
                    (1 - percentageComplete).toFloat(),
                    BossBar.Color.PURPLE,
                    BossBar.Style.NOTCHED_10
                )
            }

            timeElapsed <= PILLARS_JOIN_TIME -> {
                val percentageComplete = (timeElapsed / PILLARS_JOIN_TIME).clamp(0.0, 1.0)
                val formattedTime = (PILLARS_JOIN_TIME - timeElapsed).formatShortDuration()

                val text = "Pillars Starts in ${formattedTime}s".toText(Colors.ChatColor.Yellow, bold = true)

                event.add(
                    text,
                    (1 - percentageComplete).toFloat(),
                    BossBar.Color.YELLOW,
                    BossBar.Style.NOTCHED_10
                )
            }

            World.Pillars.isInWorld() && timeElapsed <= PILLARS_JOIN_TIME + PILLARS_GAME_START -> {
                val phaseTimeElapsed = timeElapsed - PILLARS_JOIN_TIME
                val percentageComplete = (phaseTimeElapsed / PILLARS_GAME_START).clamp(0.0, 1.0)
                val formattedTime = (PILLARS_GAME_START - phaseTimeElapsed).formatShortDuration()

                val text = "Game Starts in ${formattedTime}s".toText(Colors.ChatColor.Green, bold = true)

                event.add(
                    text,
                    (1 - percentageComplete).toFloat(),
                    BossBar.Color.GREEN,
                    BossBar.Style.NOTCHED_10
                )
            }

            World.Pillars.isInWorld() && timeElapsed <= PILLARS_JOIN_TIME + PILLARS_GAME_START + PILLARS_BORDERED -> {
                val phaseTimeElapsed = timeElapsed - (PILLARS_JOIN_TIME + PILLARS_GAME_START)
                val percentageComplete = (phaseTimeElapsed / PILLARS_BORDERED).clamp(0.0, 1.0)
                val formattedTime = (PILLARS_BORDERED - phaseTimeElapsed).formatDuration()

                val text = "Border starts to close in $formattedTime".toText(Colors.ChatColor.Red, bold = true)

                event.add(
                    text,
                    (1 - percentageComplete).toFloat(),
                    BossBar.Color.RED,
                    BossBar.Style.NOTCHED_10
                )

                if (!gameStarted) {
                    gameStarted = true
                    itemDuration = System.currentTimeMillis()
                }
            }

            World.Pillars.isInWorld() && timeElapsed <= PILLARS_JOIN_TIME + PILLARS_GAME_START + PILLARS_BORDERED + PILLARS_BORDER_ENDED -> {
                val phaseTimeElapsed = (timeElapsed - (PILLARS_JOIN_TIME + PILLARS_GAME_START + PILLARS_BORDERED)).coerceAtLeast(0.seconds)
                val percentageComplete = (phaseTimeElapsed / PILLARS_BORDER_ENDED).clamp(0.0, 1.0)
                val formattedTime = (PILLARS_BORDER_ENDED - phaseTimeElapsed).formatShortDuration()

                val text = "Border fully closes ${formattedTime}s".toText(Colors.ChatColor.Red, bold = true)

                event.add(
                    text,
                    (1 - percentageComplete).toFloat(),
                    BossBar.Color.RED,
                    BossBar.Style.NOTCHED_10
                )
            }

            else -> gameStarted = false
        }

        if (PILLARS_HUB_BOUNDS.isInBounds(World.Tower) && timeElapsed <= PILLARS_TIMER) {
			val percentageComplete = (timeElapsed / PILLARS_TIMER).clamp(0.0, 1.0)
			val formattedTime = (PILLARS_TIMER - timeElapsed).formatDuration()

			val text = "Pillars starts in $formattedTime".toText(Colors.ChatColor.DarkPurple, bold = true)

			event.add(
				text,
				(1 - percentageComplete).toFloat(),
				BossBar.Color.PURPLE,
				BossBar.Style.NOTCHED_10
			)
        }
    }
}