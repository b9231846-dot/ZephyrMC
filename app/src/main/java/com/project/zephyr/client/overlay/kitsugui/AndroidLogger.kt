package com.project.zephyr.client.overlay.kitsugui

import androidx.compose.ui.graphics.Color
import com.project.zephyr.relay.ZephyrLogger
import java.text.SimpleDateFormat
import java.util.*

data class LogEntry(
    val timestamp: Long,
    val tag: String,
    val message: String,
    val level: LogLevel
)

enum class LogLevel(val color: Color, val symbol: String) {
    DEBUG(Color(0xFF6B7280), "D"),
    INFO(Color(0xFF3B82F6), "I"),
    WARNING(Color(0xFFF59E0B), "W"),
    ERROR(Color(0xFFEF4444), "E"),
    SUCCESS(Color(0xFF10B981), "S")
}

object LogcatBuffer {
    private const val MAX_LOGS = 500
    private val logs = CopyOnWriteArrayList<LogEntry>()
    private val dateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    fun d(tag: String, message: String) = log(LogLevel.DEBUG, tag, message)
    fun i(tag: String, message: String) = log(LogLevel.INFO, tag, message)
    fun w(tag: String, message: String) = log(LogLevel.WARNING, tag, message)
    fun e(tag: String, message: String) = log(LogLevel.ERROR, tag, message)
    fun s(tag: String, message: String) = log(LogLevel.SUCCESS, tag, message)

    private fun log(level: LogLevel, tag: String, message: String) {
        val entry = LogEntry(System.currentTimeMillis(), tag, message, level)
        logs.add(entry)
        if (logs.size > MAX_LOGS) {
            logs.removeAt(0)
        }
        println("[${dateFormat.format(Date(entry.timestamp))}] [${level.symbol}/$tag] $message")
    }

    fun getLogs(): List<LogEntry> = logs.toList()

    fun clear() {
        logs.clear()
    }
}

class AndroidLogger : ZephyrLogger {
    private val dateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    override fun d(tag: String, message: String) {
        LogcatBuffer.d(tag, message)
    }

    override fun i(tag: String, message: String) {
        LogcatBuffer.i(tag, message)
    }

    override fun w(tag: String, message: String) {
        LogcatBuffer.w(tag, message)
    }

    override fun e(tag: String, message: String) {
        LogcatBuffer.e(tag, message)
    }

    override fun s(tag: String, message: String) {
        LogcatBuffer.s(tag, message)
    }
}
