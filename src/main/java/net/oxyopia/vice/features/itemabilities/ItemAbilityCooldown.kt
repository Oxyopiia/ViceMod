package net.oxyopia.vice.features.itemabilities

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.util.ClickType
import net.minecraft.util.math.MathHelper
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.*
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.ItemUtils
import net.oxyopia.vice.utils.ItemUtils.nameWithoutEnchants
import net.oxyopia.vice.utils.Utils.clamp
import net.oxyopia.vice.utils.Utils.getEquippedSets
import java.awt.Color
import kotlin.math.ceil

/**
 * Inspired from SkyHanni's similar feature, partially adapted
 * under the GNU Lesser General Public License v2.1.
 *
 * @link https://github.com/hannibal002/SkyHanni/blob/beta/src/main/java/at/hannibal2/skyhanni/features/itemabilities/abilitycooldown/ItemAbilityCooldown.kt
 * @link https://github.com/hannibal002/SkyHanni/blob/beta/LICENSE
 * @author hannibal002
 */
object ItemAbilityCooldown {
	enum class DisplayType(val id: Int) {
		VANILLA(0),
		STATIC(1),
		COLORFADE(2),
		PERCENTAGE(3),
		TEXTONLY(4);
	}

	@SubscribeEvent
	fun onSound(event: SoundEvent) {

		when {

			// VIRTUASWORD
			event.soundName == "entity.zombie_villager.cure" && event.pitch == 2f && event.volume == 3f -> {
				ItemAbility.VIRTUASWORD.onSound()
			}

			// Barbed/Wasted Shotgun
			event.soundName == "entity.blaze.shoot" && event.pitch == 0.5f && event.volume == 9999f -> {
				ItemAbility.BARBED_SHOTGUN.onSound()
				ItemAbility.WASTED_SHOTGUN.onSound()
			}

			// Wasted Boomstick
			event.soundName == "entity.blaze.shoot" && event.pitch == 1.0f && event.volume == 9999f -> {
				ItemAbility.WASTED_BOOMSTICK.onSound()
			}

			// Crystalline Blade
			event.soundName == "block.amethyst_block.break" && event.pitch == 1.0f && event.volume == 9999f -> {
				ItemAbility.CRYSTALINE_BLADE.onSound()
			}

			// Bartender's Glove/Arctic Core
			event.soundName == "entity.snowball.throw" && event.pitch == 0.5f && event.volume == 9999f -> {
				ItemAbility.BARTENDER_GLOVE.onSound()
				ItemAbility.ARCTIC_CORE.onSound()
			}

			// Flesh Hatchet
			event.soundName == "item.trident.return" && event.pitch == 1.0f && event.volume == 9999f -> {
				ItemAbility.FLESH_HATCHET.onSound()
			}

			// Arctic Scroll
			(event.soundName == "block.lava.pop" || event.soundName == "entity.player.hurt_freeze") && event.pitch == 1f && event.volume == 9999f -> {
				ItemAbility.ARCTIC_SCROLL.onSound()
			}

			// 8-Bit Katana
			event.soundName == "entity.player.attack.crit" && event.volume == 9999f -> {
				ItemAbility.EIGHT_BIT_KATANA.onSound()
			}

			// Any Hook - calls all, should correctly identify based off enum value lastClicked
			event.soundName == "entity.fishing_bobber.retrieve" && event.pitch >= 0.25f && event.pitch <= 0.8f && event.volume == 1f -> {
				ItemAbility.ADVENTURER_HOOK.onSound()
				ItemAbility.SLIME_HOOK.onSound()
				ItemAbility.LUMINESCENT_HOOK.onSound()
				ItemAbility.GENHOOK.onSound()
			}

			// Bedrock Breaker
			event.soundName == "entity.bat.takeoff" && event.volume == 9999f -> {
				ItemAbility.BEDROCK_BREAKER.onSound()
			}

			// Doge Hammer
			event.soundName == "entity.wolf.ambient" && event.volume == 9999f -> {
				ItemAbility.DOGE_HAMMER.onSound()
			}

			// Revolver
			event.soundName == "entity.wither.spawn" && event.pitch == 1.25f && event.volume == 9999f -> {
				ItemAbility.REVOLVER.onSound()
			}

			// Dynamite Barrel
			event.soundName == "entity.generic.explode" && event.pitch == 1.5f && event.volume == 9999f -> {
				ItemAbility.DYNAMITE_BARREL.onSound()
			}

			// Laser Point Minigun
			event.soundName == "entity.generic.explode" && event.pitch == 2.0f && event.volume == 9999f -> {
				ItemAbility.LASER_POINT_MINIGUN.onSound()
			}

			// Snowball Cannon
			event.soundName == "entity.snowball.throw" && event.pitch == 1f && event.volume == 9999f -> {
				ItemAbility.SNOWBALL_CANNON.onSound()
			}
		}
	}

