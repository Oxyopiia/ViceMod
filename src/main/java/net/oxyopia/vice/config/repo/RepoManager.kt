package net.oxyopia.vice.config.repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.minecraft.client.MinecraftClient
import net.oxyopia.vice.Vice
import net.oxyopia.vice.events.repo.RepoUpdateEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.Executors

object RepoManager {
	private val defaultRepoSource = RepoSource("Oxyopiia", "doomtowers-repo", "main", "all_data.json")

	private val cacheFile = File("./config/vice/repo.json")
	private val client = OkHttpClient()

	private val mainScope = CoroutineScope(Executors.newSingleThreadExecutor { runnable ->
		Thread(runnable, "RepoManager-Main").apply { isDaemon = true }
	}.asCoroutineDispatcher() + SupervisorJob())

	private var currentData: RepoStorageData? = null
	val data: RepoStorageData?
		get() = currentData

	fun initialize() {
		val localData = loadLocalData()
		Vice.logger.info("Initialising RepoManager")

		currentData = localData
		if (localData == null) {
			Vice.logger.warn("No local repo data available")
		}

		refreshData()
	}

	fun refreshData(onComplete: (success: Boolean) -> Unit = {}) {
		fetchLatestData(
			onSuccess = { newData ->
//				currentData = newData
				onComplete(true)
				Vice.EVENT_MANAGER.publish(RepoUpdateEvent(newData))
				Vice.logger.info("Updated data from repository (SHA: ${newData.sha})")
			},
			onFailure = {
				onComplete(false)
				Vice.logger.warn("Failed to fetch latest repository data, continuing with local data")
			}
		)
	}

	private fun fetchLatestData(onSuccess: (RepoStorageData) -> Unit, onFailure: () -> Unit) {
		val repoSource = getRepoSource()

		GlobalScope.launch(Dispatchers.IO) {
			try {
				val request = Request.Builder()
					.url(repoSource.getAggregateFileUrl())
					.build()
				val response = client.newCall(request).execute()
				if (response.isSuccessful) {
					val json = response.body?.string() ?: throw Exception("Empty response")
					val data = Vice.gson.fromJson(json, RepoStorageData::class.java)

					withContext(Dispatchers.IO) {
						cacheFile.writeText(json)
					}

					if (MinecraftClient.getInstance() != null) {
						MinecraftClient.getInstance().execute {
							onSuccess(data)
						}
					} else { // Fallback for initialization
						mainScope.launch {
							onSuccess(data)
						}
					}
				} else {
					throw Exception("Failed to fetch data: Error Code ${response.code}")
				}
			} catch (e: Exception) {
				Vice.logger.error("Failed to fetch latest data: ${e.message}")
				if (MinecraftClient.getInstance() != null) {
					MinecraftClient.getInstance().execute {
						onFailure()
					}
				} else {
					mainScope.launch {
						onFailure()
					}
				}
			}
		}
	}

	private fun loadLocalData(): RepoStorageData? {
		if (!cacheFile.exists()) {
			/*
			Provisions for default data file in the future if needed

			val defaultData = object {}.javaClass.getResourceAsStream("/default_data.json")
				?.bufferedReader()?.readText()
			if (defaultData != null) {
				cacheFile.writeText(defaultData)
				return Vice.gson.fromJson(defaultData, RepoStorage::class.java)
			}
			 */
			return null
		}

		return try {
			Vice.gson.fromJson(cacheFile.readText(), RepoStorageData::class.java)
		} catch (e: Exception) {
			Vice.logger.error("Failed to parse local data: ${e.message}")
			null
		}
	}

	private fun getLatestCommitSha(repoSource: RepoSource): String {
		val commitRequest = Request.Builder()
			.url(repoSource.getCommitDataUrl())
			.build()

		client.newCall(commitRequest).execute().use { response ->
			if (!response.isSuccessful) throw Exception("Failed to fetch commit: ${response.code}")

			val commitJson = response.body?.string() ?: throw Exception("Empty commit response")
			val commitData = Vice.gson.fromJson(commitJson, GitCommitResponse::class.java)
			return commitData.sha
		}
	}

	private fun getRepoSource(): RepoSource {
		val config = Vice.devConfig

		if (config.USE_CUSTOM_REPO) {
			return RepoSource(config.CUSTOM_REPO_USER, config.CUSTOM_REPO_NAME, config.CUSTOM_REPO_BRANCH, config.CUSTOM_REPO_GLOBAL_FILE_NAME)
		}

		return defaultRepoSource
	}

	private data class GitCommitResponse(
		val sha: String
	)
}



