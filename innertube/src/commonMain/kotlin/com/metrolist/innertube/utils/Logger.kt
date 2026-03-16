package com.metrolist.innertube.utils

interface Logger {
    fun d(message: String)
    fun e(t: Throwable, message: String)
    fun i(message: String)
    fun w(message: String)
}

expect val logger: Logger