	@SubscribeEvent
	fun onRenderInGameHud(event: RenderInGameHudEvent) {
		if (!(Vice.config.ITEM_COOLDOWN_DISPLAY && Vice.config.SHOW_ITEMCD_TEXT_CROSSHAIR)) return

		val ability: ItemAbility? = ItemAbility.getByName(ItemUtils.getHeldItem().nameWithoutEnchants())

		ability?.apply {
			if (!isOnCooldown() || !displayCooldown) return

			val matrices = MatrixStack()

			var centered = false
			var xPos = (event.scaledWidth / 2 - 1) + 3
			var yPos = (event.scaledHeight / 2 - 1) - 1 + 3
			var color = Color(0, 236, 255, 255)

			if (Vice.config.DEVMODE) {
				xPos = (event.scaledWidth / 2 - 1) + Vice.devConfig.ITEMCD_CURSORCD_X_OFFSET
				yPos = (event.scaledHeight / 2 - 1) + Vice.devConfig.ITEMCD_CURSORCD_Y_OFFSET
				centered = Vice.devConfig.ITEMCD_CURSORCD_CENTER_TEXT
				color = Vice.devConfig.ITEMCD_CURSORCD_COLOR
			}

			matrices.push()
			matrices.translate(0.0f, 0.0f, 200.0f)

			val roundedFloat: String = String.format("%.0f", ceil(remainingCooldown().toDouble()))
			HudUtils.drawText(matrices,
				MinecraftClient.getInstance().textRenderer, roundedFloat, xPos, yPos, color.rgb, Vice.config.HUD_TEXT_SHADOW, centered)

			matrices.pop()
		}
	}

	// im putting functionality here just for future proofing because i will not be bothered if i wanted to add this in the future
	@Suppress("UNUSED_PARAMETER")
	@SubscribeEvent
	fun onLeftClick(event: LeftClickEvent) {
		if (!Vice.config.ITEM_COOLDOWN_DISPLAY) return

		val stack: ItemStack = MinecraftClient.getInstance().player?.mainHandStack ?: ItemStack.EMPTY
		val name = stack.nameWithoutEnchants()
		val ability: ItemAbility = ItemAbility.getByName(name, ClickType.LEFT) ?: return

		handleClickEventAbility(ability)
	}

	@Suppress("UNUSED_PARAMETER")
	@SubscribeEvent
	fun onRightClick(event: RightClickEvent) {
		if (!Vice.config.ITEM_COOLDOWN_DISPLAY) return

		val stack: ItemStack = MinecraftClient.getInstance().player?.mainHandStack ?: ItemStack.EMPTY
		val name = stack.nameWithoutEnchants()
		val ability: ItemAbility = ItemAbility.getByName(name, ClickType.RIGHT) ?: return

		handleClickEventAbility(ability)
	}

	private fun handleClickEventAbility(ability: ItemAbility) {
		ability.apply {
			lastClicked = System.currentTimeMillis()
			DevUtils.sendDebugChat("&&bITEMABILITY &&conLeftClick as&&b $name", "ITEM_ABILITY_DEBUGGER")

			if (soundOnUse || remainingCooldown() > 0f) return
			if (set == null || (MinecraftClient.getInstance().player?.getEquippedSets()?.getOrDefault(set, 0) ?: 0) >= setAmount) {
				activate()
			}
		}
	}

	@SubscribeEvent
	fun onSubtitle(event: SubtitleEvent) {
		// Known Bug: will clear other cooldowns (daily rewards/arenas)
		if (Vice.config.HIDE_ITEM_COOLDOWN_TITLES && event.subtitle.contains("Cooldown")) {
			MinecraftClient.getInstance().inGameHud.clearTitle()
			event.callbackInfo.cancel()
		}
	}

