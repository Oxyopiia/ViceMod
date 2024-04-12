package net.oxyopia.vice.features.itemabilities

import gg.essential.elementa.utils.withAlpha
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ClickType
import net.minecraft.util.math.MathHelper
import net.oxyopia.vice.Vice
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.*
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.HudUtils
import net.oxyopia.vice.utils.HudUtils.drawString
import net.oxyopia.vice.utils.ItemUtils
import net.oxyopia.vice.utils.ItemUtils.nameWithoutEnchants
import net.oxyopia.vice.utils.ItemUtils.getEquippedSets
import net.oxyopia.vice.utils.Utils
import net.oxyopia.vice.utils.NumberUtils.clamp
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
	private class DisplayType {
		companion object {
			const val VANILLA = 0
			const val STATIC = 1
			const val COLOR_FADE = 2
			const val PERCENTAGE = 3
			const val TEXT_ONLY = 4
		}
	}

	@SubscribeEvent
	fun onSound(event: SoundEvent) {

		when {

			// Zip Bomb
			event.soundName == "block.note_block.bit" && event.pitch == 2f && event.volume == 9999f -> {
				ItemAbility.ZIP_BOMB.onSound()
			}

			// Warped Grenade
			event.soundName == "entity.generic.explode" && event.pitch == 1f && event.volume == 9999f -> {
				ItemAbility.WARPED_GRENADE.onSound()
			}

			// Poseidon's Fury
			event.soundName == "item.trident.throw" && event.pitch == 2f && event.volume == 9999f -> {
				ItemAbility.POSEIDONS_FURY.onSound()
			}

			// Glitch Mallet
			event.soundName == "block.respawn_anchor.deplete" && event.pitch == 1.0f && event.volume == 1f -> {
				ItemAbility.GLITCH_MALLET.onSound()
			}

			// VIRTUASWORD
			event.soundName == "block.note_block.pling" && event.pitch == 0.5f && event.volume == 9999f -> {
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

			// Jynx's Chain Gun
			event.soundName == "entity.guardian.attack" && event.pitch == 1.25f && !ItemAbility.JYNX_CHAIN_GUN.isOnCooldown() -> {
				ItemAbility.JYNX_CHAIN_GUN.onSound()
			}

			// The Synthflesh
			event.soundName == "entity.player.burp" && (event.pitch == 0.5f || event.pitch == 1f) && event.volume == 9999f -> {
				ItemAbility.THE_SYNTHFLESH.onSound()
			}

			// Shadow Gelato's Drum Gun
			event.soundName == "entity.illusioner.cast_spell" && event.volume == 9999f -> {
				ItemAbility.SHADOW_GELATO_DRUM_GUN.onSound()
			}

			event.soundName == "item.trident.riptide_1" && event.volume == 3f -> {
				ItemAbility.ASSASSIN_BLADE.onSound()
			}

			event.soundName == "block.note_block.pling" && event.pitch == 1f && event.volume == 3f -> {
				ItemAbility.C4.onSound()
			}

			event.soundName == "entity.shulker.shoot" && event.volume == 9999f -> {
				ItemAbility.CURSED_SKULL.onSound()
			}

			event.soundName == "entity.generic.eat" && event.volume == 3f -> {
				ItemAbility.GLOW_BERRIES.onSound()
			}
		}
	}

	@SubscribeEvent
	fun onRenderInGameHud(event: HudRenderEvent) {
		if (!Vice.config.ITEM_COOLDOWN_DISPLAY || !Vice.config.SHOW_ITEMCD_TEXT_CROSSHAIR) return

		val ability: ItemAbility? = ItemAbility.getByName(ItemUtils.getHeldItem().nameWithoutEnchants())

		ability?.apply {
			if (!isOnCooldown() || !displayCooldown) return

			val pos = Position(event.scaledWidth / 2f + 2, event.scaledHeight / 2f + 2, centered = false)
			val color = Color(0, 236, 255, 255)

			val roundedFloat: String = String.format("%.0f", ceil(remainingCooldown().toDouble()))
			pos.drawString(roundedFloat, event.context, defaultColor = color)
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
	fun onRenderItemSlot(event: RenderItemSlotEvent) {
		val ability: ItemAbility? = ItemAbility.getByName(event.itemStack.nameWithoutEnchants())

		val bgOpacity = Vice.config.ITEMCD_BACKGROUND_OPACITY
		val matrices = event.context.matrices

		ability?.apply {
			if(Vice.config.WRONG_SET_INDICATOR && setAmount > 0) {
				if ((Utils.getPlayer()?.getEquippedSets()?.getOrDefault(set, 0) ?: 0) < setAmount) {
//					TODO("Create Position.fillUIArea()")
					HudUtils.fillUIArea(
						matrices,
						RenderLayer.getGuiOverlay(),
						event.x,
						event.y,
						event.x + 16,
						event.y + 16,
						-500,
						Color(255, 90, 15).withAlpha(bgOpacity)
					)
					drawStatus(event.x, event.y + 9, event.context)
					return
				}
			}

			if (!Vice.config.ITEM_COOLDOWN_DISPLAY || !displayCooldown) return
			val displayType = Vice.config.ITEMCD_DISPLAY_TYPE

			if (isOnCooldown()) {
				val progressLeft: Float = remainingCooldown() / cooldown

				when (displayType) {
					DisplayType.VANILLA -> {
						val topLeft = event.y + MathHelper.floor(16.0f * (1.0f - progressLeft))
						val bottomRight = topLeft + MathHelper.ceil(16.0f * progressLeft)

						HudUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), event.x, topLeft, event.x + 16, bottomRight, 0, Int.MAX_VALUE)
					}

					DisplayType.STATIC -> {
						HudUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), event.x, event.y, event.x + 16, event.y + 16, -500, Color(0.9f, 0f, 0f, bgOpacity))
					}

					DisplayType.COLOR_FADE -> {
						var redness: Float = (2.7f * progressLeft).clamp(0f, 1f)
						var greenness: Float = (1.3f - (1.3f * progressLeft)).clamp(0f, 1f)

						if (Vice.config.DEVMODE) {
							redness = (Vice.devConfig.ITEMCD_RED_FADE_OVERRIDE * progressLeft).clamp(0f, 1f)
							greenness = (Vice.devConfig.ITEMCD_GREEN_FADE_OVERRIDE - (Vice.devConfig.ITEMCD_GREEN_FADE_OVERRIDE * progressLeft)).clamp(0f, 1f)
						}

						HudUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), event.x, event.y, event.x + 16, event.y + 16, 0, Color(redness, greenness, 0f, bgOpacity))
					}

					DisplayType.PERCENTAGE -> {
						val col = when {
							progressLeft <= 0.075f -> Color(0.4f, 1f, 0f)
							progressLeft > 0.075 && progressLeft <= 0.5f -> Color.yellow
							else -> Color.green
						}.withAlpha(bgOpacity)

						HudUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), event.x, event.y, event.x + 16, event.y + 16, 0, col)
					}
				}


			} else if (!Vice.config.HIDE_ITEMCD_WHEN_READY && (displayType != DisplayType.VANILLA && displayType != DisplayType.TEXT_ONLY)) {
				HudUtils.fillUIArea(matrices, RenderLayer.getGuiOverlay(), event.x, event.y, event.x + 16, event.y + 16, 0, Color.green.withAlpha(bgOpacity))
			}

			drawStatus(event.x, event.y + 9, event.context)
		}
	}

	private fun ItemAbility.drawStatus(x: Int, y: Int, context: DrawContext, centered: Boolean = false, defaultColor: Color = Color.white) {
		if (!Vice.config.SHOW_ITEMCD_TEXT && Vice.config.ITEMCD_DISPLAY_TYPE != DisplayType.TEXT_ONLY) return
		if (!Vice.config.ITEM_COOLDOWN_DISPLAY || !displayCooldown) return

		val matrices = context.matrices

		matrices.push()
		matrices.translate(0.0f, 0.0f, 200.0f)

		if (isOnCooldown()) {
			val roundedFloat: String = String.format("%.0f", ceil(remainingCooldown().toDouble()))
			HudUtils.drawText(roundedFloat, x, y, context, color = defaultColor.rgb, shadow = true, centered = centered)

		} else {
			var color = defaultColor

			if (this == ItemAbility.ARCTIC_SCROLL) {
				val hp = MinecraftClient.getInstance().player?.health ?: 0f
				color = if (hp <= 10f) Color.green else Color.red
			}

			HudUtils.drawText("R", x, y, context, color = color.rgb, shadow = true, centered = centered)
		}

		matrices.pop()
	}
}
