package net.oxyopia.vice.events

import net.minecraft.client.gui.hud.ClientBossBar
import net.minecraft.text.Text

class ModifyBossBarEvent(val instance: ClientBossBar, val original: Text) : ViceEvent.Cancelable<Text>()