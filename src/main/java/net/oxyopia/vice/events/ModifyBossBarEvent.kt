package net.oxyopia.vice.events

import net.minecraft.client.gui.hud.ClientBossBar
import net.minecraft.text.Text
import net.oxyopia.vice.events.core.Returnable

@Returnable
class ModifyBossBarEvent(val instance: ClientBossBar, val original: Text) : ViceEvent()