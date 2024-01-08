package net.oxyopia.vice.events

import net.minecraft.text.Text
import net.oxyopia.vice.events.core.Returnable

@Returnable
class ModifyBossBarEvent(val original: Text) : BaseEvent()