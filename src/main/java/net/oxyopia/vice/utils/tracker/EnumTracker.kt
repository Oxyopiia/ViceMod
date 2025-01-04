package net.oxyopia.vice.utils.tracker

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.MutableText
import net.oxyopia.vice.data.Colors
import net.oxyopia.vice.data.Size
import net.oxyopia.vice.data.gui.HudElement
import net.oxyopia.vice.data.gui.Position
import net.oxyopia.vice.events.HudRenderEvent
import net.oxyopia.vice.events.core.SubscribeEvent
import net.oxyopia.vice.utils.hud.HudUtils.drawTexts
import net.oxyopia.vice.utils.hud.HudUtils.toText
import kotlin.enums.EnumEntries
import kotlin.math.ceil

open class EnumTracker<T : Enum<T>>(
	val title: String,
	private val entries: EnumEntries<T>,
	private val getTrackedItems: () -> HashMap<String, Int>,
	private val getEnumFormatting: (T?) -> TrackerFormatting?,
	position: Position,
	storePosition: (Position) -> Unit,
	enabled: () -> Boolean,
	drawCondition: () -> Boolean = { true },
	searchTerm: String = title
) : HudElement(
	title,
	position,
	storePosition,
	enabled,
	drawCondition,
	searchTerm = searchTerm
) {
	private var defaultSpaceWidth: Int? = null

	@SubscribeEvent
	fun onHudRender(event: HudRenderEvent) {
		if (!canDraw()) return
		draw(event.context)
	}

	open fun draw(context: DrawContext): Size {
		val list = mutableListOf(title.toText(Colors.Wave, bold = true))
		list.addAll(getCounts().writeAligned())

		return position.drawTexts(list, context)
	}

	protected fun getCounts(): Map<String, Int> {
		val trackedItems = getTrackedItems()

		return entries.associate { enumItem ->
			val name = enumItem.name
			name to (trackedItems[name] ?: 0)
		}
	}

	private fun Map<String, Int>.getFormattingMap(): List<Pair<TrackerFormatting, Int>> {
		return map { (label, count) ->
			val enumEntry = this@EnumTracker.entries.find { label == it.name }
			val formatting = getEnumFormatting(enumEntry) ?: TrackerFormatting(label.toReadable(), Colors.ChatColor.Grey)

			formatting to count
		}
	}

	protected fun Map<String, Int>.writeDefault(): List<MutableText> {
		return getFormattingMap().map { (formatting, count) ->
			"§7${count}x ".toText().append(formatting.displayName.toText(formatting.color))
		}
	}

	protected fun Map<String, Int>.writeAligned(): List<MutableText> {
		val mappedFormatting = getFormattingMap()

		val textRenderer = MinecraftClient.getInstance().textRenderer
		if (defaultSpaceWidth == null) {
			defaultSpaceWidth = MinecraftClient.getInstance().textRenderer.getWidth(" ")
		}
		val spaceWidth = (defaultSpaceWidth ?: MinecraftClient.getInstance().textRenderer.getWidth(" "))  * position.scale

		val maxLabelWidth = mappedFormatting.maxOf { textRenderer.getWidth(it.first.displayName) * position.scale } + 3
		val maxNumberWidth = mappedFormatting.maxOf { textRenderer.getWidth(it.second.toString()) * position.scale }

		return mappedFormatting.map { (formatting, count) ->
			val leftWidth = textRenderer.getWidth(formatting.displayName)
			val leftPadSpacesNeeded = ceil((maxLabelWidth - leftWidth) / spaceWidth).toInt()
			val leftPad = " ".repeat(leftPadSpacesNeeded.takeIf{ it > 0 } ?: 0)

			val rightWidth = textRenderer.getWidth(count.toString())
			val rightPadSpacesNeeded = ceil((maxNumberWidth - rightWidth) / spaceWidth).toInt()
			val rightPad = " ".repeat(rightPadSpacesNeeded.takeIf{ it > 0 } ?: 0)

			val pad = leftPad + rightPad

			"${formatting.displayName}$leftWidth $leftPadSpacesNeeded $pad $rightPadSpacesNeeded $rightWidth $count".toText(formatting.color)
		}
	}

	private fun String.toReadable(): String = split("_").joinToString(" ") { it.lowercase().replaceFirstChar(Char::titlecase) }

	override fun Position.drawPreview(context: DrawContext): Size {
		return draw(context)
	}
}