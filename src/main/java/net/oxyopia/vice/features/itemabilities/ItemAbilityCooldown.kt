package net.oxyopia.vice.features.itemabilities

import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket
import net.minecraft.text.Text
import net.minecraft.util.ClickType
import net.minecraft.util.math.MathHelper
import net.oxyopia.vice.Vice
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.ItemUtils
import net.oxyopia.vice.utils.RenderUtils
import net.oxyopia.vice.utils.Utils
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

/**
 * Inspired from SkyHanni's similar feature, partially adapted
 * under the GNU Lesser General Public License v2.1.
 *
 * @link https://github.com/hannibal002/SkyHanni/blob/beta/src/main/java/at/hannibal2/skyhanni/features/itemabilities/abilitycooldown/ItemAbilityCooldown.kt
 * @link https://github.com/hannibal002/SkyHanni/blob/beta/LICENSE
 * @author hannibal002
 */
class ItemAbilityCooldown {
	enum class DisplayType(val id: Int) {
		VANILLA(0),
		STATIC(1),
		COLORFADE(2),
		PERCENTAGE(3),
		TEXTONLY(4);
	}

	companion object {
		fun onSound(sound: PlaySoundS2CPacket) {
			when {

				// Barbed Shotgun
				sound.soundName() == "entity.blaze.shoot" && sound.pitch == 0.5f && sound.volume == 9999f -> {
					ItemAbility.BARBED_SHOTGUN.onSound()
				}

				// Arctic Core
				sound.soundName() == "entity.snowball.throw" && sound.pitch == 0.5f && sound.volume == 9999f -> {
					ItemAbility.ARCTIC_CORE.onSound()
				}

				// Arctic Scroll
				(sound.soundName() == "block.lava.pop" || sound.soundName() == "entity.player.hurt_freeze") && sound.pitch == 1f && sound.volume == 9999f -> {
					ItemAbility.ARCTIC_SCROLL.onSound()
				}

				// 8-Bit Katana
				sound.soundName() == "entity.player.attack.crit" && sound.volume == 9999f -> {
					ItemAbility.EIGHT_BIT_KATANA.onSound()
				}

				// Any Hook - calls all, should correctly identify based off enum value lastClicked
				sound.soundName() == "entity.fishing_bobber.retrieve" && sound.pitch >= 0.25f && sound.pitch <= 0.8f && sound.volume == 1f -> {
					ItemAbility.SLIME_HOOK.onSound()
					ItemAbility.LUMINESCENT_HOOK.onSound()
					ItemAbility.GENHOOK.onSound()
				}

				// Bedrock Breaker
				sound.soundName() == "entity.bat.takeoff" && sound.volume == 9999f -> {
					ItemAbility.BEDROCK_BREAKER.onSound()
				}

				// Doge Hammer
				sound.soundName() == "entity.wolf.ambient" && sound.volume == 9999f -> {
					ItemAbility.DOGE_HAMMER.onSound()
				}

				// Revolver
				sound.soundName() == "entity.wither.spawn" && sound.pitch == 1.25f && sound.volume == 9999f -> {
					ItemAbility.REVOLVER.onSound()
				}

				// Dynamite Barrel
				sound.soundName() == "entity.generic.explode" && sound.pitch == 1.5f && sound.volume == 9999f -> {
					ItemAbility.DYNAMITE_BARREL.onSound()
				}

				// Laser Point Minigun
				sound.soundName() == "entity.generic.explode" && sound.pitch == 2.0f && sound.volume == 9999f -> {
					ItemAbility.LASER_POINT_MINIGUN.onSound()
				}

				// Snowball Cannon
				sound.soundName() == "entity.snowball.throw" && sound.pitch == 1f && sound.volume == 9999f -> {
					ItemAbility.SNOWBALL_CANNON.onSound()
				}
			}
		}

		// im putting functionality here just for future proofing because i will not be bothered if i wanted to add this in the future
		fun onLeftClick(stack: ItemStack) {
			if (Utils.inDoomTowers() && Vice.config.ITEM_COOLDOWN_DISPLAY) {
				val name = ItemUtils.getNameWithoutEnchants(stack)
				val ability: ItemAbility? = ItemAbility.getByName(name, ClickType.LEFT)

				ability?.let {
					it.lastClicked = System.currentTimeMillis()
					DevUtils.sendDebugChat("&&bITEMABILITY &&conLeftClick as&&b " + ability.name, "ITEM_ABILITY_DEBUGGER")

					if (!it.soundOnUse && it.remainingCooldown() == 0f) {
						it.activate()
					}
				}
			}
		}

		fun onRightClick(stack: ItemStack) {
			if (Utils.inDoomTowers() && Vice.config.ITEM_COOLDOWN_DISPLAY) {
				val ability: ItemAbility? = ItemAbility.getByName(ItemUtils.getNameWithoutEnchants(stack), ClickType.RIGHT)

				ability?.let {
					it.lastClicked = System.currentTimeMillis()
					DevUtils.sendDebugChat("&&bITEMABILITY &&donRightClick as&&b " + ability.name, "ITEM_ABILITY_DEBUGGER")

					if (!it.soundOnUse && it.remainingCooldown() == 0f) {
						it.activate()
					}
				}
			}
		}

		fun onSubtitle(title: Text, ci: CallbackInfo) {
			if (Utils.inDoomTowers()) {
				if (title.string.toString().contains("100%")) {
					ItemAbility.GALACTIC_HAND_CANNON.lastClicked = System.currentTimeMillis()
				}

				if (Vice.config.HIDE_ITEM_COOLDOWN_TITLES && title.string.toString().contains("Cooldown")) {
					Vice.client.inGameHud.clearTitle()
					ci.cancel()
				}
			}
		}

		fun onRenderItemSlot(textRenderer: TextRenderer, stack: ItemStack, x: Int, y: Int) {
			if(!Utils.inDoomTowers() || !Vice.config.ITEM_COOLDOWN_DISPLAY) return

			val ability: ItemAbility? = ItemAbility.getByName(ItemUtils.getNameWithoutEnchants(stack))

			ability?.apply {
				if (!cooldownDisplay) return

				val matrices = MatrixStack()
				val displayType = Vice.config.ITEMCD_DISPLAY_TYPE
				val bgOpacity = Vice.config.ITEMCD_BACKGROUND_OPACITY

				if (this.isOnCooldown()) {
					val timeRemaining = remainingCooldown()
					val progressLeft: Float = remainingCooldown() / cooldown

					when {
						displayType == DisplayType.VANILLA.id -> {
							val k = y + MathHelper.floor(16.0f * (1.0f - progressLeft))
							val l = k + MathHelper.ceil(16.0f * progressLeft)
							RenderUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), x, k, x + 16, l, 0, Int.MAX_VALUE)
						}

						displayType == DisplayType.STATIC.id -> {
							RenderUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), x, y, x + 16, y + 16, -500, Color(0.9f, 0f, 0f, bgOpacity))
						}

						displayType == DisplayType.COLORFADE.id -> {
							var redness: Float = max(0f, min(1f, 2.7f * progressLeft))
							var greenness: Float = max(0f, min(1f, 1.3f - (1.3f * progressLeft)))

							if (Vice.config.DEVMODE) {
								redness = max(0f, min(1f, Vice.devConfig.ITEMCD_RED_FADE_OVERRIDE * progressLeft))
								greenness = max(0f, min(1f, Vice.devConfig.ITEMCD_GREEN_FADE_OVERRIDE - (Vice.devConfig.ITEMCD_GREEN_FADE_OVERRIDE * progressLeft)))
							}

							RenderUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), x, y, x + 16, y + 16, 0, Color(redness, greenness, 0f, bgOpacity))
						}

						displayType == DisplayType.PERCENTAGE.id -> {
							var col = Color(1f, 0f, 0f, bgOpacity)

							if (progressLeft > 0.075f && progressLeft <= 0.5f) col = Color(1f, 1f, 0f, bgOpacity)
							else if (progressLeft <= 0.075f) col = Color(0.4f, 1f, 0f, bgOpacity)

							RenderUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), x, y, x + 16, y + 16, 0, col)
						}
					}

					// On Cooldown, Render Timer if Enabled (by Text Option or using Display Type TextOnly)
					if (Vice.config.SHOW_ITEMCD_TEXT || displayType == DisplayType.TEXTONLY.id) {
						val roundedFloat: String = String.format("%.0f", ceil(timeRemaining.toDouble()))

						matrices.push()
						matrices.translate(0.0f, 0.0f, 200.0f)
						RenderUtils.drawText(matrices, textRenderer, roundedFloat, x, y + 9, 0xFFFFFF, true, false)
						matrices.pop()
					}

				} else {
					// Render Green background if enabled
					if (!Vice.config.HIDE_ITEMCD_WHEN_READY && (displayType != DisplayType.VANILLA.id && displayType != DisplayType.TEXTONLY.id)) {
						RenderUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), x, y, x + 16, y + 16, 0, Color(0f, 1f, 0f, bgOpacity))
					}

					// Render R if enabled (by Text Option or using Display Type TextOnly)
					if (Vice.config.SHOW_ITEMCD_TEXT || displayType == DisplayType.TEXTONLY.id) {
						matrices.push()
						matrices.translate(0.0f, 0.0f, 200.0f)
						RenderUtils.drawText(matrices, textRenderer, "R", x, y + 9, 0xFFFFFF, true, false)
						matrices.pop()
					}
				}
			}
		}

		fun onRenderInGameHud(x: Int, y: Int) {
			if(!Utils.inDoomTowers() || !(Vice.config.ITEM_COOLDOWN_DISPLAY && Vice.config.SHOW_ITEMCD_TEXT_CROSSHAIR)) return

			val ability: ItemAbility? = ItemAbility.getByName(ItemUtils.getNameWithoutEnchants(ItemUtils.getHeldItem()))

			ability?.apply {
				if(!isOnCooldown() || !cooldownDisplay) return

				val matrices = MatrixStack()

				var centered = false
				var xPos = x + 3
				var yPos = y + 3
				var color = Color(0f, 1f, 1f, 1f)

				if (Vice.config.DEVMODE) {
					xPos = x + Vice.devConfig.ITEMCD_CURSORCD_X_OFFSET
					yPos = y + Vice.devConfig.ITEMCD_CURSORCD_Y_OFFSET
					centered = Vice.devConfig.ITEMCD_CURSORCD_CENTER_TEXT
					color = Vice.devConfig.ITEMCD_CURSORCD_COLOR
				}

				matrices.push()
				matrices.translate(0.0f, 0.0f, 200.0f)

				val roundedFloat: String = String.format("%.0f", ceil(remainingCooldown().toDouble()))
				RenderUtils.drawText(matrices, Vice.client.textRenderer, roundedFloat, xPos, yPos, color.rgb, Vice.config.HUD_TEXT_SHADOW, centered)

				matrices.pop()
			}
		}
	}
}

private fun PlaySoundS2CPacket.soundName(): String = this.sound.value().id.path
