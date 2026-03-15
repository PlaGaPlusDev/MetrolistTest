package com.metrolist.innertube.utils

import com.metrolist.innertube.models.YouTubeLocale
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttpConfig
import okhttp3.ConnectionPool
import okhttp3.Protocol
import okhttp3.Cache
import java.io.File
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.Locale
import java.util.concurrent.TimeUnit

actual fun HttpClientConfig<*>.configurePlatformEngine(
    proxy: PlatformProxy?,
    proxyAuth: String?
) {
    (this as? HttpClientConfig<OkHttpConfig>)?.engine {
        config {
            connectionPool(
                ConnectionPool(
                    10,
                    5,
                    TimeUnit.MINUTES
                )
            )
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
            retryOnConnectionFailure(true)

            cache(
                Cache(
                    directory = File(System.getProperty("java.io.tmpdir"), "http_cache"),
                    maxSize = 50L * 1024L * 1024L
                )
            )

            proxy?.let {
                proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(it.host, it.port)))
            }

            proxyAuth?.let { auth ->
                proxyAuthenticator { _, response ->
                    response.request.newBuilder()
                        .header("Proxy-Authorization", auth)
                        .build()
                }
            }
        }
    }
}

actual fun getDefaultLocale(): YouTubeLocale = YouTubeLocale(
    gl = Locale.getDefault().country,
    hl = Locale.getDefault().toLanguageTag()
)
