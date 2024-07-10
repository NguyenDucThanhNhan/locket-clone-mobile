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

    data class FriendIdRequest(
        val friendId: String
    )

    data class SendInviteResponse(
        val message: String,
        val status: Int,
        val reasonPhrase: String?,
        val metadata: SendInviteMetadata?
    )

    data class SendInviteMetadata(
        val fullname: Fullname,
        val receivedInviteList: List<Friend>,
        val sentInviteList: List<Friend>,
        val _id: String,
        val email: String,
        val password: String,
        val birthday: String,
        val profileImageUrl: String,
        val createdAt: String,
        val updatedAt: String,
        val __v: Int,
        val friendList: List<Friend>
    )

    data class AcceptInviteResponse(
        val message: String,
        val status: Int,
        val reasonPhrase: String?,
        val metadata: FriendMetadata?
    )

    data class RemoveInviteRequest(
        val friendId: String
    )

    data class RemoveInviteResponse(
        val message: String,
        val status: Int,
        val reasonPhrase: String?,
        val metadata: FriendMetadata?
    )

    data class FriendMetadata(
        val fullname: Fullname,
        val _id: String,
        val email: String,
        val password: String,
        val birthday: String,
        val profileImageUrl: String,
        val friends: List<Friend>,
        val createdAt: String,
        val updatedAt: String,
        val __v: Int,
        val friendList: List<Friend>,
        val receivedInviteList: List<Friend>,
        val sentInviteList: List<Friend>
    )

    data class RemoveFriendResponse(
        val message: String,
        val status: Int,
        val reasonPhrase: String?,
        val metadata: FriendMetadata?
    )

    data class UserInfoResponse(
        val message: String,
        val status: Int,
        val reasonPhrase: String?,
        val metadata: UserInfoMetadata?
    )

    data class UserInfoMetadata(
        val _id: String,
        val email: String,
        val password: String? = null,
        val fullname: Fullname,
        val birthday: String,
        val profileImageUrl: String,
        val createdAt: String? = null,
        val updatedAt: String? = null,
        val __v: Int? = null,
        val friendList: List<Friend>? = null,
        val receivedInviteList: List<Friend>? = null,
        val sentInviteList: List<Friend>? = null
    )
}