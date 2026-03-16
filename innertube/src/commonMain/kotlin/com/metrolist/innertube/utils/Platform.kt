package com.metrolist.innertube.utils

import com.metrolist.innertube.models.YouTubeLocale
import io.ktor.client.HttpClientConfig

expect fun HttpClientConfig<*>.configurePlatformEngine(
    proxy: PlatformProxy?,
    proxyAuth: String?
)

expect fun getDefaultLocale(): YouTubeLocale

class PlatformProxy(val host: String, val port: Int)
