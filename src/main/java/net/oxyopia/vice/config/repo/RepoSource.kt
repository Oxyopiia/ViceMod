package net.oxyopia.vice.config.repo

data class RepoSource(
	val user: String,
	val name: String,
	val branch: String,
	val aggregateFileName: String
) {
	fun getAggregateFileUrl() = "https://raw.githubusercontent.com/$user/$name/$branch/$aggregateFileName"
	fun getCommitDataUrl() = "https://api.github.com/repos/$user/$name/commits/$branch"
}
