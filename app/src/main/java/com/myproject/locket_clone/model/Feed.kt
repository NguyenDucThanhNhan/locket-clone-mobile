package com.myproject.locket_clone.model

import java.io.Serializable

data class CreateFeedResponse(
    val message: String,
    val status: Int,
    val reasonPhrase: String?,
    val metadata: FeedMetadata?
)

data class FeedMetadata(
    val userId: String,
    val description: String,
    val imageUrl: String,
    val visibility: Any,
    val _id: String,
    val reactions: List<Reaction>,
    val reactionStatistic: ReactionStatistic,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class Reaction(
    val userId: String,
    val fullname: Fullname,
    val profileImageUrl: String,
    val icon: String
)

data class ReactionStatistic(
    val angry: Int,
    val haha: Int,
    val like: Int,
    val love: Int,
    val sad: Int,
    val wow: Int
)

data class Visibility(
    val id: String,
    val name: Fullname,
    val profileImageUrl: String,
    var isClick: Boolean
) : Serializable

data class GetCertainFeedsResponse(
    val message: String,
    val status: Int,
    val reasonPhrase: String?,
    val metadata: List<FeedMetadata>?
)

data class Feed(
    val userId: String,
    val description: String,
    val imageUrl: String,
    val visibility: Any,
    val _id: String,
    val reactions: List<Reaction>,
    val reactionStatistic: ReactionStatistic,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val name: String
)