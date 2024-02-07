package net.oxyopia.vice.config

import com.google.gson.annotations.Expose
import net.oxyopia.vice.Vice
import net.oxyopia.vice.Vice.Companion.version
import net.oxyopia.vice.config.features.MiscStorage
import net.oxyopia.vice.config.features.worlds.ArenaStorage
import net.oxyopia.vice.config.features.worlds.CookingStorage
import net.oxyopia.vice.config.features.worlds.ShowdownStorage
import java.io.File
import java.io.Reader
import java.io.Writer

class Storage : PersistentSave(File("./config/vice/storage.json")) {

	@Expose
	var isFirstUse: Boolean = true

	@Expose
	var lastVersion: String = version.friendlyString

	@Expose
	var arenas: ArenaStorage = ArenaStorage()

	@Expose
	var cooking: CookingStorage = CookingStorage()

	@Expose
	var showdown: ShowdownStorage = ShowdownStorage()

	@Expose
	var misc: MiscStorage = MiscStorage()

	override fun write(writer: Writer) {
		Vice.gson.toJson(this, writer)
	}

	override fun read(reader: Reader) {
		Vice.storage = Vice.gson.fromJson(reader, this.javaClass)
	}
}