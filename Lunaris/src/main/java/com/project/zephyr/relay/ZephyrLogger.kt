package com.project.zephyr.relay

interface ZephyrLogger {
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, message: String)
    fun s(tag: String, message: String)
}

class SystemLogger : ZephyrLogger {
    private val dateFormat = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.getDefault())

    override fun d(tag: String, message: String) = println("[${timestamp()}] [D/$tag] $message")
    override fun i(tag: String, message: String) = println("[${timestamp()}] [I/$tag] $message")
    override fun w(tag: String, message: String) = println("[${timestamp()}] [W/$tag] $message")
    override fun e(tag: String, message: String) = System.err.println("[${timestamp()}] [E/$tag] $message")
    override fun s(tag: String, message: String) = println("[${timestamp()}] [S/$tag] $message")

    private fun timestamp(): String = dateFormat.format(java.util.Date())
}
