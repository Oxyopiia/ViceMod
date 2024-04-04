package net.oxyopia.vice.config.components

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.*
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.state.BasicState
import gg.essential.vigilance.data.PropertyInfo
import gg.essential.vigilance.gui.DataBackedSetting
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.gui.settings.SettingComponent
import gg.essential.vigilance.utils.onLeftClick
import java.lang.IllegalStateException

/**
 * TODO - Remove text input cursor element thing - see to textInput.setActive()
 * TODO - Display text as human readable (LSHIFT, W, TAB) rather than key code
 * TODO - Properly center text input inside container
 * TODO - Fix text input width to match text width for proper centering
 * TODO - Fix element not properly loosing focus using releaseWindowFocus()
 */
internal class KeybindComponent(val value: Int) : SettingComponent() {

	private val container by UIBlock().constrain {
		width = ChildBasedSizeConstraint() + 2.pixels
		height = ChildBasedMaxSizeConstraint() + 12.pixels
//		height = (3 * DefaultFonts.VANILLA_FONT_RENDERER.getBaseLineHeight()).pixels
		color = VigilancePalette.getDividerDark().toConstraint()
	} childOf this effect OutlineEffect(VigilancePalette.getComponentBorder(), 1f).bindColor(BasicState(VigilancePalette.getComponentBorder()))

	private val textInput by UITextInput(value.toString())
		.constrain {
			x = CenterConstraint()
			y = CenterConstraint()
			width = basicWidthConstraint { 20f }
			color = VigilancePalette.getPrimary().toConstraint()
			textScale = 1.5f.pixels
		} childOf container

	init {
		constrain {
			x = (DataBackedSetting.INNER_PADDING + 10f).pixels(alignOpposite = true)
			y = CenterConstraint()
			width = ChildBasedSizeConstraint()
			height = ChildBasedSizeConstraint()
		}

		container.onMouseEnter {
			container.animate {
				setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getButtonHighlight().toConstraint())
			}
		}.onMouseLeave {
			container.animate {
				setColorAnimation(Animations.OUT_EXP, 0.5f, VigilancePalette.getButton().toConstraint())
			}
		}

		textInput.onKeyType { _, keyCode ->
			changeValue(keyCode)
			textInput.setText(keyCode.toString())
			textInput.releaseWindowFocus()
		}.onLeftClick { event ->
			event.stopPropagation()
			textInput.grabWindowFocus()
		}.onFocus {
			textInput.setActive(true)
		}.onFocusLost {
			textInput.setActive(false)
		}
	}
}

/**
 * Custom Vigilance component used to set customisable keybinds.
 *
 * Stores keybinds as a key code integer.
 */
class GenericKeybindInput : PropertyInfo() {
	override fun createSettingComponent(initialValue: Any?): SettingComponent {
		return KeybindComponent((initialValue as? Int) ?: throw IllegalStateException("Incorrect type"))
	}
}

