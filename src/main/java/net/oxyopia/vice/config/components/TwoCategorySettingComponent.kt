package net.oxyopia.vice.config.components

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.elementa.font.DefaultFonts
import gg.essential.elementa.state.BasicState
import gg.essential.vigilance.data.PropertyInfo
import gg.essential.vigilance.gui.DataBackedSetting
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.gui.settings.SettingComponent
import java.awt.Color
import java.lang.IllegalStateException

internal class TwoCategorySettingComponent(val options: List<String>, val value: MutableList<String>) : SettingComponent() {
	init {
		constrain {
			x = (DataBackedSetting.INNER_PADDING + 10f).pixels(alignOpposite = true)
			y = CenterConstraint()
			width = ChildBasedSizeConstraint()
			height = ChildBasedSizeConstraint()
		}
	}

	private val background by UIBlock().constrain {
		width = ChildBasedSizeConstraint() + 2.pixels
		height = ChildBasedMaxSizeConstraint()
		color = VigilancePalette.getComponentBackgroundHighlight().toConstraint()
	} childOf this

	private val leftSide by UIBlock().constrain {
		width = 10.pixels
		height = ((options.size + 1) * DefaultFonts.VANILLA_FONT_RENDERER.getBaseLineHeight()).pixels
		color = VigilancePalette.getComponentBackgroundHighlight().toConstraint()
	} childOf background effect OutlineEffect(BasicState(Color.BLACK), BasicState(1f))

	private val rightSide by UIBlock().constrain {
		x = SiblingConstraint(2f)
		width = 10.pixels
		height = CopyConstraintFloat() boundTo leftSide
		color = VigilancePalette.getComponentBackgroundHighlight().toConstraint()
	} childOf background effect OutlineEffect(BasicState(Color.BLACK), BasicState(1f))

	init {
		options.maxOf { option ->
			UIBlock()
				.constrain {
					y = SiblingConstraint()
					width = RelativeConstraint()
					height = ChildBasedSizeConstraint()
					color = VigilancePalette.getMidGray().toConstraint()
				}
				.childOf(if (option in value) rightSide else leftSide)
				.let {
					it.onMouseClick {
						if (isChildOf(leftSide)) {
							this.hide(instantly = true)
							childOf(rightSide)
							value.add(option)
							changeValue(value.toList())
						} else {
							this.hide(instantly = true)
							childOf(leftSide)
							value.remove(option)
							changeValue(value.toList())
						}
					}
					UIText(option).constrain { x = CenterConstraint() }.childOf(it).getTextWidth()
				}
		}.let {  width ->
			leftSide.constraints.width = width.pixels
			rightSide.constraints.width = width.pixels
		}
	}
}

class NumberCategoriserComponent : PropertyInfo() {
	override fun createSettingComponent(initialValue: Any?): SettingComponent {
		val options = listOf("One", "Two", "Three")
		return TwoCategorySettingComponent(options, (initialValue as? List<String>)?.toMutableList() ?: throw IllegalStateException("Wrong type"))
	}
}