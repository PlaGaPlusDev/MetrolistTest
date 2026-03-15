package com.metrolist.innertube.utils

import com.metrolist.innertube.models.YouTubeLocale
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.http.auth.AuthScheme
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.Locale

actual fun HttpClientConfig<*>.configurePlatformEngine(
    proxy: PlatformProxy?,
    proxyAuth: String?
) {
    (this as? HttpClientConfig<CIOEngineConfig>)?.engine {
        proxy?.let {
            this.proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(it.host, it.port))
        }
        // CIO has different proxy auth handling, usually via interceptors or specific plugins
        // For a basic port, we focus on connectivity first
    }
}

actual fun getDefaultLocale(): YouTubeLocale = YouTubeLocale(
    gl = Locale.getDefault().country,
    hl = Locale.getDefault().toLanguageTag()
)
