package net.oxyopia.vice.events.repo

import net.oxyopia.vice.config.repo.RepoStorageData
import net.oxyopia.vice.events.ViceEvent

class RepoUpdateEvent(val newData: RepoStorageData) : ViceEvent()