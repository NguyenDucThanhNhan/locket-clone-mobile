package com.myproject.locket_clone.model

data class NameChangeRequest(
    val firstname: String,
    val lastname: String
)

data class NameChangeResponse(
    val status: Int,
    val message: String,
    val reasonPhrase: String? = null,
    val metadata: NameChangeMetadata? = null
)

data class NameChangeMetadata(
    val fullname: NameChangeFullname? = null,
    val profileImageUrl: String
)

data class NameChangeFullname(
    val firstname: String,
    val lastname: String
)
