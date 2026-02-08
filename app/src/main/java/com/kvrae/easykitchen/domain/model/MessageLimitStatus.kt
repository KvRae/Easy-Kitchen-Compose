package com.kvrae.easykitchen.domain.model

sealed class MessageLimitStatus {
    data class Allowed(
        val remaining: Int,
        val maxPerDay: Int
    ) : MessageLimitStatus()

    data class LimitReached(
        val maxPerDay: Int
    ) : MessageLimitStatus()
}