	private val redColor = Color(255, 0, 0).rgb
	private val greenColor = Color(0, 255, 0).rgb

	@SubscribeEvent
	fun onRenderItemSlot(event: RenderItemSlotEvent) {
		if (!Vice.config.ITEM_COOLDOWN_DISPLAY) return

		val ability: ItemAbility? = ItemAbility.getByName(event.itemStack.nameWithoutEnchants())

		// May refactor code in the future, a bit too much indentation and ambiguity for my liking
		ability?.apply {
			if (!displayCooldown) return

			val matrices = MatrixStack()
			val displayType = Vice.config.ITEMCD_DISPLAY_TYPE
			val bgOpacity = Vice.config.ITEMCD_BACKGROUND_OPACITY

			if (isOnCooldown()) {
				val timeRemaining = remainingCooldown()
				val progressLeft: Float = remainingCooldown() / cooldown

				when (displayType) {
					DisplayType.VANILLA.id -> {
						val topLeft = event.y + MathHelper.floor(16.0f * (1.0f - progressLeft))
						val bottomRight = topLeft + MathHelper.ceil(16.0f * progressLeft)

						HudUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), event.x, topLeft, event.x + 16, bottomRight, 0, Int.MAX_VALUE)
					}

					DisplayType.STATIC.id -> {
						HudUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), event.x, event.y, event.x + 16, event.y + 16, -500, Color(0.9f, 0f, 0f, bgOpacity))
					}

					DisplayType.COLORFADE.id -> {
						var redness: Float = (2.7f * progressLeft).clamp(0f, 1f)
						var greenness: Float = (1.3f - (1.3f * progressLeft)).clamp(0f, 1f)

						if (Vice.config.DEVMODE) {
							redness = (Vice.devConfig.ITEMCD_RED_FADE_OVERRIDE * progressLeft).clamp(0f, 1f)
							greenness = (Vice.devConfig.ITEMCD_GREEN_FADE_OVERRIDE - (Vice.devConfig.ITEMCD_GREEN_FADE_OVERRIDE * progressLeft)).clamp(0f, 1f)
						}

						HudUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), event.x, event.y, event.x + 16, event.y + 16, 0, Color(redness, greenness, 0f, bgOpacity))
					}

					DisplayType.PERCENTAGE.id -> {
						var col = Color(1f, 0f, 0f, bgOpacity)

						if (progressLeft > 0.075f && progressLeft <= 0.5f) col = Color(1f, 1f, 0f, bgOpacity)
						else if (progressLeft <= 0.075f) col = Color(0.4f, 1f, 0f, bgOpacity)

						HudUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), event.x, event.y, event.x + 16, event.y + 16, 0, col)
					}
				}

				// On Cooldown, Render Timer if Enabled (by Text Option or using Display Type TextOnly)
				if (Vice.config.SHOW_ITEMCD_TEXT || displayType == DisplayType.TEXTONLY.id) {
					val roundedFloat: String = String.format("%.0f", ceil(timeRemaining.toDouble()))

					matrices.push()
					matrices.translate(0.0f, 0.0f, 200.0f)
					HudUtils.drawText(matrices, event.textRenderer, roundedFloat, event.x, event.y + 9, shadow = true, centered = false)
					matrices.pop()
				}

			} else {
				// Render Green background if enabled
				if (!Vice.config.HIDE_ITEMCD_WHEN_READY && (displayType != DisplayType.VANILLA.id && displayType != DisplayType.TEXTONLY.id)) {
					HudUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), event.x, event.y, event.x + 16, event.y + 16, 0, Color(0f, 1f, 0f, bgOpacity))
				}

				// Render R in slot if enabled (by Text Option or using Display Type TextOnly)
				if (Vice.config.SHOW_ITEMCD_TEXT || displayType == DisplayType.TEXTONLY.id) {
					matrices.push()
					matrices.translate(0.0f, 0.0f, 200.0f)

					var color = Color(255, 255, 255, 255).rgb

					if (ability == ItemAbility.ARCTIC_SCROLL) {
						val hp = MinecraftClient.getInstance().player?.health ?: 0f
						color = if (hp <= 10f) greenColor else redColor
					}

					HudUtils.drawText(matrices, event.textRenderer, "R", event.x, event.y + 9, color, shadow = true, centered = false)
					matrices.pop()
				}
			}
		}
	}
}
