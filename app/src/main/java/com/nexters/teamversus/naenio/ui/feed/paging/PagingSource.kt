package com.nexters.teamversus.naenio.ui.feed.paging

sealed class PlaceholderState {
    data class Idle(val isEmpty: Boolean) : PlaceholderState()
    object Loading : PlaceholderState()
    data class Failure(val throwable: Throwable) : PlaceholderState()
}

interface PagingSource {
    fun loadNextPage()

    fun retry()

    fun refresh()
}

interface PagingSource2 {
    fun loadNextPage(id: Int)

    fun retry(id: Int)

    fun refresh(id: Int)
}