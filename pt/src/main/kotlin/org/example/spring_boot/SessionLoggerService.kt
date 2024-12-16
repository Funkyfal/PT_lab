package org.example.spring_boot
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime

@Service
class SessionLoggerService {

    private val logFile = File("session_log.txt")

    init {
        if (!logFile.exists()) {
            logFile.createNewFile()
        }
    }

    fun logAction(action: String, clientName: String? = null, tourDestination: String? = null) {
        val timestamp = LocalDateTime.now()
        val logMessage = buildString {
            append("Время: $timestamp\n")
            append("Действие: $action\n")
            clientName?.let { append("Клиент: $it\n") }
            tourDestination?.let { append("Тур: $it\n") }
            append("\n")
        }
        logFile.appendText(logMessage)
    }

    fun getLastSessionLog(): String {
        val lines = logFile.readLines()
        return if (lines.isNotEmpty()) {
            lines.last()
        } else {
            "Лог пуст."
        }
    }
}
