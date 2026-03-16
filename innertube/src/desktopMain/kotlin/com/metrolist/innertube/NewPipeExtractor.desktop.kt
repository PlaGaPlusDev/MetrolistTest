package com.metrolist.innertube

import com.metrolist.innertube.models.response.PlayerResponse

actual object NewPipeExtractor {
    actual fun getSignatureTimestamp(videoId: String): Result<Int> = Result.failure(Exception("Not implemented on desktop"))
    actual fun getStreamUrl(format: PlayerResponse.StreamingData.Format, videoId: String): String? = null
    actual fun newPipePlayer(videoId: String): List<Pair<Int, String>> = emptyList()
}
