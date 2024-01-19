package net.oxyopia.vice.events

import net.minecraft.client.gui.hud.ClientBossBar
import net.minecraft.text.Text

// TODO - Make event Returnable with String/Text Type
class ModifyBossBarEvent(val instance: ClientBossBar, val original: Text) : ViceEvent()