package net.oxyopia.vice.config

import net.oxyopia.vice.utils.DevUtils
import net.oxyopia.vice.utils.TimeUtils.ms
import java.io.File
import java.io.Reader
import java.io.Writer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration.Companion.minutes

abstract class PersistentSave(private val file: File) {
	private var dirty = false

	abstract fun write(writer: Writer)
	abstract fun read(reader: Reader)

	private fun writeFile() {
		try {
			file.ensureFile()
			file.bufferedWriter().use {
				write(it)
			}
		} catch (e: Exception) {
			DevUtils.sendErrorMessage(e, "An error occurred writing to ${file.name}")
		}

		dirty = false
	}

	private fun readFile() {
		try {
			file.ensureFile()
			file.bufferedReader().use {
				read(it)
			}
		} catch (e1: Exception) {
			try {
				file.bufferedWriter().use {
					writeDefault(it)
				}
			} catch (e2: Exception) {
				DevUtils.sendErrorMessage(e1, "An error occurred reading ${file.name}")
			}
		}
	}

	open fun writeDefault(writer: Writer) {
		writer.write("{}")
	}

	fun markDirty() = run { dirty = true }

	fun initialize() {
		readFile()
	}

	fun forceSave() = writeFile()

	private fun File.ensureFile() = (parentFile.exists() || parentFile.mkdirs()) && createNewFile()

	init {
		fixedRateTimer("Vice-WritePersistentSave", period = 1.minutes.ms()) {
			if (dirty) writeFile()
		}

		Runtime.getRuntime().addShutdownHook(Thread({
			if (dirty) writeFile()
		}, "Vice-WritePersistentSave"))
	}
}