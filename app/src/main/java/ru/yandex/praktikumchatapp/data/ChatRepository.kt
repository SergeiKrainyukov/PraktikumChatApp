package ru.yandex.praktikumchatapp.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.retryWhen
import kotlin.math.pow

class ChatRepository(
    private val api: ChatApi = ChatApi()
) {
    fun getReplyMessage(): Flow<String> =
        api.getReply()
            .retryWhen { cause, attempt ->
                val delayTime = calculateExponentialDelay(attempt)
                delay(delayTime)
                true
            }


    private fun calculateExponentialDelay(attempt: Long): Long {
        val baseDelay = 1000L
        val maxDelay = 60000L
        val safeAttempt = attempt.coerceAtMost(10).toInt()
        val delay = baseDelay * (2.0.pow(safeAttempt)).toLong()
        return delay.coerceAtMost(maxDelay)
    }
}