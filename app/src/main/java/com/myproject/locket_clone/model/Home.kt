package com.myproject.locket_clone.model

class Home {
    data class SearchResponse(
        val message: String,
        val status: Int,
        val reasonPhrase: String? = null,
        val metadata: List<UserMetadata>? = null
    )

    data class UserMetadata(
        val _id: String,
        val fullname: Fullname,
        val profileImageUrl: String
    )

}