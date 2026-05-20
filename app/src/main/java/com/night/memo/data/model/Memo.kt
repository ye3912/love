package com.night.memo.data.model

/**
 * Generic memo data model.
 * All state transitions are type-safe - no nulls.
 */
data class Memo(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Long,
    val isFavorite: Boolean = false
)
