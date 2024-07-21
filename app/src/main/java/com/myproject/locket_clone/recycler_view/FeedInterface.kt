package com.myproject.locket_clone.recycler_view

interface FeedInterface {
    fun onClickHeart(position: Int)
    fun onClickHaha(position: Int)
    fun onClickLike(position: Int)
    fun onClickWow(position: Int)
    fun onClickAngry(position: Int)
    fun onClickSad(position: Int)
    fun onClickUserProfile(position: Int)
    fun onClickAllFriends(position: Int)
    fun onClickSearchUser(position: Int)
    fun onClickGrid(position: Int)
    fun onClickMore(position: Int)
}