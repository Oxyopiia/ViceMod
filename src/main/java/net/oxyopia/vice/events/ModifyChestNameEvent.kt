package net.oxyopia.vice.events

import net.minecraft.text.Text

class ModifyChestNameEvent(val original: Text) : ViceEvent.Cancelable<Text>()