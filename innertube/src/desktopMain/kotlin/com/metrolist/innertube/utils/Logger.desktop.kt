package com.metrolist.innertube.utils

actual val logger: Logger = object : Logger {
    override fun d(message: String) { println("D: $message") }
    override fun e(t: Throwable, message: String) { println("E: $message"); t.printStackTrace() }
    override fun i(message: String) { println("I: $message") }
    override fun w(message: String) { println("W: $message") }
}
