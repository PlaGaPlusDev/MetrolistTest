package com.metrolist.innertube.utils

import timber.log.Timber

actual val logger: Logger = object : Logger {
    override fun d(message: String) { Timber.d(message) }
    override fun e(t: Throwable, message: String) { Timber.e(t, message) }
    override fun i(message: String) { Timber.i(message) }
    override fun w(message: String) { Timber.w(message) }
}
